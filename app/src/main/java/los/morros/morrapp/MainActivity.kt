package los.morros.morrapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import los.morros.morrapp.util.WrapContentLinearLayoutManager
import los.morros.morrapp.util.goToMenu
import los.morros.morrapp.util.realmInstance
import los.morros.morrapp.util.startNewActivity
import los.morros.morrapp.entities.User as MorroUser

val guestUser = MorroUser(
    "Invitado",
    "",
    "",
    "Para entrar haz click en iniciar sesión o edita este usuario para crear tu cuenta",
    true
)

var loggedUser: MorroUser = guestUser

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var profPic: ImageView
    lateinit var profName: TextView
    lateinit var adapter: PublicationViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.publications)

        realmInstance.addChangeListener {
            Handler(Looper.getMainLooper()).post {
                if (this::recyclerView.isInitialized && !recyclerView.isComputingLayout)
                    recyclerView.post(updateRV)
            }
        }

        val lmanager = WrapContentLinearLayoutManager(this)
        recyclerView.layoutManager = lmanager
        adapter = PublicationViewAdapter(this@MainActivity)
        recyclerView.adapter = adapter
        profName = findViewById(R.id.profile_name)

        profPic = findViewById<ImageView>(R.id.profile_pic).apply {
            setOnClickListener {
                startNewActivity(this@MainActivity, Profile::class.java)
            }

            loggedUser.image?.let(this::setImageBitmap)
        }

        findViewById<FloatingActionButton>(R.id.create_pub).apply {
            setOnClickListener {
                if (loggedUser != guestUser)
                    startNewActivity(this@MainActivity, CreatePublicationActivity::class.java)
                else Toast.makeText(
                    this@MainActivity,
                    "Inicia sesión para publicar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        findViewById<ImageView>(R.id.menu).apply {
            setOnClickListener {
                goToMenu(this@MainActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    private fun update() {
        profName.text = loggedUser.name.substringBefore(" ")
        loggedUser.image?.let(profPic::setImageBitmap)
        adapter.update()
    }
}