package los.morros.morrapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import los.morros.morrapp.util.goToMenu
import los.morros.morrapp.util.startNewActivity

class PublicationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication)

        val publication = lastClickedPublication
        findViewById<ImageView>(R.id.profile_pic).apply {
            setOnClickListener {
                startNewActivity(this@PublicationActivity, Profile::class.java)
            }
        }

        findViewById<ImageView>(R.id.pub_image).apply {
            setImageBitmap(publication.image)
        }

        findViewById<TextView>(R.id.pub_title).apply {
            text = publication.title
        }

        findViewById<TextView>(R.id.pub_description).apply {
            text = publication.description
        }

        findViewById<ImageView>(R.id.menu).apply {
            setOnClickListener {
                goToMenu(this@PublicationActivity)
            }
        }

        findViewById<ImageView>(R.id.profile_pic).apply {
            setOnClickListener {
                startNewActivity(this@PublicationActivity, Profile::class.java)
            }
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
    }
}