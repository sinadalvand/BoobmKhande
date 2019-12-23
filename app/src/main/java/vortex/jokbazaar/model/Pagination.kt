package vortex.jokbazaar.model

import com.google.gson.annotations.SerializedName

class Pagination <T> {

    @SerializedName("pages")
    var totalPage = -1


    @SerializedName("page")
    var currentPage = -1


    @SerializedName("docs")
    var data = arrayListOf<T>()

    // 0==> online     1==> offline
    var dataLoadMode = 0

}