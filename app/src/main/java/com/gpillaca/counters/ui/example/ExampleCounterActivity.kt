package com.gpillaca.counters.ui.example

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityExampleCounterBinding

class ExampleCounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExampleCounterBinding
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var miscAdapter: MiscAdapter

    companion object {
        const val PARAM_COUNTER_NAME = "ExampleCounterActivity:name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initAdapters()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initAdapters() {
        foodAdapter = FoodAdapter { name ->
            sendName(name)
        }
        drinkAdapter = DrinkAdapter { name ->
            sendName(name)
        }
        miscAdapter = MiscAdapter { name ->
            sendName(name)
        }

        foodAdapter.foods = getFoodList()
        miscAdapter.miscs = getMiscList()
        drinkAdapter.drinks = getDrinkList()
        binding.recyclerViewFood.addItemDecoration(ExampleItemDecorator())
        binding.recyclerViewDrinks.addItemDecoration(ExampleItemDecorator())
        binding.recyclerViewMisc.addItemDecoration(ExampleItemDecorator())
        binding.recyclerViewFood.adapter = foodAdapter
        binding.recyclerViewDrinks.adapter = drinkAdapter
        binding.recyclerViewMisc.adapter = miscAdapter
    }

    private fun sendName(name: String) {
        val intent = Intent()
        intent.putExtra(PARAM_COUNTER_NAME, name)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun getDrinkList(): ArrayList<String> {
        val stringArray = resources.getStringArray(R.array.drinks)
        val drinks = ArrayList<String>()
        drinks.addAll(stringArray)
        return drinks
    }

    private fun getFoodList(): ArrayList<String> {
        val stringArray = resources.getStringArray(R.array.food)
        val food = ArrayList<String>()
        food.addAll(stringArray)
        return food
    }

    private fun getMiscList(): ArrayList<String> {
        val stringArray = resources.getStringArray(R.array.misc)
        val misc = ArrayList<String>()
        misc.addAll(stringArray)
        return misc
    }
}