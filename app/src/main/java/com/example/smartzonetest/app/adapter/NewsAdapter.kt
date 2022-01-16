package com.example.smartzonetest.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartzonetest.R
import com.example.smartzonetest.app.activity.DetailsActivity
import com.example.smartzonetest.network.responses.Article
import de.hdodenhof.circleimageview.CircleImageView


class NewsAdapter(var context: Context, var list: MutableList<Article>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var onAddClick: OnItemClickListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_news, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val data = list[p1]

        if (!data.urlToImage.isNullOrEmpty()) {
            Glide.with(context)
                .load(data.urlToImage)
                .into(p0.articleImage)
        } else {
            Glide.with(context)
                .load(R.drawable.placeholder_image)
                .into(p0.articleImage)
        }
        p0.articleTitle.text = data.title
        p0.articleTime.text = data.publishedAt

        p0.itemView.setOnClickListener {
            //--- Go To Details Activity ...
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("allArticleData", list[p1])
            context.startActivity(intent)
        }


        p0.insertBtn.setOnClickListener {
            onAddClick?.onItemClick(p1, data)
        }

    }

    fun setOnAddClickListener(onAddClickListener: OnItemClickListener) {
        this.onAddClick = onAddClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var articleImage = itemView.findViewById<CircleImageView>(R.id.article_image)
        var articleTitle = itemView.findViewById<TextView>(R.id.article_title)
        var articleTime = itemView.findViewById<TextView>(R.id.article_time)
        var insertBtn = itemView.findViewById<Button>(R.id.btn_insert)
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int, item: Article?)
    }

    fun add(r: Article) {
        list.add(r)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(list: MutableList<Article>) {
        for (result in list) {
            add(result)
        }
    }
}