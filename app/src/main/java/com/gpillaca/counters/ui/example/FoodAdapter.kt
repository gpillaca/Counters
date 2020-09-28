package com.gpillaca.counters.ui.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ItemExampleBinding
import kotlin.properties.Delegates

typealias OnClickFoodListener = (name: String) -> Unit

class FoodAdapter(private val listener: OnClickFoodListener) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    var foods: List<String> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_example, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int = foods.size

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food, listener)
    }

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemExampleBinding.bind(view)

        fun bind(name: String, listener: OnClickFoodListener) {
            binding.chipName.text = name
            binding.chipName.setOnClickListener {
                listener(name)
            }
        }
    }
}