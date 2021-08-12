package los.morros.morrapp.entities

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import los.morros.morrapp.R
import los.morros.morrapp.loggedUser
import los.morros.morrapp.util.deserializeBitmap
import los.morros.morrapp.util.realmInstance
import los.morros.morrapp.util.serializeBitmap
import java.util.*

open class User(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var description: String = "",
    @Transient
    val isGuest: Boolean = false,
    @Transient
    var _image: Bitmap? = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.blank_avatar),
) : RealmObject() {
    constructor() : this(_image = null)

    @PrimaryKey
    var id: UUID = UUID.randomUUID()
    private lateinit var bitmapArray: ByteArray

    val publications: List<Publication>
        get() = realm.where(Publication::class.java).contains("owner", id.toString()).findAll()

    var image
        get() = _image ?: if (this::bitmapArray.isInitialized) deserializeBitmap(bitmapArray)
        else null
        set(value) {
            _image = value
            if (value != null)
                bitmapArray = serializeBitmap(value)
        }

    init {
        _image?.let {
            bitmapArray = serializeBitmap(it)
        } ?: run {
            if (this::bitmapArray.isInitialized)
                _image = deserializeBitmap(bitmapArray)
        }
    }

    fun save() {
        if (isGuest) {
            loggedUser = User(
                name, email, password, description, true, image
            )
        }

        realmInstance.executeTransaction {
            it.insertOrUpdate(this)
        }
    }
}