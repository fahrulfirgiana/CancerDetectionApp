package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.adapter.HistoryAdapter
import com.dicoding.asclepius.view.model.HistoryViewModel
import com.dicoding.asclepius.view.model.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.menuBar)
        bottomNavigationView.selectedItemId = R.id.history_menu
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    true
                }
                R.id.news -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    true
                }
                R.id.history_menu -> {
                    true
                }
                else -> false
            }
        }

        viewModel = obtainViewModel(this@HistoryActivity)

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvHistory.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE
            }
        }

        viewModel.getAllHistory().observe(this) { historyList ->
            showRecyclerView(historyList as ArrayList<HistoryEntity>)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HistoryViewModel::class.java]
    }

    private fun showRecyclerView(historyList: ArrayList<HistoryEntity>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager
        binding.rvHistory.setHasFixedSize(true)
        historyAdapter = HistoryAdapter(historyList) { history ->
            viewModel.deleteHistory(history)
        }
        binding.rvHistory.adapter = historyAdapter
    }
}