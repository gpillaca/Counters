package com.gpillaca.counters.ui.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ItemExampleBinding
import kotlin.properties.Delegates

typealias OnClickMiscListener = (name: String) -> Unit

class MiscAdapter(private val listener: OnClickMiscListener) :
    RecyclerView.Adapter<MiscAdapter.MiscViewHolder>() {

    var miscs: List<String> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiscViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_example, parent, false)
        return MiscViewHolder(view)
    }

    override fun getItemCount(): Int = miscs.size

    override fun onBindViewHolder(holder: MiscViewHolder, position: Int) {
        val food = miscs[position]
        holder.bind(food, listener)
    }

    class MiscViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemExampleBinding.bind(view)

        fun bind(name: String, listener: OnClickMiscListener) {
            binding.chipName.text = name
            binding.chipName.setOnClickListener {
                listener(name)
            }
        }
    }
}