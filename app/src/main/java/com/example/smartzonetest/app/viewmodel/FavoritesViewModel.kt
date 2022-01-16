package com.example.smartzonetest.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartzonetest.app.adapter.LocalAdapter
import com.example.smartzonetest.app.local.ArticleDatabse
import com.example.smartzonetest.network.responses.Article
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoritesViewModel : ViewModel() {

    private val TAG = "FavoritesViewModel"

    private var _localMutLiveData = MutableLiveData<MutableList<Article>>()
    val localMutLiveData = _localMutLiveData


    fun getLocalData(context: Context) {
        //--- Get Local Data ...
        ArticleDatabse.getInstance(context).articlesDao().getAllArticles()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<MutableList<Article>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(t: MutableList<Article>) {
                        _localMutLiveData.value = t
                    }

                    override fun onError(e: Throwable) {
                    }
                }
            )
    }

    fun removeItemLocalDatabase(context: Context, adapter: LocalAdapter) {
        adapter.setOnRemoveClickListener(object : LocalAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int, item: Article?) {
                //--- remove item depend on id ...
                ArticleDatabse.getInstance(context).articlesDao()
                    .deleteArticleItem(item!!.id.toInt())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        object : CompletableObserver {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onComplete() {

                                getLocalData(context)

                                Log.d(TAG, "Item Removed ... ")
                            }

                            override fun onError(e: Throwable) {
                                Log.d(TAG, "Error....")
                            }
                        }
                    )
            }
        })
    }

}