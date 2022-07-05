package repository.comic

import io.reactivex.Single
import model.Comic

interface ComicRepository {

    fun getLatestComic(): Single<Comic>

    fun getComic(id: Int): Single<Comic>
}
