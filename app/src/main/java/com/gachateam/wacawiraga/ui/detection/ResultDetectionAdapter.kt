package com.gachateam.wacawiraga.ui.detection

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gachateam.wacawiraga.ImageLabel
import com.gachateam.wacawiraga.databinding.ItemResultDetectionBinding

class ResultDetectionAdapter :
    ListAdapter<ImageLabel, ResultDetectionAdapter.ResultDetectionViewHolder>(imageLabelDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultDetectionViewHolder {
        val binding =
            ItemResultDetectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultDetectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultDetectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ResultDetectionViewHolder(val binding: ItemResultDetectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageLabel: ImageLabel) {
            val spanableText = SpannableStringBuilder()
            val spannableIndex = SpannableStringBuilder()
            val spannableConfidence = SpannableStringBuilder()
            if (adapterPosition == 0) { // first Item
                spanableText
                    .bold { append(imageLabel.text) }
                spannableConfidence
                    .bold { append(imageLabel.confidence.toString()) }
                spannableIndex
                    .bold { append(imageLabel.index.toString()) }
            } else {
                spanableText
                    .append(imageLabel.text)
                spannableConfidence
                    .append(imageLabel.confidence.toString())
                spannableIndex
                    .append(imageLabel.index.toString())
            }
            binding.resultText.text = spanableText
            binding.resultIndex.text = spannableIndex
            binding.confidence.text = spannableConfidence

        }
    }

    companion object{
        val imageLabelDiffCallback = object : DiffUtil.ItemCallback<ImageLabel>() {
            override fun areItemsTheSame(oldItem: ImageLabel, newItem: ImageLabel): Boolean = oldItem.text == newItem.text
            override fun areContentsTheSame(oldItem: ImageLabel, newItem: ImageLabel): Boolean = oldItem == newItem
        }
    }
}





