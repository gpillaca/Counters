package com.gpillaca.counters.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gpillaca.counters.MainActivity
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }

        binding.buttonStarted.setOnClickListener(this)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(view: View?) {
        val id = view?.id ?: return

        when (id) {
            R.id.buttonStarted -> {
                navigateToMain()
            }
        }
    }
}