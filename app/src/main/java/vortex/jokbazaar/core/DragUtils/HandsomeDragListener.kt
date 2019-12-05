package vortex.jokbazaar.core.DragUtils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.BitmapCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import timber.log.Timber
import vortex.jokbazaar.R
import vortex.jokbazaar.core.adapter.MainRecyclerAdapter
import vortex.jokbazaar.core.utils.Utils
import kotlin.math.abs


class HandsomeDragListener(private val draggListener: RecyclerDragCallback) : ItemTouchHelper.Callback() {

    var swipeBack = false
    private var direction = Direction.NONE
    private var position = -1


    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is MainRecyclerAdapter.JokView)
            makeMovementFlags(0, ItemTouchHelper.END or ItemTouchHelper.START)
        else
            makeMovementFlags(0, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Timber.e("Swipped: ${direction}")
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        Timber.e("Dragged: ${actionState}")
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        var drag = actionState
        var dx = dX
        var isactive = isCurrentlyActive

        val offsetSize = viewHolder.itemView.width / 4
//        Timber.e("Offset: ${dx}     size: ${offsetSize}")
        position = viewHolder.adapterPosition


        var newDirection = direction
        if (dx <= -offsetSize || dx >= offsetSize) {
            drag = 0
            dx = if (dx < 0) (-offsetSize).toFloat() else offsetSize.toFloat()




            if (abs(dx).toInt() == offsetSize || dx.toInt() == 0) {
                newDirection = if (dx < 0) {
                    Direction.LEFT
                } else if (dx > 0) {
                    Direction.RIGHT
                } else {
                    Direction.NONE
                }
            }
            if (newDirection != Direction.NONE && direction != newDirection) {
                Utils.virateNow(viewHolder.itemView.context)
                Timber.e("Vibrate !!!")
            }


//            super.onChildDraw(c, recyclerView, viewHolder, dx, dY, drag, false)
//            return

        }


        drawThumber(viewHolder.itemView, c, dx / offsetSize)




        direction = newDirection
        if (dx == 0.0f && direction != Direction.NONE) {
            direction = Direction.NONE
            if (!swipeBack)
                draggListener.done(position)
        }

        if (actionState == ACTION_STATE_SWIPE) {
            setTouchListener(c, recyclerView, viewHolder, dx, dY, actionState, isCurrentlyActive)
        }



        super.onChildDraw(c, recyclerView, viewHolder, dx, dY, drag, isactive)
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {


        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            takeAction()
//            direction = Direction.NONE
            position = -1
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {


        recyclerView.setOnTouchListener { v, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            false
        }


    }

    private fun takeAction() {
        when (direction) {
            Direction.LEFT -> {
                draggListener.RTLdrag(position)
            }
            Direction.RIGHT -> {
                draggListener.LTRdrag(position)
            }
        }
    }

    private enum class Direction {
        LEFT, RIGHT, NONE
    }


    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private fun drawThumber(v: View, c: Canvas, m: Float) {

        paint.color = Color.RED

        val left = m > 0
        val morph = abs(m)
        val centerH = v.top + (v.height / 2)
        val circleRad = if (morph < 0.7) 280f * morph else 280f * 0.7f
        if (left) {

            val circleMove = if (morph < 0.7) 100f * morph else 100f * 0.7f
//            c.drawCircle(0f + circleMove, centerH.toFloat(), circleRad, paint)
//            c.drawRect(RectF(0f,centerH-circleRad,circleMove,centerH+circleRad),paint)


//            val path = Path()
//            path.moveTo(0f,v.top.toFloat())
//            path.cubicTo(
//                0f,v.top.toFloat()+(v.height/3),
//                circleRad*3.2f,centerH-(circleRad/6),
//                0f,centerH-circleRad)
//            path.close()
//            c.drawPath(path,paint)


            paint.color = ColorUtils.setAlphaComponent(Color.CYAN, 125)
            paint.shader = RadialGradient(
                0f,
                centerH.toFloat(),
                circleRad,
                paint.color,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
            c.drawCircle(0f, centerH.toFloat(), circleRad, paint)


//            c.drawBitmap(VectorDrawableCompat.create(v.resources,R.drawable.ic_favorite,null).current.bi)


        } else {
            paint.color =
                ColorUtils.setAlphaComponent(v.resources.getColor(R.color.colorPrimary), 200)
            paint.shader = RadialGradient(
                c.width.toFloat(),
                centerH.toFloat(),
                circleRad + 1,
                paint.color,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
            c.drawCircle(c.width.toFloat(), centerH.toFloat(), circleRad, paint)

//
//            val icon = VectorDrawableCompat.create(v.resources, R.drawable.ic_favorite, null)?.mutate()
//            val icons = BitmapFactory.decodeResource(v.resources,R.drawable.ic_share,BitmapFactory.Options())
//
//
//
//            c.drawBitmap(icons,Rect(0,0,icons.width,icons.height),Rect(0,centerH-20,40,centerH+20),paint)

//            load(R.drawable.ic_share)
//                .addListener(object : RequestListener<Bitmap> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Bitmap>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Bitmap?,
//                        model: Any?,
//                        target: Target<Bitmap>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//
//                    }
//
//                })

        }


    }

    fun getGradinatDrawable(context: Context): GradientDrawable {
        val gd = GradientDrawable(
            GradientDrawable.Orientation.RIGHT_LEFT, intArrayOf(
                ContextCompat.getColor(context, R.color.colorPrimary)
                , android.R.color.transparent
            )
        )

        return gd
    }

}