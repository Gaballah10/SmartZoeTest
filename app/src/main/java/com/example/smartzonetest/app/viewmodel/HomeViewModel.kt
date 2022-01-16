package com.example.smartzonetest.app.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartzonetest.R
import com.example.smartzonetest.app.adapter.NewsAdapter
import com.example.smartzonetest.app.local.ArticleDatabse
import com.example.smartzonetest.app.userrepo.NewsRepository
import com.example.smartzonetest.app.util.Constants
import com.example.smartzonetest.app.util.toast
import com.example.smartzonetest.network.MyApi
import com.example.smartzonetest.network.responses.Article
import com.example.smartzonetest.network.responses.NewsInfo
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeViewModel(val repository: NewsRepository) : ViewModel() {
    private val TAG = "HomeViewModel"

    var q: String = "apple"
    var from: String = "2022-01-16"
    var to: String = "2022-01-16"
    var sortBy: String = "popularity"

    private var _dataCalledBack = MutableLiveData<MutableList<Article>>()
    val dataCalledBack = _dataCalledBack

    fun getNewsInfo(
        q: String,
        from: String,
        to: String,
        sortBy: String,
        apiKey: String
    ): Lazy<Deferred<MutableLiveData<NewsInfo>?>> {
        return lazy {
            GlobalScope.async(start = CoroutineStart.LAZY) {
                repository.getNewsData(q, from, to, sortBy, apiKey)
            }
        }
    }

    fun filterData(value: String, filterData: MutableList<Article>, allData: MutableList<Article>) {
        filterData.clear()
        for (i in 0 until allData.size) {
            if (allData[i].title!!.contains(value)) {
                filterData.add(allData[i])
            }
        }
    }

    fun insertArticle(context: Context, adapter: NewsAdapter) {
        adapter.setOnAddClickListener(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int, item: Article?) {
                ArticleDatabse.getInstance(context).articlesDao().insertArticle(item!!)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        object : CompletableObserver {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onComplete() {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.data_added),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onError(e: Throwable) {
                                Log.d(TAG, "Error...")
                            }
                        }
                    )
            }
        })
    }

    //--- Get More Data For RecyclerView (Pagination)
    fun getMoreData() {
        MyApi().callBackNewsData(
            q,
            from,
            to,
            sortBy,
            Constants.newsApiKey
        )
            .enqueue(newsLoadNextPageCallback)
    }

    private val newsLoadNextPageCallback = object :
        Callback<NewsInfo> {
        override fun onFailure(call: Call<NewsInfo>, t: Throwable) {
            if (t !is IOException) {
                Log.d(TAG, t.message.toString())
            }
        }

        override fun onResponse(
            call: Call<NewsInfo>,
            response: Response<NewsInfo>
        ) {
            if (response.isSuccessful) {
                Log.d(TAG, "data Success ")
                _dataCalledBack.value = response.body()!!.articles.toMutableList()
            }
        }
    }

}