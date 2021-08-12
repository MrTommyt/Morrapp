package los.morros.morrapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import los.morros.morrapp.entities.User
import los.morros.morrapp.util.imageContract
import los.morros.morrapp.util.realmInstance
import los.morros.morrapp.util.startNewActivity

class EditProfile : AppCompatActivity() {
    lateinit var nameEdit: EditText
    lateinit var descriptionEdit: EditText
    lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        image = findViewById(R.id.profile_pic)
        image.setImageBitmap(loggedUser.image)

        val registered = registerForActivityResult(imageContract) {
            val stream = contentResolver.openInputStream(it ?: return@registerForActivityResult)
            val bm = BitmapFactory.decodeStream(stream)
            image.setImageBitmap(bm)
            realmInstance.executeTransaction {
                loggedUser.image = bm
            }
        }

        image.setOnClickListener {
            registered.launch(1)
        }

        nameEdit = findViewById(R.id.profile_name)
        nameEdit.hint = loggedUser.name
        descriptionEdit = findViewById(R.id.profile_description)
        descriptionEdit.hint = loggedUser.description
        emailEdit = findViewById(R.id.email)
        emailEdit.hint = loggedUser.email
        passwordEdit = findViewById(R.id.password)
        passwordEdit.hint = loggedUser.password

        findViewById<FloatingActionButton>(R.id.finalize_edit).apply {
            setOnClickListener {
                loggedUser.apply {
                    if (nameEdit.text.isNotEmpty())
                        name = nameEdit.text.toString()

                    if (descriptionEdit.text.isNotEmpty())
                        description = descriptionEdit.text.toString()

                    if (emailEdit.text.isNotEmpty())
                        email = emailEdit.text.toString()

                    if (passwordEdit.text.isNotEmpty())
                        password = passwordEdit.text.toString()

                    realmInstance.executeTransaction {
                        image = loggedUser.image
                    }

                    save()
                }

                onBackPressed()
            }
        }
    }
}