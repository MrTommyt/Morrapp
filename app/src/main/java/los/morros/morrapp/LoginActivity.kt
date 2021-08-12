package los.morros.morrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import los.morros.morrapp.entities.User
import los.morros.morrapp.util.realmInstance
import los.morros.morrapp.util.startNewActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    lateinit var passField: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.editTextEmail)
        passField = findViewById(R.id.editTextPassword)

        findViewById<Button>(R.id.login_button).apply {
            setOnClickListener {
                val email = emailField.text.toString()
                val pass = passField.text.toString()
                loggedUser = realmInstance.where(User::class.java)
                    .equalTo("email", email)
                    .equalTo("password", pass)
                    .findFirst() ?: return@setOnClickListener run {
                    Toast.makeText(
                        this@LoginActivity,
                        "Usuario no encontrado, ingresa como invitado si no tienes una cuenta",
                        Toast.LENGTH_LONG
                    ).show()
                }

                startNewActivity(this@LoginActivity, MainActivity::class.java)
            }
        }
    }
}