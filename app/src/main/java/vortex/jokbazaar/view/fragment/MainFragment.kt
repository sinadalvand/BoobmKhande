package vortex.jokbazaar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vortex.jokbazaar.core.xpack.Xfragment
import vortex.jokbazaar.R
import vortex.jokbazaar.viewModel.MainFragmentViewModel

class MainFragment : Xfragment<MainFragmentViewModel>() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}