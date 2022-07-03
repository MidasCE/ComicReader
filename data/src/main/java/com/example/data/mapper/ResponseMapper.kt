package com.example.data.mapper

import com.example.data.entity.*
import model.*

fun ComicResponse.toDomain(): Comic {
    return Comic(
        num = num,
        link = link,
        year = year,
        news = news,
        safeTitle = safeTitle,
        transcript = transcript,
        alt = alt,
        img = img,
        title = title,
        day = day,
    )
}
