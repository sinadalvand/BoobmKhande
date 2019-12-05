package vortex.jokbazaar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Post{

    @PrimaryKey
    @SerializedName("id")
    var id = ""


    @SerializedName("body")
    var content = ""


    @SerializedName("image")
    var img:String? = ""


    @SerializedName("timestamp")
    var time = 0L



    var favorit = false

}