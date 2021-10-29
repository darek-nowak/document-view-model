package com.example.viewmodelapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentsList
import com.example.viewmodelapp.R
import com.example.viewmodelapp.di.DocumentScreenComponentHolder
import com.example.viewmodelapp.di.DocumentViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var documentViewModelFactory: DocumentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // wire up dagger - to get documentViewModelFactory injected ... (may take a while)
//        (application as DocumentApplication).getComponent()
//            .documentScreenComponent()
//            .create()
//            .inject(this)
        DocumentScreenComponentHolder.getComponent(this).inject(this)

        val viewModel: DocumentViewModel by viewModels { documentViewModelFactory }

        val docText = findViewById<TextView>(R.id.docText)
        val docButton = findViewById<TextView>(R.id.docButton)

        // read state for right fragment,
        // State { LIST, DETAIL }
        // ListData
        // DetailData

        viewModel.state.observe(this) { state ->
            when (state) {
                DocumentsList.InProgress -> docText.text = "in progress"
                DocumentsList.Error -> docText.text = "error"
                is DocumentsList.Success -> {
                    docText.text = state.data.map { it.name }.joinToString(",")
                }
            }

        }
        docButton.setOnClickListener {
            viewModel.click()
        }
    }
}