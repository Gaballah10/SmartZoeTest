package com.example.smartzonetest.app.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.smartzonetest.R
import com.example.smartzonetest.app.util.toast
import com.example.smartzonetest.app.viewmodel.DetailsActivityViewModel
import com.example.smartzonetest.databinding.ActivityDetailsBinding
import com.example.smartzonetest.network.responses.Article

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding
    lateinit var model: DetailsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000  // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        model = ViewModelProvider(this)[DetailsActivityViewModel::class.java]
        binding.lifecycleOwner = this

        model.getIntetData(this)

        model.image.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it)
                    .into(binding.topImage)
            }
        })
        model.image.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it)
                    .into(binding.topImage)
            }
        })
        model.title.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                binding.topTitle.setText(it)
            }
        })
        model.content.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                binding.newsContent.setText(it)
            }
        })
        model.author.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                binding.topAuthor.setText(it)
            }
        })
        model.date.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                binding.newsDate.setText(it)
            }
        })
    }
}