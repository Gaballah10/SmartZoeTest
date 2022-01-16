package com.example.smartzonetest.app.local


import androidx.room.*
import com.example.smartzonetest.network.responses.Article
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article): Completable

    @Query("DELETE FROM article_table")
    fun deleteAllArticles(): Completable

    @Query("DELETE FROM article_table WHERE id = :id")
    fun deleteArticleItem(id: Int): Completable

    @Query("SELECT * FROM article_table")
    fun getAllArticles(): Single<MutableList<Article>>


}