package vortex.jokbazaar.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vortex.jokbazaar.BuildConfig
import vortex.jokbazaar.model.Category
import vortex.jokbazaar.model.Jok

@Database(entities = [Jok::class, Category::class], version = 1)
abstract class HandsomeDatabse : RoomDatabase() {

    abstract fun MainDAO(): HandsomeDAO

    companion object {
        @Volatile
        private var instance: HandsomeDatabse? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }

        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context, HandsomeDatabse::class.java, "joks.db").build()
    }
}