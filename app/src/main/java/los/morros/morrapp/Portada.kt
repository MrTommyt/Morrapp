package los.morros.morrapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import los.morros.morrapp.util.startNewActivity

class Portada : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        los.morros.morrapp.util.init(this)
        setContentView(R.layout.activity_portada)

        findViewById<Button>(R.id.login).apply {
            setOnClickListener {
                startNewActivity(this@Portada, LoginActivity::class.java)
            }
        }

        findViewById<Button>(R.id.guest).apply {
            setOnClickListener {
                loggedUser = guestUser
                startNewActivity(this@Portada, MainActivity::class.java)
            }
        }
    }
}