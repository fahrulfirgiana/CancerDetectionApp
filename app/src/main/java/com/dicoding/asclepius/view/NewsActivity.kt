package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.view.adapter.NewsAdapter
import com.dicoding.asclepius.view.model.NewsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.menuBar)
        bottomNavigationView.selectedItemId = R.id.news
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    true
                }
                R.id.news -> {
                    true
                }
                R.id.history_menu -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    true
                }
                else -> false
            }
        }

        initViewModel()
        setupRecyclerView()
        viewModel.getNews()

        observeViewModel()


    }

    private fun initViewModel() {
        viewModel = NewsViewModel()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@NewsActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this, { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvNews.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvNews.visibility = View.VISIBLE
            }
        })

        viewModel.news.observe(this, { users ->
            newsAdapter.submitList(users)
        })
    }

    fun openNewsUrl(view: View) {
        val url = view.getTag(R.id.tvLink) as? String
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}