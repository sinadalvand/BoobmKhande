package vortex.jokbazaar.view.activity

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.mobeedev.library.SlidingMenuBuilder
import com.mobeedev.library.SlidingNavigation
import com.mobeedev.library.gravity.SlideGravity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber
import vortex.jokbazaar.core.xpack.XappCompatActivity
import vortex.jokbazaar.R
import vortex.jokbazaar.core.database.HandsomeDatabse
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.model.Post
import vortex.jokbazaar.viewModel.MainActivityViewModel
import javax.inject.Inject

class MainActivity : XappCompatActivity<MainActivityViewModel>(), View.OnClickListener,
    NavController.OnDestinationChangedListener {

    @Inject
    lateinit var database:HandsomeDatabse


    override fun onClick(v: View?) {
        when (v) {
            menu_exit_layout -> finish()
            menu_home_layout -> {

            }
            menu_favor_layout -> getControler().navigate(R.id.favorFragment)
            menu_setting_layout -> getControler().navigate(R.id.settingsFragment)
            menu_about_layout -> getControler().navigate(R.id.aboutFragment)
        }
        menu.closeMenu(true)
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.mainFragment -> bar.displayHomeAsUpEnabled(false, null)
            R.id.favorFragment -> bar.displayHomeAsUpEnabled(true, getString(R.string.favor))
            R.id.settingsFragment -> bar.displayHomeAsUpEnabled(true, getString(R.string.settings))
            R.id.aboutFragment -> bar.displayHomeAsUpEnabled(true, getString(R.string.about))
        }
    }

    lateinit var menu: SlidingNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeNow()
        setContentView(R.layout.activity_main)

        getControler().addOnDestinationChangedListener(this)


        menu = SlidingMenuBuilder(this)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.activity_menu)
            .withGravity(SlideGravity.RIGHT)
            .inject()






        menu_home_layout.setOnClickListener(this)
        menu_favor_layout.setOnClickListener(this)
        menu_setting_layout.setOnClickListener(this)
        menu_about_layout.setOnClickListener(this)
        menu_exit_layout.setOnClickListener(this)






        bar.setIcon(R.drawable.logo)
        bar.layoutDirection = View.LAYOUT_DIRECTION_RTL
        bar.setOnMenuClickedListener {
            if (getControler().currentDestination?.id == R.id.mainFragment)
                menu.openMenu(true)
            else
                getControler().popBackStack()
        }



        menu_container.layoutDirection = View.LAYOUT_DIRECTION_LTR






    }

    private fun setThemeNow() {
        if (reactor.get(Const.THEME, 0) == 0) {
            setTheme(R.style.Dark)
        } else {
            setTheme(R.style.Light)
        }

    }


    private fun getControler(): NavController = findNavController(R.id.main_nav_host_fragment)


//    private fun dataGenerator() {
//        val joks = arrayListOf<Post>()
//        var jok = Post()
//        for (i in 0..900) {
//            jok = Post()
//            jok.id = i
//            jok.content =
//                "جوک $i \n برای دریافت محتوای بیشتر صفحه را پایین بکشید و تنظیمات را باز کرده و بر روی اعمال تغییرات کلیک کنی این یک متن تست است ک فاقد ارزش محتوایی می باشد. "
//            if (i % 3 == 0)
//                jok.img = "http://www.jazzaab.net/upload/2/0.607419001320939172_jazzaab_ir.jpg"
//            if (i % 7 == 0)
//                jok.favorit = true
//            joks.add(jok)
//        }
//
//        Timber.e("Database Insert Start")
//        val data = database.getMainDAO().insertJoks(joks.toTypedArray()).subscribe({
//            Timber.e("Entity added to : $it")
//        }, {
//
//        })
//        Timber.e("Database Insert Done !")
//    }


}
