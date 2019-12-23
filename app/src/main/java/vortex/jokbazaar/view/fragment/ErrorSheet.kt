package vortex.jokbazaar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_error_sheet.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import vortex.jokbazaar.R
import vortex.jokbazaar.core.xpack.XsheetFragment
import vortex.jokbazaar.view.activity.MainActivity
import vortex.jokbazaar.viewModel.MainActivityViewModel
import vortex.jokbazaar.viewModel.MainFragmentViewModel

class ErrorSheet : XsheetFragment<MainFragmentViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        isCancelable = (activity as MainActivity).vm.joks.value?.isNotEmpty() ?: false

        return inflater.inflate(R.layout.fragment_error_sheet, container, false)
    }

    private var firstError = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm = (activity as MainActivity).vm

        Circular_fragment_error_button.setOnClickListener {
            Circular_fragment_error_button.startMorphAnimation()
            vm.getJoks(vm.page, "")
        }


        vm.error.observe(viewLifecycleOwner, Observer {
            Timber.e(this.toString()+"is Still Error: "+it)
            isCancelable = vm.joks.value?.isNotEmpty() ?: false
            if (it) {
                if (!firstError)
                    vm.retry.postValue(true)
                firstError = false
            } else {
                isCancelable = true
                dismiss()
            }
        })


        vm.retry.observe(viewLifecycleOwner, Observer {
            Circular_fragment_error_button.startMorphRevertAnimation()
        })

    }


    companion object {
        fun showInfo(fm: FragmentManager) {
            val frags = ErrorSheet()
            frags.show(fm, "errorSheet")
        }
    }
}