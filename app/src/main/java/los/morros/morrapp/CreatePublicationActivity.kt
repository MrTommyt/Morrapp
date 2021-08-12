package los.morros.morrapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import los.morros.morrapp.entities.Publication
import los.morros.morrapp.util.goToMenu
import los.morros.morrapp.util.imageContract
import los.morros.morrapp.util.realmInstance

class CreatePublicationActivity : AppCompatActivity() {
    lateinit var image: ImageView
    lateinit var title: EditText
    lateinit var description: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_publication)

        image = findViewById(R.id.edit_pub_image)
        title = findViewById(R.id.edit_pub_name)
        description = findViewById(R.id.edit_pub_description)

        val imageActivityResult =
            registerForActivityResult(imageContract) {
                val stream = contentResolver.openInputStream(it ?: return@registerForActivityResult)
                val bm = BitmapFactory.decodeStream(stream)
                image.setImageBitmap(bm)
            }

        findViewById<FloatingActionButton>(R.id.add_pic_button).apply {
            setOnClickListener {
                imageActivityResult.launch(1)
            }
        }

        findViewById<FloatingActionButton>(R.id.finalize_pub).apply {
            setOnClickListener {
                realmInstance.executeTransaction {
                    it.insert(
                        Publication(
                            _image = image.drawToBitmap(),
                            title = title.text.toString(),
                            description = description.text.toString(),
                            owner = loggedUser.id.toString()
                        )
                    )
                }

                Toast.makeText(
                    this@CreatePublicationActivity,
                    "Publication created successfully",
                    Toast.LENGTH_LONG
                ).show()

                clear()
                onBackPressed()
            }
        }

        findViewById<ImageView>(R.id.menu).apply {
            setOnClickListener {
                goToMenu(this@CreatePublicationActivity)
                clear()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear()
    }

    private fun clear() {
        image.setImageDrawable(null)
        title.text.clear()
        description.text.clear()
    }
}