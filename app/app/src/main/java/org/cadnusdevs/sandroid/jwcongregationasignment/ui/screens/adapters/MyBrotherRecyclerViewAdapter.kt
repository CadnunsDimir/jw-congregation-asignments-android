package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.cadnusdevs.sandroid.jwcongregationasignment.databinding.FragmentItemBinding
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyBrotherRecyclerViewAdapter(
    private val values: List<Brother>
) : RecyclerView.Adapter<MyBrotherRecyclerViewAdapter.ViewHolder>() {

    private lateinit var onItemClickBehavior: (Brother) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = "${item.id}. ${item.name}: "
        holder.contentView.text = StringBuilder()
            .append(if(item.canBeUsher)  "Acomodador " else "")
            .append(if(item.canBeMicrophone)  "Micrófono " else "")
            .append(if(item.canBeComputer)  "Computadora " else "")
            .append(if(item.canBeSoundSystem)  "Sonido" else "")
            .toString()
            .replace(" ",", ")

        holder.contentView.setOnClickListener {
            this.onItemClickBehavior?.invoke(item)
        }

        holder.idView.setOnClickListener{
            this.onItemClickBehavior?.invoke(item)
        }
    }

    override fun getItemCount(): Int = values.size

    fun defineItemClickBehavior(event: (Brother)-> Unit) {
        this.onItemClickBehavior = event
    }

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}