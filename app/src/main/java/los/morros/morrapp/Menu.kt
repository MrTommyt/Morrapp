package los.morros.morrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import los.morros.morrapp.util.startNewActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViewById<TextView>(R.id.text_explore).apply {
            setOnClickListener {
                startNewActivity(this@Menu, MainActivity::class.java)
            }
        }

        findViewById<TextView>(R.id.profile).apply {
            setOnClickListener {
                startNewActivity(this@Menu, Profile::class.java)
            }
        }

        findViewById<TextView>(R.id.como_llegar).apply {
            setOnClickListener {
                startNewActivity(this@Menu, ComoLlegar::class.java)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startNewActivity(this, MainActivity::class.java)
    }
}