package com.example.smartzonetest.app.userrepo

import androidx.lifecycle.MutableLiveData
import com.example.smartzonetest.app.util.debug
import com.example.smartzonetest.network.responses.NewsInfo
import com.example.smartzonetest.network.MyApi
import com.example.smartzonetest.network.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class NewsRepository(
    private val apiService: MyApi
) : SafeApiRequest() {

    private val addNewsDataResponse = MutableLiveData<NewsInfo>()

    suspend fun getNewsData(
        q: String,
        from: String,
        to: String,
        sortBy: String,
        apiKey: String
    ): MutableLiveData<NewsInfo>? {
        return withContext(Dispatchers.IO) {
            return@withContext getNewsDatafromApi(q, from, to, sortBy, apiKey)
        }
    }

    private suspend fun getNewsDatafromApi(
        q: String,
        from: String,
        to: String,
        sortBy: String,
        apiKey: String
    ): MutableLiveData<NewsInfo>? {

        try {
            val response = apiRequest {
                apiService.getNewsData(q, from, to, sortBy, apiKey)
            }

            addNewsDataResponse.postValue(response)
            return addNewsDataResponse
        } catch (e: Exception) {
            e.printStackTrace()
            debug(e.message + " Casuse:${e.cause}")
        }
        return null
    }
}