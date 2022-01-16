package com.example.smartzonetest.app.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.smartzonetest.R
import com.example.smartzonetest.app.adapter.NewsAdapter
import com.example.smartzonetest.app.local.ArticleDatabse
import com.example.smartzonetest.app.userrepo.NewsRepository
import com.example.smartzonetest.app.util.Constants
import com.example.smartzonetest.app.util.Coroutines
import com.example.smartzonetest.app.util.PaginationScrollListener
import com.example.smartzonetest.app.util.toast
import com.example.smartzonetest.app.viewmodel.HomeViewModel
import com.example.smartzonetest.databinding.FragmentHomeBinding
import com.example.smartzonetest.network.responses.Article
import com.example.smartzonetest.network.viewmodefac.HomeModelFactory
import com.example.smartzonetest.network.MyApi
import com.example.smartzonetest.network.responses.NewsInfo
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.util.HalfSerializer.onComplete
import io.reactivex.internal.util.HalfSerializer.onError
import io.reactivex.plugins.RxJavaPlugins.onSubscribe
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var model: HomeViewModel
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var isSearching: Boolean = false
    val llm = LinearLayoutManager(activity)
    var data: MutableList<Article> = ArrayList()
    var searchedData: MutableList<Article> = ArrayList()
    private val adapter by lazy {
        NewsAdapter(requireContext(), data)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        model =
            ViewModelProvider(this, HomeModelFactory(NewsRepository(MyApi()))).get(
                HomeViewModel::class.java
            )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressBar.show()

        //--- Getting Data By Coroutines ...     //--- only available pagination on 16 - 15 /2022
        Coroutines.main {
            val response = model.getNewsInfo(
                model.q,
                model.from,
                model.to,
                model.sortBy,
                Constants.newsApiKey
            ).value.await()
            response?.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    binding.progressBar.hide()
                    data.clear()
                    data.addAll(it.articles.toMutableList())
                    binding.recylerNews.setLayoutManager(llm)
                    binding.recylerNews.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                    onTextChange()
                    addToLocalDatabase()
                } else {
                    binding.progressBar.hide()
                    toast(getString(R.string.no_data))
                }

            })
        }


        //--- Recyclerview PaginationScrollListener for pagination ....
        binding.recylerNews.addOnScrollListener(object : PaginationScrollListener(llm) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (!isSearching) {
                    isLoading = true
                    model.from = "2022-01-15"
                    model.to = "2022-01-15"
                    model.getMoreData()
                    binding.progressBar.show()
                    toast(" Load More ....")

                    model.dataCalledBack.observe(requireActivity(), Observer {
                        if (!it.isNullOrEmpty()) {
                            binding.progressBar.hide()
                            adapter.addAll(it)
                            isLastPage = true
                            isLoading = false
                            adapter.notifyDataSetChanged()
                        }else
                            binding.progressBar.hide()
                    })
                }
            }
        })
    }

    //--- Search For Items By Title ...
    private fun onTextChange() {
        binding.textEditSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.trim()?.length!! > 0) {
                    model.filterData(s.toString(), searchedData, data)
                    adapter.list = searchedData
                    isSearching = true
                    adapter.notifyDataSetChanged()
                } else if (s?.trim()?.length!! == 0) {
                    isSearching = false
                    adapter.list = data
                    adapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //--- Add Item To Local Room Database ...
    fun addToLocalDatabase() {
        model.insertArticle(requireContext(), adapter)
    }
}