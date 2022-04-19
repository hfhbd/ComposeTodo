package app.softwork.composetodo.viewmodels

import kotlinx.coroutines.*

actual abstract class ViewModel actual constructor() {
    val lifecycleScope = MainScope()
}

actual val ViewModel.lifecycleScope: CoroutineScope
    get() = lifecycleScope
