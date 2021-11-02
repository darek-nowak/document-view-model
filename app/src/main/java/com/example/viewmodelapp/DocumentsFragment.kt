package com.example.viewmodelapp

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewmodelapp.data.CvDocumentInfo
import com.example.viewmodelapp.presentation.DocumentsListAdapter
import kotlinx.android.synthetic.main.fragment_documents.*

class DocumentsFragment: Fragment(R.layout.fragment_documents) {
    private val viewModel: DocumentViewModel by activityViewModels()
    private val documentsAdapter = DocumentsListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = requireArguments().getParcelableArrayList<CvDocumentInfo>(DOCUMENTS_LIST) as List<CvDocumentInfo>
        initList(data)
    }

    private fun initList(data: List<CvDocumentInfo>) {
        allDocumentsList.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = documentsAdapter
        }
        documentsAdapter.setItems(data)
    }

    companion object {
        private val DOCUMENTS_LIST = "documentsArg"

        fun attach(
            @IdRes containerId: Int,
            fragmentManager: FragmentManager,
            data: List<CvDocumentInfo>
        ) {
            fragmentManager.beginTransaction()
                .add(containerId, newInstance(data))
                .commitAllowingStateLoss()
        }

        private fun newInstance(data: List<CvDocumentInfo>) = DocumentsFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(DOCUMENTS_LIST, ArrayList(data))
            }
        }
    }
}