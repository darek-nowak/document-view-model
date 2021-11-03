package com.example.viewmodelapp

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewmodelapp.data.DocumentDisplayItem
import com.example.viewmodelapp.presentation.DetailsAdapter
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment: Fragment(R.layout.fragment_details) {
    private val detailsAdapter = DetailsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val details = requireArguments().getParcelableArrayList<DocumentDisplayItem>(DOCUMENT_KEY) as List<DocumentDisplayItem>
        initRecyclerView(details)
    }

    private fun initRecyclerView(details: List<DocumentDisplayItem>) {
        documentDetails.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = detailsAdapter
        }
        detailsAdapter.setItems(details)
    }

    companion object {
        private const val DOCUMENT_KEY = "docKeyArg"

        fun attach(
            @IdRes containerId: Int,
            fragmentManager: FragmentManager,
            documentDetails: List<DocumentDisplayItem>
        ) {
            fragmentManager.beginTransaction()
                .replace(containerId, newInstance(documentDetails))
                .commitAllowingStateLoss()
        }

        private fun newInstance(documentDetails: List<DocumentDisplayItem>): Fragment = DetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(DOCUMENT_KEY, ArrayList(documentDetails))
            }
        }
    }
}