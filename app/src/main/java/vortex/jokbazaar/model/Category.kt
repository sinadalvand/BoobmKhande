package vortex.jokbazaar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Category {

    @PrimaryKey
    @SerializedName("id")
    var id = -1


    @SerializedName("title")
    var title = -1
}