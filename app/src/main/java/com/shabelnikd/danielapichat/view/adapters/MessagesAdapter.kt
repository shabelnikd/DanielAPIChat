package com.shabelnikd.danielapichat.view.adapters

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shabelnikd.danielapichat.databinding.ItemChatMessageBinding
import com.shabelnikd.danielapichat.model.models.MessagesResponse
import com.shabelnikd.danielapichat.view.fragments.chat.ChatFragment.Companion.ME_ID
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessagesAdapter(
) : ListAdapter<MessagesResponse, MessagesAdapter.ViewHolder>(GenericDiffUtil<MessagesResponse>()) {

    private var onClick: ((chatId: Int, messageId: Int, oldMessage: String) -> Unit)? = null
    private var onLongClick: ((chatId: Int, messageId: Int) -> Unit)? = null

    fun setOnClickListener(listener: (chatId: Int, messageId: Int, oldMessage: String) -> Unit) {
        this.onClick = listener
    }

    fun setOnLongClickListener(listener: (chatId: Int, messageId: Int) -> Unit) {
        this.onLongClick = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val messageItem = getItem(position)
        with(holder) {
            when {
                messageItem.message?.trim()?.startsWith("@image") == true -> {
                    runCatching {
                        Glide.with(binding.root).load(messageItem.message.replace("@image", "").trim()).into(binding.imageView)
                    }.onSuccess {
                        binding.imageView.visibility = View.VISIBLE
                        binding.tvMessageText.visibility = View.GONE
                    }

                }
                else -> {
                    binding.tvMessageText.visibility = View.VISIBLE
                    binding.imageView.visibility = View.GONE
                    binding.tvMessageText.text = messageItem.message
                }
            }

            binding.tvSendTime.text = formatTimestamp(messageItem.timestamp)

            when {
                messageItem.senderId?.toInt() == ME_ID -> {
                    binding.root.gravity = Gravity.END
                    binding.cardMessage.setCardBackgroundColor(Color.parseColor("#144e38"))
                }

                else -> {
                    binding.root.gravity = Gravity.START
                    binding.cardMessage.setCardBackgroundColor(Color.parseColor("#20262B"))

                }
            }

            itemView.setOnClickListener  {
                onClick?.invoke(messageItem.chatId!!, messageItem.id!!, messageItem.message!!)
            }

            itemView.setOnLongClickListener {
                onLongClick?.invoke(messageItem.chatId!!, messageItem.id!!)
                true
            }
        }

    }

    fun formatTimestamp(timestamp: String?): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = LocalDateTime.parse(timestamp, formatter)
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    class ViewHolder(val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root)

}