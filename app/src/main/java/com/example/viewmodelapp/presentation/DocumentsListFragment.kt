package com.example.viewmodelapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewmodelapp.R
import com.example.viewmodelapp.data.CvDocumentInfo
import com.example.viewmodelapp.databinding.FragmentDocumentsBinding
import com.example.viewmodelapp.di.DocumentViewModelFactory
import com.example.viewmodelapp.extensions.changeVisibility
import com.example.viewmodelapp.setUpAppBar
import com.example.viewmodelapp.viewmodel.DocumentViewModel
import com.example.viewmodelapp.viewmodel.DocumentsState
import kotlinx.android.synthetic.main.fragment_documents.*
import javax.inject.Inject

class DocumentsListFragment: Fragment(R.layout.fragment_documents) {
    @Inject
    lateinit var documentViewModelFactory: DocumentViewModelFactory

    private lateinit var binding: FragmentDocumentsBinding

    private val documentsAdapter = DocumentsAdapter()
    private val viewModel: DocumentViewModel by activityViewModels { documentViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).documentScreenComponent.inject(this)

        if (savedInstanceState == null) {
            // may not be needed since it is called by fragment manager
            viewModel.fetchDocuments()
        }
        setupList()
        requireActivity().setUpAppBar(titleText = getString(R.string.title_documents))

        val documentsList = binding.documentsList
        val errorText = binding.errorText
        val progressBar = binding.progressBar

        viewModel.documents.observe(viewLifecycleOwner) { state ->
            when (state) {
                DocumentsState.InProgress -> {
                    progressBar.changeVisibility(true)
                }
                DocumentsState.Error -> {
                    progressBar.changeVisibility(false)
                    errorText.changeVisibility(true)
                }
                is DocumentsState.Documents -> {
                    progressBar.changeVisibility(false)
                    documentsList.changeVisibility(true)
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
            DocumentDetailsFragment.attachIfNeeded(
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
                    .add(containerId, DocumentsListFragment(), TAG)
                    .commitAllowingStateLoss()
            }
        }
    }
}