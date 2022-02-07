package com.example.viewmodelapp.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.viewmodelapp.R
import com.example.viewmodelapp.ViewModelApplication
import com.example.viewmodelapp.databinding.ActivityMainBinding
import com.example.viewmodelapp.di.DocumentScreenComponent
import com.example.viewmodelapp.di.DocumentViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var documentViewModelFactory: DocumentViewModelFactory

    lateinit var documentScreenComponent: DocumentScreenComponent

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        documentScreenComponent = ViewModelApplication.applicationComponent
            .documentScreenComponent()
            .create()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        documentScreenComponent.inject(this)

        attachListFragmentIfNeeded()
        setUpToolbar()
    }

    private fun attachListFragmentIfNeeded() {
        DocumentsListFragment.attachIfNeeded(
            containerId = R.id.documentContainer,
            fragmentManager = supportFragmentManager
        )
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
        setSupportActionBar(binding.toolbar)
    }
}