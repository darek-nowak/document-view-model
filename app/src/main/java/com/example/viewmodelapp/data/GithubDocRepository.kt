package com.example.viewmodelapp.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject

class GithubDocRepository @Inject constructor(
    private val api: GitHubApi,
    private val base64Decoder: Base64Decoder,
    private val jsonObjectMapper: ObjectMapper
) {
    fun fetchDocument(filename: String): Single<CvData> {
        return api.getFileContent(file = filename)
            .map { response -> base64Decoder.decode(response.content) }
            .map { jsonObjectMapper.readValue(it, CvData::class.java) }
    }

    suspend fun fetchDocumentsList(): List<FileInfo> = api.getFilesList()
}

interface GitHubApi {

    @GET("repos/$USER/$DOC_REPO/contents")
    suspend fun getFilesList(): List<FileInfo>

    @GET("repos/$USER/$DOC_REPO/contents/{file}")
    fun getFileContent(@Path("file") file: String): Single<FileContent>

    companion object {
        const val USER = "darek-nowak"
        const val DOC_REPO = "json"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class  FileInfo(
    @JsonProperty("name") val filename: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class  FileContent(
    @JsonProperty("encoding") val encoding: String,
    @JsonProperty("content") val content: String
)

data class CvData(
    @JsonProperty("applicant") val applicant: String,
    @JsonProperty("currentRole") val currentRole: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("experience") val experience: List<ExperienceItem>
)

data class ExperienceItem(
    @JsonProperty("startDate") val startDate: String,
    @JsonProperty("endDate") val endDate: String,
    @JsonProperty("company") val company: String,
    @JsonProperty("role") val role: String,
    @JsonProperty("responsibilities") val responsibilities: List<String>
)
