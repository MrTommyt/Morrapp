package los.morros.morrapp

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import los.morros.morrapp.entities.Publication
import los.morros.morrapp.entities.User
import los.morros.morrapp.util.startNewActivity

lateinit var updateRV: () -> Unit

val loadedPublications = mutableListOf<Publication>()

lateinit var lastClickedPublication: Publication

class PublicationViewAdapter(private val activity: Activity, private val user: User? = null) :
    RecyclerView.Adapter<PublicationViewAdapter.PublicationViewHolder>() {
    val contextPublications: List<Publication>
        get() = if (user != null) loadedPublications.filter { it.owner == user.id.toString() }
        else loadedPublications
    private val loadedPublicationHolders = mutableListOf<PublicationViewHolder>()

    companion object {
        init {
            val allPubs = realmInstance.where(Publication::class.java).findAll().toList()
            loadedPublications.addAll(allPubs)
        }
    }

    init {
        updateRV = this::update
    }

    class PublicationViewHolder(v: View, activity: Activity) : RecyclerView.ViewHolder(v) {
        private val image: ImageView = v.findViewById(R.id.pub_image)
        private val title: TextView = v.findViewById(R.id.pub_title)
        private val description: TextView = v.findViewById(R.id.pub_description)
        var publication: Publication? = null
            set(value) {
                field = value
                value ?: return
                value.image?.let { image.setImageBitmap(it) }
                title.text = value.title
                description.text = value.description
            }

        init {
            v.setOnClickListener {
                lastClickedPublication = publication ?: return@setOnClickListener
                startNewActivity(activity, PublicationActivity::class.java)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_scrolling, parent, false)
        return PublicationViewHolder(v, activity)
    }

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        holder.publication = contextPublications[position]
        loadedPublicationHolders.add(holder)
    }

    override fun getItemCount(): Int {
        update()
        return contextPublications.size
    }

    private fun update() {
        val mapped = loadedPublications.map { it.id }
        realmInstance.where(Publication::class.java)
            .apply { user?.let { contains("owner", it.id.toString()) } }.findAll().forEach {
                if (it.id !in mapped) {
                    loadedPublications.add(it)
                    Handler(Looper.getMainLooper()).post {
                        notifyItemInserted(loadedPublications.size - 1)
                    }
                }
            }
    }
}