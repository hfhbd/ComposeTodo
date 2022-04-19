package app.softwork.composetodo.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.*

actual typealias ViewModel = androidx.lifecycle.ViewModel

actual val ViewModel.lifecycleScope: CoroutineScope get() = this.viewModelScope
