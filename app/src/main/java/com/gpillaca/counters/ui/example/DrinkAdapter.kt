package com.gpillaca.counters.ui.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ItemExampleBinding
import kotlin.properties.Delegates

typealias OnClickDrinkListener = (name: String) -> Unit

class DrinkAdapter(private val listener: OnClickDrinkListener) :
    RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>() {

    var drinks: List<String> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_example, parent, false)
        return DrinkViewHolder(view)
    }

    override fun getItemCount(): Int = drinks.size

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val food = drinks[position]
        holder.bind(food, listener)
    }

    class DrinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemExampleBinding.bind(view)

        fun bind(name: String, listener: OnClickDrinkListener) {
            binding.chipName.text = name
            binding.chipName.setOnClickListener {
                listener(name)
            }
        }
    }
}