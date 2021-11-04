package com.example.viewmodelapp.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
//        (application as DocumentApplication).getComponent()
//            .documentScreenComponent()
//            .create()
//            .inject(this)
        DocumentScreenComponentHolder.getComponent(this).inject(this)

        DocumentsFragment.attachIfNeeded(
            containerId = R.id.documentContainer,
            fragmentManager = supportFragmentManager
        )

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