package los.morros.morrapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
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

private lateinit var user: User
private lateinit var realmConfig: RealmConfiguration
lateinit var app: App

private var realmI: Realm? = null
val realmInstance: Realm
    get() = realmI ?: Realm.getDefaultInstance()

private var isInit = false

fun init(context: Context) {
    if (isInit)
        return

    Realm.init(context)

    app = App(
        AppConfiguration.Builder("morrapp-vpzsp")
            .build()
    )

    app.loginAsync(Credentials.apiKey("n0lm2JzVqZygn4K0gN6R1PSN0HQzta7IW7yCDdmQYnoh66jk8IlSnNLAWQfNa0fo")) {
        user = it.get()
    }

    realmConfig = RealmConfiguration.Builder()
        .name("morrapp1")
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()

    realmI = Realm.getInstance(realmConfig)
    isInit = true
}

class WrapContentLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("TAG", "IOOBE in RecyclerView")
        }
    }
}
