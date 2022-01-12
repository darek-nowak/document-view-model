package com.example.viewmodelapp.presentation

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewmodelapp.*
import com.example.viewmodelapp.data.DocumentDisplayItem
import com.example.viewmodelapp.extensions.changeVisibility
import com.example.viewmodelapp.viewmodel.DetailsState
import com.example.viewmodelapp.viewmodel.DocumentViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment: Fragment(R.layout.fragment_details) {
    private val detailsAdapter = DetailsAdapter()
    private val viewModel: DocumentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            val documentName = requireArguments().getString(DOCUMENT_NAME_KEY)!!
            viewModel.fetchDetails(documentName)
        }

        setupList()
        requireActivity().setUpAppBar(titleText = getString(R.string.title_details), homeEnabled = true)

        val documentDetails = view.findViewById<RecyclerView>(R.id.documentDetails)
        val errorText = view.findViewById<TextView>(R.id.errorText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        viewModel.details.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailsState.InProgress -> {
                    progressBar.changeVisibility(true)
                }
                DetailsState.Error -> {
                    progressBar.changeVisibility(false)
                    errorText.changeVisibility(true)
                }
                is DetailsState.Details -> {
                    progressBar.changeVisibility(false)
                    documentDetails.changeVisibility(true)
                    showData(state.data)
                }
            }
        }
    }

    private fun setupList() {
        documentDetails.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = detailsAdapter
        }
    }

    private fun showData(details: List<DocumentDisplayItem>) {
        detailsAdapter.setItems(details)
    }

    companion object {
        private const val DOCUMENT_NAME_KEY = "docNameArg"

        fun attachIfNeeded(
            @IdRes containerId: Int,
            fragmentManager: FragmentManager,
            documentName: String
        ) {
            fragmentManager.beginTransaction()
                .replace(containerId, newInstance(documentName))
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        private fun newInstance(documentName: String): Fragment = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString(DOCUMENT_NAME_KEY, documentName)
            }
        }
    }
}