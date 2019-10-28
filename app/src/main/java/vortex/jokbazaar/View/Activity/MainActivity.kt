package vortex.jokbazaar.view.activity

import android.os.Bundle
import vortex.jokbazaar.core.xpack.XappCompatActivity
import vortex.jokbazaar.R
import vortex.jokbazaar.viewModel.MainActivityViewModel

class MainActivity : XappCompatActivity<MainActivityViewModel>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
