package com.gpillaca.counters.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gpillaca.counters.ui.main.MainActivity
import com.gpillaca.counters.R
import com.gpillaca.counters.databinding.ActivityOnBoardingBinding
import com.gpillaca.counters.util.supportStatusBar

class OnBoardingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportStatusBar()
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