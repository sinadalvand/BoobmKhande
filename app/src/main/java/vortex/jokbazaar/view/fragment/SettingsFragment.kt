package vortex.jokbazaar.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*
import vortex.jokbazaar.R
import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.core.xpack.Xfragment
import vortex.jokbazaar.viewModel.FavorFragmentViewModel

class SettingsFragment : Xfragment<FavorFragmentViewModel>(), CompoundButton.OnCheckedChangeListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fragment_setting_toggle_darkMode.isChecked = getTheme() == 0
        fragment_setting_toggle_linkShare.isChecked = getLinkShare() == true


        fragment_setting_toggle_darkMode.setOnCheckedChangeListener(this)
        fragment_setting_toggle_linkShare.setOnCheckedChangeListener(this)
    }


    private fun getTheme(): Int = reactor.get(Const.THEME, 0)

    private fun getLinkShare(): Boolean = reactor.get(Const.SHARE_LINK, false)

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView) {
            fragment_setting_toggle_darkMode -> {
                reactor.put(Const.THEME, if (isChecked) 0 else 1)
                Toast.makeText(context!!,getString(R.string.rerun),Toast.LENGTH_LONG).show()
            }


            fragment_setting_toggle_linkShare -> {
                reactor.put(Const.SHARE_LINK, isChecked)
            }
        }
    }

}