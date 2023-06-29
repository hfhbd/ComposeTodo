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
package com.google.cloud.tools.jib.plugins.common

import com.google.cloud.tools.jib.api.Containerizer
import com.google.cloud.tools.jib.api.JavaContainerBuilder
import com.google.cloud.tools.jib.api.JibContainerBuilder
import com.google.cloud.tools.jib.api.LogEvent
import com.google.cloud.tools.jib.plugins.common.RawConfiguration.ExtensionConfiguration
import com.google.cloud.tools.jib.plugins.extension.JibPluginExtensionException
import java.io.IOException
import java.nio.file.Path

/** Project property methods that require maven/gradle-specific implementations.  */
interface ProjectProperties {
    /**
     * Adds the plugin's event handlers to a containerizer.
     *
     * @param containerizer the containerizer to add event handlers to
     */
    // TODO: Move out of ProjectProperties.
    fun configureEventHandlers(containerizer: Containerizer?)
    fun log(logEvent: LogEvent?)
    val toolName: String?
    val toolVersion: String?
    val pluginName: String?

    /**
     * Starts the containerization process.
     *
     * @param javaContainerBuilder Java container builder to start with
     * @param containerizingMode mode to containerize the app
     * @return a [JibContainerBuilder] with classes, resources, and dependencies added to it
     * @throws IOException if there is a problem walking the project files
     */
    fun createJibContainerBuilder(
        javaContainerBuilder: JavaContainerBuilder?, containerizingMode: ContainerizingMode?
    ): JibContainerBuilder?
    
    val classFiles: List<Path?>?
    val dependencies: List<Path?>?
    val defaultCacheDirectory: Path?
    val jarPluginName: String?
    val name: String?
    val version: String?
    val majorJavaVersion: Int
    val isOffline: Boolean

    fun runPluginExtensions(
        extensionConfigs: List<ExtensionConfiguration?>?,
        jibContainerBuilder: JibContainerBuilder?
    ): JibContainerBuilder?

    companion object {
        /** Directory name for the cache. The directory will be relative to the build output directory.  */
        const val CACHE_DIRECTORY_NAME = "jib-cache"
    }
}
