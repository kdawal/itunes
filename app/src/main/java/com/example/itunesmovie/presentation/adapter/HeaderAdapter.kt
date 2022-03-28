package com.example.itunesmovie.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesmovie.databinding.ItemHeaderBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
  private var data = ArrayList<String>()

  fun setData(date: String) {
    data.clear()
    data.add(date)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
    val headerBinding = ItemHeaderBinding
      .inflate(LayoutInflater.from(parent.context), parent, false)
    return HeaderViewHolder(headerBinding)
  }

  override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
    holder.bind(data[position])
  }

  override fun getItemCount(): Int {
    return data.size
  }

  inner class HeaderViewHolder(
   private val headerBinding: ItemHeaderBinding,
  ) : RecyclerView.ViewHolder(headerBinding.root) {
    fun bind(date: String) {
      headerBinding.dateTextView.text = date
    }

  }
}