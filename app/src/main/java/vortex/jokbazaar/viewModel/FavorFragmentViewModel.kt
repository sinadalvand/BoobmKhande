package vortex.jokbazaar.viewModel

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber
import vortex.jokbazaar.core.xpack.XviewModel
import vortex.jokbazaar.model.Post
import vortex.jokbazaar.protocol.Repository
import javax.inject.Inject

class FavorFragmentViewModel @Inject constructor() : XviewModel() {

    val favorList = MutableLiveData<Array<Post>>()

    @Inject
    lateinit var repository: Repository

    fun getFavoritList() {
        CoroutineScope(IO).launch {
            favorList.postValue(repository.getFavor())
        }
    }

}