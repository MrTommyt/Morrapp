package los.morros.morrapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import los.morros.morrapp.Menu
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.reflect.KProperty

val imageContract = object : ActivityResultContract<Int, Uri?>() {
    override fun createIntent(context: Context, input: Int?): Intent = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent?.data
    }
}

fun <T> startNewActivity(activity: Activity, cls: Class<T>) {
    val intent = Intent(activity.applicationContext, cls)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    activity.startActivity(intent)
}

fun goToMenu(activity: Activity) {
    startNewActivity(activity, Menu::class.java)
}

fun serializeBitmap(bm: Bitmap): ByteArray {
    return ByteArrayOutputStream().apply {
        bm.compress(Bitmap.CompressFormat.PNG, 100, this)
    }.toByteArray()
}

fun deserializeBitmap(byteArray: ByteArray): Bitmap {
    val bais = ByteArrayInputStream(byteArray)
    return BitmapFactory.decodeStream(bais)
}

class BitmapSerializerDelegator<I>(var byteArray: ByteArray) {
    lateinit var image: Bitmap
    operator fun getValue(self: I, property: KProperty<*>): Bitmap = if (this::image.isInitialized)
        image
    else deserializeBitmap(byteArray).also { image = it }

    operator fun setValue(self: I, property: KProperty<*>, value: Bitmap) {
        image = value
        byteArray = serializeBitmap(value)
    }
}
