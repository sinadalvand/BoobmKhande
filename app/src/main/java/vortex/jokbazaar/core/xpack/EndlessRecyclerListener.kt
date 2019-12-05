package vortex.jokbazaar.core.xpack

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerListener<T : RecyclerView.LayoutManager>(
    val layoutManager: RecyclerView.LayoutManager,
    val pageItem: Int,
    val Threshold: Int = 5
) : RecyclerView.OnScrollListener() {

    private var loading = false
    private var totalItem = 0
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var currentPage = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = getFirstVIsibleItem()

        if (loading && totalItemCount > totalItem + 1) {
            loading = false
            totalItem = totalItemCount

        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + Threshold) {
            if (currentPage < totalItemCount / pageItem) {
                currentPage = totalItemCount / pageItem
                onLoadPage(currentPage + 1)
                loading = true
            }
        }
    }

    abstract fun onLoadPage(page: Int)


    private fun getFirstVIsibleItem(): Int {
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> 0
        }
    }
}