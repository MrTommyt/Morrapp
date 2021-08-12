package los.morros.morrapp.entities

import android.graphics.Bitmap
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import los.morros.morrapp.util.deserializeBitmap
import los.morros.morrapp.util.serializeBitmap
import java.util.*

open class Publication(
    @Transient
    var _image: Bitmap? = null,
    var title: String = "",
    var description: String = "",
    var owner: String = UUID.randomUUID().toString(),
) : RealmObject() {
    @PrimaryKey
    var id: UUID = UUID.randomUUID()
    private lateinit var bitmapArray: ByteArray

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
}
