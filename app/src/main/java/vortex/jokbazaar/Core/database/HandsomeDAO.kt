package vortex.jokbazaar.core.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.core.utils.Utils
import vortex.jokbazaar.model.Jok

@Dao
abstract class HandsomeDAO {

    @Query("SELECT * FROM jok WHERE id > :lastId AND LIMIT ${Const.Jok_PAGE}")
    abstract fun getJokpage(lastId: Int): LiveData<List<Jok>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertJok(joks: Array<Jok>)


}