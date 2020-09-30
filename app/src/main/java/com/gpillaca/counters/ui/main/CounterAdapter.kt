package com.gpillaca.counters.ui.main

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ItemCounterBinding
import com.gpillaca.counters.domain.Counter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

typealias OnClickCounterListener = (counter: Counter, action: CounterAction) -> Unit
typealias OnLongClickCounterListener = () -> Unit
typealias Lister = (counters: List<Counter>) -> Unit

enum class CounterAction {
    DELETE,
    PLUS,
    LESS,
    DIALOG
}

class CounterAdapter(
    private val onClickCounterListener: OnClickCounterListener,
    private val onLongClickCounterListener: OnLongClickCounterListener,
    private val lister: Lister
) : RecyclerView.Adapter<CounterAdapter.CounterViewHolder>(), Filterable {

    var selectedItems = SparseBooleanArray()
    var counters: MutableList<Counter> by Delegates.observable(mutableListOf()) { _, oldValue, newValue ->
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

    var countersTemp: List<Counter> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_counter, parent, false)
        return CounterViewHolder(view)
    }

    override fun getItemCount(): Int = counters.size

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        val counter = counters[position]
        holder.bind(counter, onClickCounterListener)
        hideQuantityButtons(holder, selectedItems.size() > 0)

        holder.itemView.setOnLongClickListener {
            toggleSelection(holder.adapterPosition)
            onLongClickCounterListener()
            true
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()

                if (countersTemp.isEmpty()) {
                    countersTemp = counters
                }

                val filterList: List<Counter>

                if (constraint.isEmpty()) {
                    filterList = countersTemp
                    countersTemp = emptyList()
                } else {
                    filterList = counters.filter {
                        it.title.toLowerCase(Locale.ROOT)
                            .contains(charSearch.toLowerCase(Locale.ROOT))
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                counters = results?.values as ArrayList<Counter>
                lister(counters)
            }
        }
    }

    private fun hideQuantityButtons(holder: CounterViewHolder, value: Boolean) {
        holder.binding.buttonPlus.visibility = if (value) View.GONE else View.VISIBLE
        holder.binding.buttonLess.visibility = if (value) View.GONE else View.VISIBLE
        holder.binding.textViewValue.visibility = if (value) View.GONE else View.VISIBLE
    }

    fun clearSelection() {
        selectedItems.clear()
        counters.map {
            it.isSelected = false
        }
    }

    private fun toggleSelection(position: Int) {
        if (selectedItems.get(position)) {
            selectedItems.delete(position)
            counters[position].isSelected = false
        } else {
            selectedItems.put(position, true)
            counters[position].isSelected = true
        }

        if (selectedItems.size() <= 1) {
            notifyDataSetChanged()
        } else {
            notifyItemChanged(position)
        }
    }

    class CounterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCounterBinding.bind(view.rootView)

        fun bind(counter: Counter, onClickCounterListener: OnClickCounterListener) {
            selectedItem(counter.isSelected)
            binding.textViewName.text = counter.title
            binding.textViewValue.text = counter.count.toString()
            binding.buttonPlus.setOnClickListener {
                onClickCounterListener(counter, CounterAction.PLUS)
            }

            binding.buttonLess.setOnClickListener {
                if (counter.count == 0) {
                    binding.buttonLess.isEnabled = false
                    binding.buttonLess.setImageResource(R.drawable.ic_less_gray)
                    onClickCounterListener(counter, CounterAction.DIALOG)
                    return@setOnClickListener
                }

                binding.buttonLess.isEnabled = true
                binding.buttonLess.setImageResource(R.drawable.ic_less)
                onClickCounterListener(counter, CounterAction.LESS)
            }
        }

        private fun selectedItem(isSelected: Boolean) {
            binding.viewItemBackground.visibility = if (isSelected) View.VISIBLE else View.GONE
            binding.buttonCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
        }
    }
}