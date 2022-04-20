package app.softwork.composetodo.viewmodels

import kotlinx.coroutines.*

expect abstract class ViewModel()

expect val ViewModel.lifecycleScope: CoroutineScope
