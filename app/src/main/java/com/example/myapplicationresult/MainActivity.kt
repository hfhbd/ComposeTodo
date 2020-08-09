package com.example.myapplicationresult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.setContent
import com.example.myapplicationresult.views.PressMeButton
import com.example.myapplicationresult.views.Todos

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Row {
                PressMeButton()
                Todos()
            }
        }
    }
}
