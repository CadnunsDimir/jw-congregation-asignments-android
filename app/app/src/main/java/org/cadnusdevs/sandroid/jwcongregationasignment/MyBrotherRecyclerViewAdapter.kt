package org.cadnusdevs.sandroid.jwcongregationasignment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import org.cadnusdevs.sandroid.jwcongregationasignment.placeholder.PlaceholderContent.PlaceholderItem
import org.cadnusdevs.sandroid.jwcongregationasignment.databinding.FragmentItemBinding
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyBrotherRecyclerViewAdapter(
    private val values: List<Brother>
) : RecyclerView.Adapter<MyBrotherRecyclerViewAdapter.ViewHolder>() {

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
        holder.idView.text = "${item.name}: "
        holder.contentView.text = StringBuilder()
            .append(if(item.canBeUsher)  "Indicador " else "")
            .append(if(item.canBeMicrophone)  "Microfone " else "")
            .append(if(item.canBeComputer)  "PC " else "")
            .append(if(item.canBeSoundSystem)  "Som " else "")
            .toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}