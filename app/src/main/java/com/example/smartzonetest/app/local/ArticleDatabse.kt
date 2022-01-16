package com.example.smartzonetest.app.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartzonetest.app.local.converter.Converters
import com.example.smartzonetest.network.responses.Article

@Database(entities = [(Article::class)], version = ArticleDatabse.VERSION)
@TypeConverters(Converters::class)
abstract class ArticleDatabse : RoomDatabase() {


    companion object {
        const val DB_NAME = "articles.db"
        const val VERSION = 1

        private var INSTANCE: ArticleDatabse? = null
        private val LOCK = Any()

        @JvmStatic
        fun getInstance(context: Context): ArticleDatabse {
            synchronized(LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(
                            context.applicationContext,
                            ArticleDatabse::class.java,
                            DB_NAME
                        )
                        .build()
                }
                return INSTANCE!!
            }
        }
    }


    abstract fun articlesDao(): ArticlesDao
}
