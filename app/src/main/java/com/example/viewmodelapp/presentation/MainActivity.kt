package com.example.viewmodelapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.example.viewmodelapp.DetailsFragment
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentState
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
                DocumentState.InProgress -> {
                    progressBar.setVisibility(true)
                }
                DocumentState.Error -> {
                    errorText.setVisibility(true)
                }
                is DocumentState.Documents -> {
                    dataContainer.setVisibility(true)
                    progressBar.setVisibility(false)
                    DocumentsFragment.attach(
                        containerId = R.id.container,
                        fragmentManager = supportFragmentManager,
                        data = state.data
                    )
                }
                is DocumentState.Details -> {
                    dataContainer.setVisibility(true)
                    progressBar.setVisibility(false)
                    DetailsFragment.attach(
                        containerId = R.id.container,
                        fragmentManager = supportFragmentManager,
                        documentDetails = state.data
                    )
                }
            }
        }
        setUpToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
}

fun View.setVisibility(visible: Boolean) { visibility = if (visible) View.VISIBLE else View.GONE }