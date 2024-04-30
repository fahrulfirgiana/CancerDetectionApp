package com.dicoding.asclepius.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.response.NewsResponse
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel {

    private val _news = MutableLiveData<List<ArticlesItem>>()
    val news: LiveData<List<ArticlesItem>> = _news

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        getNews()
    }

    fun getNews(query: String = "", category: String = "health") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchHealthNews(query, category)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _news.value = responseBody.articles
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                // Handle failure (e.g., display error message)
            }
        })
    }
}