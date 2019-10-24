package co.rosemovie.app.Core.Xpack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import co.rosemovie.app.Core.Security.Offline.Reactor
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

/**
 * abstract class for make easier use Fragment in MVVM arch
 * 1- auto binding ViewModel
 * 2- inject refactor for public use
 * 3- get args in #getArgs() func , just override this
 *
 * god bless
 *
 * @param T:XviewModel
 * @property vmFactory ViewModelFactory
 * @property reactor Reactor
 * @property vm T
 * @property consume Boolean
 */
abstract class Xfragment<T : XviewModel> : Fragment() {


    @Inject
    lateinit var vmFactory: ViewModelFactory


    @Inject
    lateinit var reactor: Reactor


    lateinit var vm: T

    var consume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        val time = System.currentTimeMillis()
        vm = ViewModelProviders.of(this, vmFactory)[getGenericEasy()]
        val passTime = System.currentTimeMillis()
        Timber.e("Time: ${passTime-time}")
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        consume = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    /**
     * This function use for bind ViewModel to this Fragment
     * @return Class<T>
     */
    //TODO Should remove this from everywhere that use and then at last remove it !
    open fun setViewModel(): Class<T> {
        return getGenericEasy()
    }


    /**
     * unfortunately after write "abstract fun setViewModel()" i realize ,
     * can get generic type whit out this func , so fuck that
     * lets use this fucking shit
     * @return Class<T>
     */
    private fun getGenericEasy(): Class<T> {
        val ops = javaClass.genericSuperclass
        val tType = (ops as ParameterizedType).actualTypeArguments[0]
        val className = tType.toString().split(" ")[1]
        return Class.forName(className) as Class<T>
    }


    /**
     * this function should override in fragment that
     * need get parameter from every where before onCreate()
     */
    open fun getArgs() {

    }


}