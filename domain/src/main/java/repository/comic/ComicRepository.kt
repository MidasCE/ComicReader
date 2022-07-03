package repository.comic

import io.reactivex.Single
import model.Comic

interface ComicRepository {

    fun getCurrentComic(): Single<Comic>

    fun getComic(id: Int): Single<Comic>
}
