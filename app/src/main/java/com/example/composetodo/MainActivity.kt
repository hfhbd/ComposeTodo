package com.example.composetodo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import com.example.composetodo.repository.AppDatabase
import com.example.composetodo.repository.TodoRepository
import com.example.composetodo.viewmodels.PressMeViewModel
import com.example.composetodo.viewmodels.TodoViewModel
import com.example.composetodo.views.PressMeButton
import com.example.composetodo.views.Todos

class MainActivity : AppCompatActivity() {
    lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = Container()

        setContent {
            Row {
                PressMeButton(appContainer.pressMeViewModel)
                Todos(appContainer.todoViewModel)
            }
        }
    }

    inner class Container: AppContainer {
        private val db = AppDatabase.getInstance(applicationContext)
        override val todoViewModel = TodoViewModel(lifecycleScope, TodoRepository(db.todoDao))
        override val pressMeViewModel = PressMeViewModel(lifecycleScope)
    }
}
