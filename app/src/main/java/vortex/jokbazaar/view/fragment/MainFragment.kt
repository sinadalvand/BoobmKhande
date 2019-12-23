package vortex.jokbazaar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.schedulers.Schedulers
import ir.tapsell.sdk.AdRequestCallback
import ir.tapsell.sdk.nativeads.TapsellNativeBannerManager
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import vortex.jokbazaar.BuildConfig
import vortex.jokbazaar.R
import vortex.jokbazaar.core.DragUtils.HandsomeDragListener
import vortex.jokbazaar.core.adapter.MainRecyclerAdapter
import vortex.jokbazaar.core.database.HandsomeDatabse
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.core.xpack.EndlessRecyclerListener
import vortex.jokbazaar.core.xpack.Xfragment
import vortex.jokbazaar.view.activity.MainActivity
import vortex.jokbazaar.viewModel.MainActivityViewModel
import vortex.jokbazaar.viewModel.MainFragmentViewModel
import javax.inject.Inject


class MainFragment : Xfragment<MainFragmentViewModel>() {


    lateinit var mainAdapter: MainRecyclerAdapter

    @Inject
    lateinit var databse: HandsomeDatabse


    lateinit var vms: MainActivityViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vms = (activity as MainActivity).vm
        mainAdapter = MainRecyclerAdapter(context!!, reactor, databse.getMainDAO())
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_fragment_main.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        recycler_fragment_main.adapter = mainAdapter

        vms.getJoks(1, "")

        recycler_fragment_main.addOnScrollListener(object :
            EndlessRecyclerListener<LinearLayoutManager>(
                recycler_fragment_main.layoutManager as LinearLayoutManager,
                Const.Jok_PAGE
            ) {
            override fun onLoadPage(page: Int) {
                vms.getJoks(page, vms.joks.value?.last()?.id ?: "")
                getAd()
            }
        })

        recycler_fragment_main.addOnScrollListener(object :
            EndlessRecyclerListener<LinearLayoutManager>(
                recycler_fragment_main.layoutManager as LinearLayoutManager,
                20
            ) {
            override fun onLoadPage(page: Int) {
                getAd()
            }
        })


        vms.joks.observe(viewLifecycleOwner, Observer {
            mainAdapter.addPost(it.toTypedArray())
        })


        vms.ads.observe(viewLifecycleOwner, Observer {
            mainAdapter.addAd(it.toTypedArray())
        })


        vms.error.observe(viewLifecycleOwner, Observer {
            if (it && !vms.retry.hasActiveObservers())
                ErrorSheet.showInfo(childFragmentManager)
        })


        val callback = HandsomeDragListener(mainAdapter)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recycler_fragment_main)

        getAd()


    }

    private fun getAd() {
        TapsellNativeBannerManager.getAd(
            context, BuildConfig.TAPSELL_NATIVE_BANNER, object : AdRequestCallback {
                override fun onResponse(adId: Array<String>) {
                    val ads = vms.ads.value ?: arrayListOf()
                    ads.addAll(adId)
                    vms.ads.postValue(ads)
                }

                override fun onFailed(message: String) {
                }
            })
    }

}