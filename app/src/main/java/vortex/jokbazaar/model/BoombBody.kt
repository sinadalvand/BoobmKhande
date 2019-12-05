package vortex.jokbazaar.model

import com.google.gson.annotations.SerializedName

class BoombBody {

    @SerializedName("data")
    var data = Pagination<Post>()

}