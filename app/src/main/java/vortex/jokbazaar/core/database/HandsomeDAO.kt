package vortex.jokbazaar.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.model.Post

@Dao
abstract class HandsomeDAO {

    @Query("SELECT COUNT(id) FROM Post")
    abstract fun getJokSize(): Int

//    @Query("SELECT * FROM jok ORDER BY id AND LIMIT ${Const.Jok_PAGE} OFFSET 100")
//    abstract fun getJok(page: Int): List<Jok>
    @Query("SELECT * FROM Post WHERE id >= :lastid ORDER BY id ASC limit ${Const.Jok_PAGE}")
    abstract fun getJokFrom(lastid:String): Flowable<Array<Post>>


    @Query("SELECT * FROM Post WHERE id == :id")
    abstract fun getJok(id:String): Single<Post>

//    @Query("SELECT * FROM jok ORDER BY id WHERE favorit = true AND LIMIT ${Const.Jok_PAGE}")
//    abstract fun getFavoritJok(lastId: Int): LiveData<List<Jok>>
    @Query("SELECT * FROM Post WHERE favorit == 1 ORDER BY time")
    abstract fun getFavoritJok(): Flowable<Array<Post>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertJoks(joks: Array<Post>):Single<Array<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFavoritJok(jok: Post):Single<Long>


//    @Query("SELECT * FROM notes WHERE note_text > :key ORDER BY note_text ASC limit :requestedLoadSize")
//    fun notesAfter(key: String, requestedLoadSize: Int): List<NoteEntity>
}