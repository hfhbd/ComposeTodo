# ComposeTodo
Sample app to play with JetPack Compose, which shows following issues:
1. Calling `public suspend fun delay(duration: Duration): Unit` of kotlinx.coroutines results into a runtime error
2. kotlinx.serializer compiler plugin does not work with compose compiler extensions

## Branches
1. main: Custom, manually kotlinx serializer for `model/Todo.kt`
1. testCompilerPlugin: Generated serializer using kotlinx.serializer plugin for `model/Todo.kt`
