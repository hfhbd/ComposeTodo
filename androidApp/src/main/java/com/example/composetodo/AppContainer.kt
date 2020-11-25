package com.example.composetodo

import com.example.composetodo.viewmodels.PressMeViewModel
import com.example.composetodo.viewmodels.TodoViewModel

interface AppContainer {
    val todoViewModel: TodoViewModel
    val pressMeViewModel: PressMeViewModel
}
