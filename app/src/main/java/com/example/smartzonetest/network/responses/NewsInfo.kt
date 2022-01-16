package com.example.smartzonetest.network.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import java.io.Serializable


@Parcel
data class NewsInfo(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Long,
    @SerializedName("articles")
    val articles: List<Article>,
)

@Parcel
@Entity(tableName = "article_table")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("source")
    val source: Source,
    @SerializedName("author")
    val author: String?,
    @SerializedName( "title")
    val title: String,
    @SerializedName( "description")
    val description: String,
    @SerializedName( "url")
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName( "content")
    val content: String?,
) : Serializable

@Parcel
@Entity
data class Source(
    @SerializedName( "id")
    val id: String?,
    @SerializedName( "name")
    val name: String,
) : Serializable