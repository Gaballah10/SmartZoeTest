package com.example.smartzonetest.app.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartzonetest.network.responses.Article

class DetailsActivityViewModel : ViewModel()  {

    var image= MutableLiveData<String>()
    var title= MutableLiveData<String>()
    var content= MutableLiveData<String>()
    var author= MutableLiveData<String>()
    var date= MutableLiveData<String>()


    //--- Get All Data From Article Class ....
     fun getIntetData(activity: Activity) {
        val category = activity.intent.getSerializableExtra("allArticleData") as Article
         image.value = category.urlToImage.toString()
         title.value = category.title
         content.value = category.description
         author.value = category.author.toString()
         date.value = category.publishedAt
    }
}