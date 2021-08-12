package los.morros.morrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import los.morros.morrapp.util.startNewActivity

private lateinit var user: User
private lateinit var realmConfig: RealmConfiguration
lateinit var app: App

lateinit var realmInstance: Realm

private var isInit = false

class Portada : AppCompatActivity() {
    private fun init() {
        if (isInit)
            return

        Realm.init(applicationContext)

        app = App(
            AppConfiguration.Builder("morrapp-vpzsp")
                .build()
        )

        app.loginAsync(Credentials.apiKey("n0lm2JzVqZygn4K0gN6R1PSN0HQzta7IW7yCDdmQYnoh66jk8IlSnNLAWQfNa0fo")) {
            user = it.get()
        }

        realmConfig = RealmConfiguration.Builder()
            .name("morrapp")
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()

        realmInstance = Realm.getInstance(realmConfig)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
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