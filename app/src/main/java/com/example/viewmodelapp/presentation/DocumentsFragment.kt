package com.example.viewmodelapp.presentation

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentsState
import com.example.viewmodelapp.R
import com.example.viewmodelapp.data.CvDocumentInfo
import com.example.viewmodelapp.di.DocumentScreenComponentHolder
import com.example.viewmodelapp.di.DocumentViewModelFactory
import com.example.viewmodelapp.extensions.changeVisibility
import com.example.viewmodelapp.setUpAppBar
import kotlinx.android.synthetic.main.fragment_documents.*
import javax.inject.Inject

class DocumentsFragment: Fragment(R.layout.fragment_documents) {
    @Inject
    lateinit var documentViewModelFactory: DocumentViewModelFactory

    private val documentsAdapter = DocumentsAdapter()
    private val viewModel: DocumentViewModel by activityViewModels { documentViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DocumentScreenComponentHolder.getComponent(requireActivity()).inject(this)

        if (savedInstanceState == null) {
            // may not be needed since it is called by fragment manager
            viewModel.fetchDocuments()
        }
        setupList()
        requireActivity().setUpAppBar(titleText = getString(R.string.title_documents))

        val documentsList = view.findViewById<RecyclerView>(R.id.documentsList)
        val errorText = view.findViewById<TextView>(R.id.errorText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        viewModel.documents.observe(viewLifecycleOwner) { state ->
            when (state) {
                DocumentsState.InProgress -> {
                    progressBar.changeVisibility(true)
                }
                DocumentsState.Error -> {
                    errorText.changeVisibility(true)
                }
                is DocumentsState.Documents -> {
                    documentsList.changeVisibility(true)
                    progressBar.changeVisibility(false)
                    showData(state.data)
                }
            }
        }
    }
    private fun setupList() {
        documentsList.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = documentsAdapter
        }
        documentsAdapter.onItemClicked = { documentSelected ->
            DetailsFragment.attachIfNeeded(
                R.id.documentContainer,
                requireActivity().supportFragmentManager,
                documentSelected.filename
            )
        }
    }

    private fun showData(data: List<CvDocumentInfo>) {
        documentsAdapter.setItems(data)
    }

    companion object {
        private const val TAG = "documentsTag"

        fun attachIfNeeded(
            @IdRes containerId: Int,
            fragmentManager: FragmentManager
        ) {
            if(fragmentManager.findFragmentByTag(TAG) == null) {
                fragmentManager.beginTransaction()
                    .add(containerId, DocumentsFragment(), TAG)
                    .commitAllowingStateLoss()
            }
        }
    }
}