package vortex.jokbazaar.Core.Xpack

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProviders
import vortex.jokbazaar.Core.Security.Offline.Reactor
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.android.support.AndroidSupportInjection
import vortex.jokbazaar.R
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class XsheetFragment<T: XviewModel> : AppCompatDialogFragment() {

    @Inject
    lateinit var vmFactory: ViewModelFactory

    @Inject
    lateinit var reactor: Reactor

    lateinit var vm:T

    var consume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        vm = ViewModelProviders.of(this, vmFactory)[getGenericEasy()]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        consume = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(this.context!!, R.style.BottomSheetDialog)
    }



    /**
     * This function use for bind ViewModel to this Fragment
     * @return Class<T>
     */
    //TODO Should remove this from everywhere that use and then at last remove it !
    open fun setViewModel():Class<T>{
        return getGenericEasy()
    }


    /**
     * unfortunately after write "abstract fun setViewModel()" i realize ,
     * can get generic type whit out this func , so fuck that
     * lets use this fucking shit
     * @return Class<T>
     */
    private fun getGenericEasy():Class<T>{
        val ops = javaClass.genericSuperclass
        val tType = (ops as ParameterizedType).actualTypeArguments[0]
        val className = tType.toString().split(" ")[1]
        return  Class.forName(className) as Class<T>
    }


    /**
     * this function should override in fragment that
     * need get parameter from every where before onCreate()
     */
    open fun getArgs(){

    }
}