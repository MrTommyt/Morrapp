package los.morros.morrapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import los.morros.morrapp.util.goToMenu
import los.morros.morrapp.util.startNewActivity

class Profile : AppCompatActivity() {
    lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<ImageView>(R.id.menu).apply {
            setOnClickListener {
                goToMenu(this@Profile)
            }
        }

        image = findViewById(R.id.profile_pic)

        update()
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        update()
    }

    private fun update() {
        loggedUser.image?.let(image::setImageBitmap)

        findViewById<TextView>(R.id.profile_name).apply {
            text = loggedUser.name
        }

        findViewById<TextView>(R.id.profile_text).apply {
            text = loggedUser.description
        }

        findViewById<RecyclerView>(R.id.publications).apply {
            layoutManager = LinearLayoutManager(this@Profile)
            adapter = PublicationViewAdapter(this@Profile, loggedUser)
        }

        findViewById<ImageView>(R.id.profile_edit).apply {
            setOnClickListener {
                startNewActivity(this@Profile, EditProfile::class.java)
            }
        }
    }
}