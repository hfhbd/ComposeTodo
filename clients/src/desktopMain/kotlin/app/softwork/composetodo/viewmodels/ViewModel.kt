package app.softwork.composetodo.viewmodels

import kotlinx.coroutines.*

actual abstract class ViewModel actual constructor() {
    val lifecycleScope: CoroutineScope = CoroutineScope(
        Job(SupervisorJob()) + Dispatchers.Main.immediate
    )
}

actual val ViewModel.lifecycleScope: CoroutineScope
    get() = lifecycleScope
