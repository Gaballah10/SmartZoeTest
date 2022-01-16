package com.example.smartzonetest.app.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartzonetest.R
import com.example.smartzonetest.app.adapter.LocalAdapter
import com.example.smartzonetest.app.local.ArticleDatabse
import com.example.smartzonetest.app.util.hide
import com.example.smartzonetest.app.util.show
import com.example.smartzonetest.app.util.toast
import com.example.smartzonetest.app.viewmodel.FavoritesViewModel
import com.example.smartzonetest.app.viewmodel.HomeViewModel
import com.example.smartzonetest.databinding.FragmentFavoritesBinding
import com.example.smartzonetest.network.responses.Article
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoritesFragment : Fragment() {

    lateinit var binding: FragmentFavoritesBinding
    lateinit var model: FavoritesViewModel
    var data: MutableList<Article> = ArrayList()
    private val adapter by lazy {
        LocalAdapter(requireContext(), data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        model =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressBarLocal.show()

        model.getLocalData(requireContext())
        model.localMutLiveData.observe(requireActivity(), Observer {
            if (it.isNullOrEmpty()) {
                binding.progressBarLocal.hide()
                binding.noDataLocal.show()
                binding.recylerLocalNews.hide()
            } else {
                data.clear()
                data.addAll(it)
                binding.progressBarLocal.hide()
                val llm = LinearLayoutManager(activity)
                binding.recylerLocalNews.layoutManager = llm
                binding.recylerLocalNews.adapter = adapter
                adapter.notifyDataSetChanged()
                model.removeItemLocalDatabase(requireContext(), adapter)
            }
        })

    }
}