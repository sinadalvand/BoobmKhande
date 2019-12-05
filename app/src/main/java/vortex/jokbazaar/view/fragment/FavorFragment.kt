package vortex.jokbazaar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favor.*
import vortex.jokbazaar.R
import vortex.jokbazaar.core.DragUtils.HandsomeDragListener
import vortex.jokbazaar.core.adapter.MainRecyclerAdapter
import vortex.jokbazaar.core.database.HandsomeDatabse
import vortex.jokbazaar.core.listener.FavorListener
import vortex.jokbazaar.core.utils.invisible
import vortex.jokbazaar.core.utils.visible
import vortex.jokbazaar.core.xpack.Xfragment
import vortex.jokbazaar.view.activity.MainActivity
import vortex.jokbazaar.viewModel.FavorFragmentViewModel
import javax.inject.Inject

class FavorFragment : Xfragment<FavorFragmentViewModel>(), FavorListener {

    lateinit var favorAdapter: MainRecyclerAdapter


    @Inject
    lateinit var database: HandsomeDatabse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fragment_favor_recycler.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        favorAdapter = MainRecyclerAdapter(context!!, reactor, database.getMainDAO(),this)
        fragment_favor_recycler.adapter = favorAdapter
        setDragger()

        vm.favorList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                fragment_favor_text_empty.visible()
                favorAdapter.addPost(arrayOf())
            } else {
                fragment_favor_text_empty.invisible()
                favorAdapter.addPost(it)
            }
        })




        vm.getFavoritList()
    }


    private fun setDragger() {
        val callback = HandsomeDragListener(favorAdapter)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(fragment_favor_recycler)
    }

    override fun dragged(id: String,delete:Boolean) {
        (activity as MainActivity).vm.deleteFavor(id,delete)
    }


}