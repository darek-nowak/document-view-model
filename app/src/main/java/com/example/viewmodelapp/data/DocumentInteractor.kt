package com.example.viewmodelapp.data

import javax.inject.Inject

class DocumentInteractor @Inject constructor(
    private val docRepository: GithubDocRepository
) {
    suspend fun getCvDocument(filename: String): Result<List<DocumentDisplayItem>> =
        asResult {
            docRepository.fetchDocument(filename)
                .toDocumentDisplayItems()
        }
}
