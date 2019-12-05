package vortex.jokbazaar.core.xpack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import vortex.jokbazaar.core.security.offline.Reactor
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.view.activity.MainActivity
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
abstract class XbaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}