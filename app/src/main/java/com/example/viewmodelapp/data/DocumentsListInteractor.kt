package com.example.viewmodelapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class DocumentListsInteractor @Inject constructor(
    private val docRepository: GithubDocRepository
) {
    suspend fun getCvDocumentsListSuspend(): Result<List<CvDocumentInfo>> =
        asResult {
            docRepository.fetchDocumentsList()
                .map { file ->
                    CvDocumentInfo(
                        name = file.filename.toApplicantName(),
                        filename = file.filename
                    )
                }
        }

    private fun String.toApplicantName() =
        removeSuffix(".json")
            .split("_")
            .joinToString(" ") { it.capitalize() }
}

@Parcelize
data class CvDocumentInfo(val filename: String, val name: String) : Parcelable
