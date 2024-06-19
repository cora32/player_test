package io.iskopasi.player_test.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.palette.graphics.Palette
import io.iskopasi.player_test.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File


//private val Context.getStatusBarHeight: Int
//    get() {
//        val styledAttributes: TypedArray = theme.obtainStyledAttributes(
//            intArrayOf(R.attr.actionBarSize)
//        )
//        val actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
//        styledAttributes.recycle()
//
//        return actionBarHeight
//    }

private val View.getStatusBarHeight: Int
    get() {
        return ViewCompat.getRootWindowInsets(this)!!
            .getInsets(WindowInsetsCompat.Type.statusBars()).top
    }

object Utils {
//    fun blur(bitmap: Bitmap): Bitmap {
//        return Toolkit.blur(bitmap, 8)
//    }

    val String.d: Unit
        get() {
            Log.d("--> DEBUG:", this)
        }

    val String.e: Unit
        get() {
            Log.e("--> ERR:", this)
        }

    fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

    fun bg(block: suspend (CoroutineScope) -> Unit): Job = CoroutineScope(Dispatchers.IO).launch {
        block(this)
    }

    fun ui(block: suspend (CoroutineScope) -> Unit): Job = CoroutineScope(Dispatchers.Main).launch {
        block(this)
    }

    fun crop(
        view: View,
        x: Float, y: Float,
        width: Int, height: Int
    ): Bitmap {
        val statusBarHeight = view.getStatusBarHeight

        val bitmapSource = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmapSource)
        view.draw(canvas)

        return Bitmap.createBitmap(
            bitmapSource,
            x.toInt(), y.toInt() + statusBarHeight,
            width,
            height
        )
    }

//    fun getStatusBarHeight(): Int {
//        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
//        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
//        else Rect().apply { window.decorView.getWindowVisibleDisplayFrame(this) }.top
//    }
}


fun Int.getAccent(context: Context, block: (Int?) -> Unit) {
    val bt = BitmapFactory.decodeResource(
        context.resources,
        this
    )

    Palette.from(bt).generate { palette ->
        block(
            palette!!.getVibrantColor(
                ResourcesCompat.getColor(
                    context.resources,
                    io.iskopasi.player_test.R.color.silver, null
                )
            )
        )

    }
}

fun File.share(context: Context, subject: String, text: String) {
    val uri = FileProvider.getUriForFile(
        context, BuildConfig.APPLICATION_ID + ".provider", this
    )
    ContextCompat.startActivity(context.applicationContext, Intent(Intent.ACTION_SEND).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setType(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(this@share.extension)
        )
        putExtra(Intent.EXTRA_STREAM, uri)

        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }, null)
}