package vortex.jokbazaar.core.DragUtils

interface RecyclerDragCallback {

    fun LTRdrag(position:Int)
    fun RTLdrag(position:Int)
    fun done(position:Int)
}