import com.google.cloud.tools.jib.api.*
import com.google.cloud.tools.jib.api.buildplan.ContainerBuildPlan
import com.google.cloud.tools.jib.event.events.ProgressEvent
import com.google.cloud.tools.jib.event.events.TimerEvent
import com.google.cloud.tools.jib.event.progress.ProgressEventHandler
import com.google.cloud.tools.jib.gradle.extension.GradleData
import com.google.cloud.tools.jib.gradle.extension.JibGradlePluginExtension
import com.google.cloud.tools.jib.plugins.common.*
import com.google.cloud.tools.jib.plugins.common.RawConfiguration.ExtensionConfiguration
import com.google.cloud.tools.jib.plugins.common.globalconfig.GlobalConfig
import com.google.cloud.tools.jib.plugins.common.logging.ProgressDisplayGenerator
import com.google.cloud.tools.jib.plugins.common.logging.SingleThreadedExecutor
import com.google.cloud.tools.jib.plugins.extension.JibPluginExtensionException
import com.google.cloud.tools.jib.plugins.extension.NullExtension
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.Optional

plugins {
    id("application")
}

interface To {
    val image: Property<String>
}

interface From {
    val image: Property<String>
    val platforms: NamedDomainObjectContainer<Platform>
}

interface Platform : Named {
    val architecture: Property<String>
    val os: Property<String>
}

@CacheableTask
abstract class Jib : DefaultTask() {
    @get:Input
    @get:Nested
    abstract val from: From

    fun from(configure: Action<From>) = configure(from)

    @get:Input
    @get:Nested
    abstract val to: To

    fun to(configure: Action<To>) = configure(to)

    @get:Input
    abstract val entrypoint: Property<String>

    @get:Inject
    internal abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun createImages() {
        workerExecutor.noIsolation().submit(JibWorker::class.java) {
            this.from.set(this@Jib.from.image)
            this.to.set(this@Jib.to.image)
            this.platforms.set(this@Jib.from.platforms)
        }
    }
}

