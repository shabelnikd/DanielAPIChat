package com.shabelnikd.danielapichat.view.adapters

import com.shabelnikd.danielapichat.model.models.MessagesResponse

class GenericDiffUtil<T : Any> : androidx.recyclerview.widget.DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when {
            oldItem is MessagesResponse && newItem is MessagesResponse -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when {
            oldItem is MessagesResponse && newItem is MessagesResponse -> oldItem == newItem
            else -> false
        }
    }
}