package co.rosemovie.app.Core.Xpack

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class XrecyclerAdapter<R, T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    protected var arrayData: ArrayList<R> = arrayListOf()

    override fun getItemCount(): Int = arrayData.size

    /**
     * this function delete previous array and replace new data and update position or body if need !
     * @param allData Collection<R> : pass all array that should show
     * @param reBorn Function1<[@kotlin.ParameterName] R, R> : if item particle views should update by array you must get every item and build new object from that and return it;
     */
    fun update(allData: Collection<R>, reBorn: (old: R) -> R = { it }) {
        val array = ArrayList<R>()
        for (data in allData)
            array.add(reBorn(data))
        val callback = getDiffCallback(this.arrayData, array)
        val diffResult = DiffUtil.calculateDiff(callback)
        this.arrayData.clear()
        this.arrayData.addAll(array)
        diffResult.dispatchUpdatesTo(this)
    }


    /**
     * this function add new data to end off recycler
     * @param newData Collection<R>
     */
    fun add(newData: Collection<R>) {
        val array = arrayListOf<R>()
        array.addAll(this.arrayData)
        array.addAll(newData)

        val callback = getDiffCallback(this.arrayData, array)
        val diffResult = DiffUtil.calculateDiff(callback)
        this.arrayData.clear()
        this.arrayData.addAll(array)
        diffResult.dispatchUpdatesTo(this)
    }


    protected fun getDiffCallback(oldList: List<R>, newList: List<R>): DiffUtil.Callback {
        return DiffBuffer(oldList, newList)
    }

    private class DiffBuffer<R>(
            private val oldList: List<R>,
            private val newList: List<R>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true
        }

    }


}