package com.screenguard.privacy.screenfilter.ui.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.databinding.ItemPatternHomeBinding
import com.screenguard.privacy.screenfilter.ui.feature.pattern.PatternAdapter
import com.screenguard.privacy.screenfilter.ui.feature.pattern.PatternModel

class PatternHomeAdapter(
    private val items: List<PatternModel>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<PatternHomeAdapter.ViewHolder>() {

    var selectedId: Int = -1

    inner class ViewHolder(val binding: ItemPatternHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPatternHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            imgPattern.setImageResource(item.imageResId)

            rootView.isSelected = (item.id == selectedId)
            icSelected.visibility = if (item.id == selectedId) View.VISIBLE else View.GONE

            rootView.setOnClickListener {
                selectedId = item.id
                onItemClick(item.id)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = items.size
}