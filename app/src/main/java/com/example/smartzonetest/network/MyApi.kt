package com.example.smartzonetest.network

import com.example.smartzonetest.app.util.Constants
import com.example.smartzonetest.network.responses.NewsInfo
import com.rixos.app.data.network.NetworkConnectionInterceptor
import okhttp3.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyApi {

    @GET("everything")                             // ---  Coroutine ....
    suspend fun getNewsData(
        @Query("q") q: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String

    ): Response<NewsInfo>


    @GET("everything")                            //--- CallBack ...
    fun callBackNewsData(
        @Query("q") q: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): retrofit2.Call<NewsInfo>


    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor? = null
        ): MyApi {

            //--- Retrofit Create Builder ...

            val interceptor = Interceptor { chain ->
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .callTimeout(0, TimeUnit.MINUTES)
                .connectTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
                .addInterceptor(interceptor)

            return Retrofit.Builder()
                .client(client.build())
                .baseUrl(Constants.newsBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}



