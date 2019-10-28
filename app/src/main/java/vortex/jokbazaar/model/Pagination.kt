package vortex.jokbazaar.model

import com.google.gson.annotations.SerializedName

class Pagination <T> {

    @SerializedName("totalPage")
    var totalPage = -1


    @SerializedName("currentPage")
    var currentPage = -1


    @SerializedName("Data")
    val data = arrayListOf<T>()

}