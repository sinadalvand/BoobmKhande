package vortex.jokbazaar.test

import vortex.jokbazaar.core.utils.Const
import vortex.jokbazaar.model.Post
import vortex.jokbazaar.model.Pagination

object FakeGenerator {

    fun getFakeJoc(page: Int): Pagination<Post> {
        val pager = Pagination<Post>()
        pager.currentPage = page
        for (i in ((page - 1) * 100) until ((Const.Jok_PAGE) * page)) {
            val jok = Post()
            jok.id = "$i"
            jok.content = "متن تست جوک $i ک باید طولانی باشه و تا جای ممکن فضای اپ رو پر کنه. "
            pager.data.add(jok)
        }
        return pager
    }
}