package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter(private var historyList: List<HistoryEntity>, private val onDeleteClickListener: (HistoryEntity) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val historyImg: ImageView = binding.savedImg
        val historyLabel: TextView = binding.tvLabel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyList[position]
        Glide.with(holder.itemView.context)
            .load(history.imagePath)
            .into(holder.historyImg)
        holder.historyLabel.text = history.result

        holder.itemView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            onDeleteClickListener(history)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
