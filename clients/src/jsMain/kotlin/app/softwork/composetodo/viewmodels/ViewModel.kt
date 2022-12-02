package app.softwork.composetodo.viewmodels

import kotlinx.coroutines.*

actual abstract class ViewModel actual constructor() {
    val lifecycleScope = CoroutineScope(Dispatchers.Default)
}

actual val ViewModel.lifecycleScope: CoroutineScope
    get() = lifecycleScope
