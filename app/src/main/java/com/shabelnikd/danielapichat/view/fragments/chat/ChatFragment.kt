package com.shabelnikd.danielapichat.view.fragments.chat

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shabelnikd.danielapichat.R
import com.shabelnikd.danielapichat.databinding.FragmentChatBinding
import com.shabelnikd.danielapichat.view.adapters.MessagesAdapter
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()

    private val messagesAdapter = MessagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        loadData()
        updateUI()

        setupListeners()

    }

    private fun initUI() {
        messagesAdapter.setOnClickListener { chatId, messageId, oldMessage->
            val builder = AlertDialog.Builder(requireContext())
            val editText = EditText(requireContext())
            editText.setText(oldMessage)

            builder.setTitle("Изменить сообщение")
                .setView(editText)
                .setPositiveButton("OK") { dialog, _ ->
                    val newMessage = editText.text.toString()
                    viewModel.editMessage(chatId, messageId, newMessage)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }

        messagesAdapter.setOnLongClickListener { chatId, messageId ->
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Изменить сообщение")
                .setPositiveButton("OK") { dialog, _ ->
                    viewModel.deleteMessage(chatId, messageId)
                }
                .setNegativeButton("Отмена", null)
                .show()

        }

        binding.rvChat.adapter = messagesAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadData() {
        viewModel.getMessages(CHAT_ID)
    }

    private fun updateUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messagesResult.collect { messages ->
                when (messages) {
                    is ChatViewModel.MessagesResult.Success -> {
                        messagesAdapter.submitList(messages.messages)
                    }
                    is ChatViewModel.MessagesResult.Error -> {
                        Snackbar.make(binding.root, messages.errorMessage, 2000)
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnSendMessage.setOnClickListener {
            viewModel.sendMessage(CHAT_ID, ME_ID, 0, binding.etMessageText.text.toString())
        }
    }


    companion object {
        const val CHAT_ID = 2101;
        const val ME_ID = 1837;
    }
}