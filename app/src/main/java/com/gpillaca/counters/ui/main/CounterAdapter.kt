package com.gpillaca.counters.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ItemCounterBinding
import com.gpillaca.counters.domain.Counter
import kotlin.properties.Delegates

typealias OnClickListener = (counter: Counter, action: CounterAction) -> Unit

enum class CounterAction {
    DELETE,
    PLUS,
    LESS,
    DIALOG
}

class CounterAdapter(
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<CounterAdapter.CounterViewHolder>() {

    var counters: List<Counter> by Delegates.observable(listOf()) { _, oldValue, newValue ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldValue[oldItemPosition].id == newValue[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldValue[oldItemPosition] == newValue[newItemPosition]
            }

            override fun getOldListSize(): Int = oldValue.size

            override fun getNewListSize(): Int = newValue.size
        }).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_counter, parent, false)
        return CounterViewHolder(view)
    }

    override fun getItemCount(): Int = counters.size

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        val counter = counters[position]
        holder.bind(counter, onClickListener)
    }

    class CounterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCounterBinding.bind(view.rootView)

        fun bind(counter: Counter, onClickListener: OnClickListener) {
            binding.textViewName.text = counter.title
            binding.textViewValue.text = counter.count.toString()

            binding.root.setOnLongClickListener {
                onClickListener(counter, CounterAction.DELETE)
                true
            }

            binding.buttonPlus.setOnClickListener {
                onClickListener(counter, CounterAction.PLUS)
            }

            binding.buttonLess.setOnClickListener {
                binding.buttonLess.isEnabled = true
                binding.buttonLess.setImageResource(R.drawable.ic_less)

                if (counter.count == 0) {
                    binding.buttonLess.isEnabled = false
                    binding.buttonLess.setImageResource(R.drawable.ic_less_gray)
                    onClickListener(counter, CounterAction.DIALOG)
                    return@setOnClickListener
                }
                onClickListener(counter, CounterAction.LESS)
            }
        }
    }
}