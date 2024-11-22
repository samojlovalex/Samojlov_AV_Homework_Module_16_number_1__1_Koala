package com.example.samojlov_av_homework_module_16_number_1__1_koala.models

import com.example.samojlov_av_homework_module_16_number_1__1_koala.R

data class VideoInternal(val description: String, val video: Int) {

    companion object {
        val videoInternalList = mutableListOf(
            VideoInternal("Хорошо живет на свете Винни-Пух...", R.raw.winnie_the_pooh_horosho_zhivet),
            VideoInternal(
                "Усли б мишки были пчёлами...",
                R.raw.winnie_the_pooh_esli_by
            ),
            VideoInternal(
                "Как хорошо, как хорошо...",
                R.raw.winnie_the_pooh_kak_horosho
            )
        )
    }

}