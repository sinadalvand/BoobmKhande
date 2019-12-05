package vortex.jokbazaar.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ir.tapsell.sdk.nativeads.TapsellNativeBannerManager
import ir.tapsell.sdk.nativeads.TapsellNativeBannerViewManager
import kotlinx.android.synthetic.main.recycler_item_ad.view.*
import kotlinx.android.synthetic.main.recycler_item_guide.view.*
import kotlinx.android.synthetic.main.recycler_item_jok.view.*
import kotlinx.android.synthetic.main.recycler_item_position.view.*
import timber.log.Timber
import vortex.jokbazaar.BuildConfig
import vortex.jokbazaar.R
import vortex.jokbazaar.core.DragUtils.RecyclerDragCallback
import vortex.jokbazaar.core.database.HandsomeDAO
import vortex.jokbazaar.core.listener.FavorListener
import vortex.jokbazaar.core.security.offline.Reactor
import vortex.jokbazaar.core.utils.Utils
import vortex.jokbazaar.core.utils.gone
import vortex.jokbazaar.core.utils.invisible
import vortex.jokbazaar.core.utils.visible
import vortex.jokbazaar.model.Post


class MainRecyclerAdapter(
    private val context: Context,
    val reactor: Reactor,
    val dao: HandsomeDAO,
    val listener: FavorListener? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerDragCallback {

    private val GUIDE = 0
    private val POSITION = 1
    private val JOK = 2
    private val ADVERTISE = 3

    private val POSI_POSITION = 50
    private val AD_POSITION = 20


    private var showGuide = false

    private var joks = arrayListOf<Post>()
    public var ads = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getViewHolder(viewType, parent)

    override fun getItemViewType(position: Int): Int = positionLogic(position)
    override fun getItemCount(): Int = getItemsCount()
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        viewHolderHandler(holder, position)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.size > 0 && payloads[0] is Int) {
            val jok = getJok(position)
            (holder as JokView).star.alpha = if (jok.favorit) 1f else 0.2f
        } else
            super.onBindViewHolder(holder, position, payloads)
    }

    fun addPost(array: Array<Post>) {
        val position = getItemsCount()
        joks.clear()
        joks.addAll(array)
        notifyItemInserted(position)
        notifyDataSetChanged()
    }

    fun addAd(ad: Array<String>) {
        val position = getItemsCount()
        ads.clear()
        ads.addAll(ad)
        notifyItemInserted(position)
        notifyDataSetChanged()
    }

    class DescriptionView(v: View) : RecyclerView.ViewHolder(v) {
        val text = v.Text_guide
    }

    private fun DescriptViewHandle(holder: DescriptionView) {
        holder.text.text = "Description"
    }

    class PositionerView(v: View) : RecyclerView.ViewHolder(v) {
        val text = v.Text_position
    }

    private fun PositionerViewHandle(holder: PositionerView, pos: Int) {
        holder.text.text = "Positioner ${pos}"
    }

    class JokView(v: View) : RecyclerView.ViewHolder(v) {
        val text = v.recycler_item_content_text
        val image = v.recycler_item_content_Image
        val star = v.recycler_item_content_star
    }

    private fun JokViewHandle(holder: JokView, Jok: Post, position: Int) {

        holder.text.text = HtmlCompat.fromHtml(Jok.content, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.star.alpha = if (Jok.favorit) 1f else 0.2f


        val params = holder.text.layoutParams as RelativeLayout.LayoutParams
        if (Jok.img == "") {
            params.topMargin = Utils.dpToPx(40)
            holder.image.gone()
        } else {
            params.topMargin = Utils.dpToPx(125)
            holder.image.visible()
            Glide.with(holder.itemView.context).load(R.drawable.test)
                .apply(RequestOptions().skipMemoryCache(true)).into(holder.image)
        }
        holder.text.layoutParams = params


    }

    inner class AdView(val v: View) : RecyclerView.ViewHolder(v) {
        private var nativeBannerViewManager: TapsellNativeBannerViewManager? = null

        init {
            nativeBannerViewManager = TapsellNativeBannerManager.Builder()
                .setParentView(v.recycler_item_ad_container)
                .setContentViewTemplate(R.layout.tapsell_view)
                .inflateTemplate(v.context)
        }

        fun bindView(position: Int) {
            TapsellNativeBannerManager.bindAd(
                v.context,
                nativeBannerViewManager,
                BuildConfig.TAPSELL_NATIVE_BANNER,
                ads[position]
            )
        }

        fun hideAd(hide: Boolean) {
            if (hide) {
                v.gone()
            } else {
                v.visible()
            }
        }
    }

    private fun AdViewHandle(holder: AdView, position: Int) {
        val pos = position / if (ads.isEmpty()) 1 else ads.size

        if (ads.isEmpty())
//            holder.hideAd(true)
        else {
//            holder.hideAd(false)
            holder.bindView(pos)
        }

    }


    private fun positionLogic(position: Int): Int {
        return when {
            position == 0 -> JOK
            position % AD_POSITION == 0 -> ADVERTISE
//                position % (POSI_POSITION + ss + 1) == 0 -> POSITION
            else -> JOK
        }
    }

    private fun getJok(position: Int): Post {
        var pos = position
        val ad = position / AD_POSITION
        pos -= ad
        return joks[pos]
    }

    private fun getPositionerPos(position: Int): Int {
        val ad = position / (AD_POSITION + if (showGuide) 1 else 0)
        val posi = position / (POSI_POSITION + if (showGuide) 1 else 0)
        return position - ad - posi - if (showGuide) 1 else 0
    }

    private fun getItemsCount(): Int = joks.size + ads.size + if (showGuide) 1 else 0
    private fun getViewHolder(type: Int, container: ViewGroup): RecyclerView.ViewHolder {
        return when (type) {
            GUIDE -> DescriptionView(
                LayoutInflater.from(container.context).inflate(
                    R.layout.recycler_item_guide,
                    container,
                    false
                )
            )
            POSITION -> PositionerView(
                LayoutInflater.from(container.context).inflate(
                    R.layout.recycler_item_position,
                    container,
                    false
                )
            )
            ADVERTISE -> AdView(
                LayoutInflater.from(container.context).inflate(
                    R.layout.recycler_item_ad,
                    container,
                    false
                )
            )
            else -> JokView(
                LayoutInflater.from(container.context).inflate(
                    R.layout.recycler_item_jok,
                    container,
                    false
                )
            )
        }
    }

    private fun viewHolderHandler(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is JokView -> JokViewHandle(holder, getJok(position), position)
            is AdView -> AdViewHandle(holder, position)
            is PositionerView -> PositionerViewHandle(holder, getPositionerPos(position))
            is DescriptionView -> DescriptViewHandle(holder)
        }
    }

    // use this part for share content
    override fun LTRdrag(position: Int) {
        Timber.e("Now Right Released Item: $position")

        val jok = getJok(position)
        val content = HtmlCompat.fromHtml(jok.content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        Utils.shareText(content, context)
    }


    // use this part for add to favor
    @SuppressLint("CheckResult")
    override fun RTLdrag(position: Int) {
        Timber.e("Now Left Released Item: $position")

        val jok = getJok(position)
        jok.favorit = !jok.favorit
        dao.insertFavoritJok(jok).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({

            }, {
                it.printStackTrace()
                jok.favorit = !jok.favorit
                Toast.makeText(context, "عملیات با خطا مواجه شد", Toast.LENGTH_LONG).show()
            })
        listener?.dragged(jok.id,!jok.favorit)

    }

    override fun done(position: Int) {
        notifyItemChanged(position, position)
    }


}