/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/** Obtains information about a Gradle [Project] that uses Jib.  */
class GradleProjectProperties internal constructor(
    private val project: Project,
    private val configurationName: String
) : ProjectProperties {
    private val singleThreadedExecutor = SingleThreadedExecutor()

    override fun createJibContainerBuilder(
        javaContainerBuilder: JavaContainerBuilder,
        containerizingMode: ContainerizingMode
    ): JibContainerBuilder {
        try {
            val projectDependencies: FileCollection = project.files(project.configurations.getByName(configurationName)
                .resolvedConfiguration
                .resolvedArtifacts
                .filter { artifact ->
                    artifact.id.componentIdentifier is ProjectComponentIdentifier
                }.map { it.file }
            )
            val mainSourceSet = mainSourceSet
            val classesOutputDirectories = mainSourceSet.output.classesDirs.filter { obj -> obj.exists() }
            val resourcesOutputDirectory = mainSourceSet.output.resourcesDir!!.toPath()
            val allFiles: FileCollection =
                project.configurations.getByName(configurationName).filter { obj: File -> obj.exists() }
            val nonProjectDependencies = allFiles
                .minus(classesOutputDirectories)
                .minus(projectDependencies)
                .filter { file: File -> file.toPath() != resourcesOutputDirectory }
            val snapshotDependencies = nonProjectDependencies.filter { file: File ->
                file.name.contains("SNAPSHOT")
            }
            val dependencies = nonProjectDependencies.minus(snapshotDependencies)

            // Adds dependency files
            javaContainerBuilder
                .addDependencies(dependencies.files.map { obj -> obj.toPath() })
                .addSnapshotDependencies(snapshotDependencies.files.map { obj -> obj.toPath() })
                .addProjectDependencies(projectDependencies.files.map { obj -> obj.toPath() })

            when (containerizingMode) {
                ContainerizingMode.EXPLODED -> {
                    // Adds resource files
                    if (Files.exists(resourcesOutputDirectory)) {
                        javaContainerBuilder.addResources(resourcesOutputDirectory)
                    }

                    // Adds class files
                    for (classesOutputDirectory in classesOutputDirectories) {
                        javaContainerBuilder.addClasses(classesOutputDirectory.toPath())
                    }
                }

                ContainerizingMode.PACKAGED -> {
                    // Add a JAR
                    val jarTask = tasks.jar
                    val jarPath = jarTask.flatMap { it.archiveFile.map { it.asFile.toPath() } }
                    javaContainerBuilder.addToClasspath(jarPath.get())
                }
            }
            return javaContainerBuilder.toContainerBuilder()
        } catch (ex: IOException) {
            throw GradleException("Obtaining project build output files failed", ex)
        }
    }

    override fun getClassFiles(): List<Path> {
        return sourceSets.main.flatMap {
            it.java.classesDirectory
        }.map { it.asFileTree }.get().map {
            it.toPath()
        }
    }

    override fun getDependencies(): List<Path> {
        val dependencies: MutableList<Path> = ArrayList<Path>()
        val runtimeClasspath: FileCollection = project.configurations.getByName(configurationName)
        // To be on the safe side with the order, calling "forEach" first (no filtering operations).
        runtimeClasspath.forEach { file ->
            if ((file.exists() && file.isFile && file.name.lowercase().endsWith(".jar"))) {
                dependencies.add(file.toPath())
            }
        }
        return dependencies
    }

    override fun configureEventHandlers(containerizer: Containerizer) {
        containerizer
            .addEventHandler(LogEvent::class.java) { logEvent -> log(logEvent) }
            .addEventHandler(TimerEvent::class.java, TimerEventHandler { message -> log(LogEvent.debug(message)) })
            .addEventHandler(
                ProgressEvent::class.java,
                ProgressEventHandler { update ->
                    val footer =
                        ProgressDisplayGenerator.generateProgressDisplay(update.progress, update.unfinishedLeafTasks)
                    footer.add("")
                }
            )
    }

    override fun getDefaultCacheDirectory(): Path {
        return project.buildDir.toPath().resolve(ProjectProperties.CACHE_DIRECTORY_NAME)
    }

    override fun getJarPluginName(): String {
        return JAR_PLUGIN_NAME
    }

    override fun getName(): String {
        return project.name
    }

    override fun getVersion(): String {
        return project.getVersion().toString()
    }

    override fun getMajorJavaVersion(): Int {
        var version = JavaVersion.current()
        val javaPluginConvention: JavaPluginConvention =
            project.getConvention().findPlugin(JavaPluginConvention::class.java)
        if (javaPluginConvention != null) {
            version = javaPluginConvention.targetCompatibility
        }
        return java.lang.Integer.valueOf(version.getMajorVersion())
    }

    override fun isOffline(): Boolean {
        return project.getGradle().getStartParameter().isOffline()
    }

    @Throws(JibPluginExtensionException::class)
    override fun runPluginExtensions(
        extensionConfigs: List<ExtensionConfiguration>,
        jibContainerBuilder: JibContainerBuilder
    ): JibContainerBuilder {
        if (extensionConfigs.isEmpty()) {
            log(LogEvent.debug("No Jib plugin extensions configured to load"))
            return jibContainerBuilder
        }
        val loadedExtensions = extensionLoader.get()
        var extension: JibGradlePluginExtension<*>? = null
        var buildPlan = jibContainerBuilder.toContainerBuildPlan()
        try {
            for (config: ExtensionConfiguration in extensionConfigs) {
                extension = findConfiguredExtension(loadedExtensions, config)
                log(LogEvent.lifecycle("Running extension: " + config.extensionClass))
                buildPlan = runPluginExtension(extension.getExtraConfigType(), extension, config, buildPlan)
                ImageReference.parse(buildPlan.baseImage) // to validate image reference
            }
            return jibContainerBuilder.applyContainerBuildPlan(buildPlan)
        } catch (ex: InvalidImageReferenceException) {
            throw JibPluginExtensionException(
                com.google.common.base.Verify.verifyNotNull(extension).javaClass,
                "invalid base image reference: " + buildPlan.baseImage,
                ex
            )
        }
    }

    // Unchecked casting: "getExtraConfiguration().get()" (Object) to Action<T> and "extension"
    // (JibGradlePluginExtension<?>) to JibGradlePluginExtension<T> where T is the extension-defined
    // config type (as requested by "JibGradlePluginExtension.getExtraConfigType()").
    @Throws(JibPluginExtensionException::class)
    private fun <T> runPluginExtension(
        extraConfigType: Optional<Class<T>>,
        extension: JibGradlePluginExtension<*>?,
        config: ExtensionConfiguration,
        buildPlan: ContainerBuildPlan
    ): ContainerBuildPlan {
        var extraConfig: T? = null
        val configs = config.extraConfiguration
        if (configs.isPresent) {
            if (!extraConfigType.isPresent) {
                throw java.lang.IllegalArgumentException(
                    "extension "
                            + extension!!.javaClass.simpleName
                            + " does not expect extension-specific configuration; remove the inapplicable "
                            + "'pluginExtension.configuration' from Gradle build script"
                )
            } else {
                // configs.get() is of type Action, so this cast always succeeds.
                // (Note generic <T> is erased at runtime.)
                val action = configs.get() as Action<T?>
                extraConfig = project.getObjects().newInstance(extraConfigType.get(), project)
                action.execute(extraConfig)
            }
        }
        try {
            return (extension as JibGradlePluginExtension<T>?)
                .extendContainerBuildPlan(
                    buildPlan,
                    config.properties,
                    java.util.Optional.ofNullable<T>(extraConfig),
                    GradleData { project },
                    PluginExtensionLogger(java.util.function.Consumer<LogEvent> { logEvent: LogEvent ->
                        log(
                            logEvent
                        )
                    })
                )
        } catch (ex: RuntimeException) {
            throw JibPluginExtensionException(
                extension!!.javaClass, "extension crashed: " + ex.message, ex
            )
        }
    }

    @Throws(JibPluginExtensionException::class)
    private fun findConfiguredExtension(
        extensions: List<JibGradlePluginExtension<*>>,
        config: ExtensionConfiguration
    ): JibGradlePluginExtension<*> {
        val matchesClassName =
            { extension: JibGradlePluginExtension<*> -> (extension.javaClass.name == config.extensionClass) }
        val found = extensions.firstOrNull(matchesClassName) ?: throw JibPluginExtensionException(
            NullExtension::class.java, (
                    "extension configured but not discovered on Jib runtime classpath: "
                            + config.extensionClass)
        )
        return found
    }

    companion object {
        /**
         * Generate an instance for a gradle project.
         *
         * @param project a gradle project
         * @param logger a gradle logging instance to use for logging during the build
         * @param tempDirectoryProvider for scratch space during the build
         * @param configurationName the configuration of which the dependencies should be packed into the
         * container
         * @return a GradleProjectProperties instance to use in a jib build
         */
        fun getForProject(
            project: Project,
            configurationName: String
        ): GradleProjectProperties {
            return GradleProjectProperties(
                project, configurationName
            )
        }

        /**
         * Returns the input files for a task. These files include the gradle [ ], output directories (classes, resources, etc.) of the
         * main [org.gradle.api.tasks.SourceSet], and any extraDirectories defined by the user to
         * include in the container.
         *
         * @param project the gradle project
         * @param extraDirectories the image's configured extra directories
         * @return the input files
         */
        fun getInputFiles(
            project: Project, extraDirectories: List<Path>, configurationName: String?
        ): FileCollection {
            val dependencyFileCollections: MutableList<FileCollection> = java.util.ArrayList<FileCollection>()
            dependencyFileCollections.add(project.getConfigurations().getByName(configurationName))
            // Output directories (classes and resources) from main SourceSet are added
            // so that BuildTarTask picks up changes in these and do not skip task
            val javaPluginConvention: JavaPluginConvention = project.getConvention().getPlugin(
                JavaPluginConvention::class.java
            )
            val mainSourceSet = javaPluginConvention.sourceSets.getByName(MAIN_SOURCE_SET_NAME)
            dependencyFileCollections.add(mainSourceSet.output)
            extraDirectories
                .filter { path: Path -> Files.exists(path) }
                .map { obj: Path -> obj.toFile() }
                .map(project::files)
                .forEach(dependencyFileCollections::add)
            return project.files(dependencyFileCollections)
        }
    }
}

abstract class JibWorker : WorkAction<JibWorker.JibParameters> {
    interface JibParameters : WorkParameters {
        val from: Property<String>
        val to: Property<String>
        val platforms: ListProperty<Platform>
    }

    override fun execute() {
        val projectProperties = GradleProjectProperties.getForProject(
            project,
            getConfigurationName().get()
        )
        PluginConfigurationProcessor.createJibBuildRunnerForRegistryImage(
            GradleRawConfiguration(jibExtension),
            { Optional.empty() },
            projectProperties,
            GlobalConfig.readConfig(),
        )
            .runBuild()
    }
}

tasks.withType<Jib>().configureEach {
    entrypoint.convention(application.mainClass)
}
