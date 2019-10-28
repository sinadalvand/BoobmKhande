package vortex.jokbazaar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Jok{

    @PrimaryKey
    @SerializedName("id")
    var id = -1


    @SerializedName("content")
    var content = ""


    @SerializedName("img")
    var img = -1


    @SerializedName("link")
    var link = -1

}