package io.iskopasi.player_test.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.iskopasi.player_test.R

data class MediaData(
    val image: Int,
    val name: String,
    val subtitle: String,
    val duration: Int,
)

class PlayerModel(context: Application) : AndroidViewModel(context) {
    val test = arrayOf(
        MediaData(R.drawable.wat, "ШТО??", "Барашек ohiovaet", 12124214),
        MediaData(R.drawable.billy, "Реднек", "Беги, няша", 122143),
        MediaData(R.drawable.i2, "Literally me", "Face reveal", 3904),
        MediaData(R.drawable.i3, "OHUET", "anune ohuela", 109284),
        MediaData(R.drawable.i4, "Outer", "As shrimple as that", 2434325),
        MediaData(R.drawable.i5, "Жирокот", "Жирный толстый", 56757),
        MediaData(R.drawable.i6, "RAPE TIEM", "Nowhere to hide", 112131243),
        MediaData(R.drawable.i7, "Meph time xD", "Best time (and last one)", 53446),
        MediaData(R.drawable.i8, "Daily thoughts", "Being useful every day", 5098567),
        MediaData(R.drawable.i9, "Science pepe", "Memetic warfare", 98234),
        MediaData(R.drawable.i10, "Anger and Wraith", "Not even mad", 9380468),
        MediaData(R.drawable.none, "No track", "No image", 12312),
    )
    var currentData = MutableLiveData(test.first())
    var isPlaying = MutableLiveData(false)
    var isShuffling = MutableLiveData(false)
    var isRepeating = MutableLiveData(false)
    var currentProgress = MutableLiveData(0)

    fun next() {
        currentData.value = test[test.indices.random()]
        currentProgress.value = (0..currentData.value!!.duration).random()
    }

    fun setSeekPosition(progress: Int) {
        currentProgress.value = progress
    }

    fun start() {
        isPlaying.value = true
    }

    fun pause() {
        isPlaying.value = false
    }

    fun shuffle() {
        isShuffling.value = !isShuffling.value!!
    }

    fun repeat() {
        isRepeating.value = !isRepeating.value!!
    }
}