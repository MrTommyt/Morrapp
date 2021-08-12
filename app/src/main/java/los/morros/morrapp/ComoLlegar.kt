package los.morros.morrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import los.morros.morrapp.util.goToMenu

data class Contact(
    val name: String,
    val description: String,
    val number: Long,
    val email: String
)

val contacts = listOf(
    Contact(
        "Jaime Altozano",
        "Compositor de música afectado por la pandemia",
        3048919201,
        "jaime_altozano@gmail.com"
    ),
    Contact(
        "Jhon Doe",
        "Vendedor ambulante",
        3055457898,
        "vendedor_ambulante@hotmail.com"
    ),
    Contact(
        "Camilo Benavides",
        "Estudiante de la universidad del magdalena",
        3015715706,
        "benavidescamilo199@gmail.com"
    ),
    Contact(
        "Jane Doe",
        "Lanchera de la región",
        3044048798,
        "janedoe123@gmail.com"
    )
)

class ComoLlegarAdapter : RecyclerView.Adapter<ComoLlegarAdapter.ComoLlegarHolder>() {
    class ComoLlegarHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.contact_name)
        val description: TextView = v.findViewById(R.id.contact_description)
        val number: TextView = v.findViewById(R.id.contact_number)
        val email: TextView = v.findViewById(R.id.contact_email)

        var contact: Contact? = null
            set(value) {
                field = value
                value ?: return
                name.text = value.name
                description.text = value.description
                number.text = value.number.toString()
                email.text = value.email
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComoLlegarHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contactos, parent, false)
        return ComoLlegarHolder(v)
    }

    override fun onBindViewHolder(holder: ComoLlegarHolder, position: Int) {
        holder.contact = contacts[position]
    }

    override fun getItemCount(): Int = contacts.size
}

class ComoLlegar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_como_llegar)

        findViewById<ImageView>(R.id.menu).apply {
            setOnClickListener {
                goToMenu(this@ComoLlegar)
            }
        }

        findViewById<RecyclerView>(R.id.contact_list).apply {
            layoutManager = LinearLayoutManager(this@ComoLlegar)
            adapter = ComoLlegarAdapter()
        }
    }
}