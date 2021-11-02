package com.example.viewmodelapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentsFragment
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

        val dataContainer = findViewById<FrameLayout>(R.id.container)
        val errorText = findViewById<TextView>(R.id.errorText)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // read state for right fragment,
        // State { LIST, DETAIL }
        // ListData
        // DetailData

        viewModel.state.observe(this) { state ->
            when (state) {
                DocumentsList.InProgress -> {
                    progressBar.setVisibility(true)
                }
                DocumentsList.Error -> {
                    errorText.setVisibility(true)
                }
                is DocumentsList.Success -> {
                    dataContainer.setVisibility(true)
                    progressBar.setVisibility(false)
                    DocumentsFragment.attach(
                        containerId = R.id.container,
                        fragmentManager = supportFragmentManager,
                        data = state.data
                    )
                }
            }
        }
    }
}

fun View.setVisibility(visible: Boolean) { visibility = if (visible) View.VISIBLE else View.GONE }