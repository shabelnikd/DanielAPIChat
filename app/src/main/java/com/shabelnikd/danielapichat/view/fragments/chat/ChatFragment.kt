package com.shabelnikd.danielapichat.view.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shabelnikd.danielapichat.databinding.FragmentChatBinding
import com.shabelnikd.danielapichat.view.adapters.MessagesAdapter
import kotlinx.coroutines.launch
import kotlin.getValue

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()

    private val messagesAdapter = MessagesAdapter()

    private val args: ChatFragmentArgs by navArgs()

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
        val chatId = args.chatId

        loadData(chatId)
        updateUI()
        setupListeners(chatId)

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
                    binding.rvChat.scrollToPosition(messagesAdapter.currentList.size - 1)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }

        messagesAdapter.setOnLongClickListener { chatId, messageId ->
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Удалить сообщение")
                .setPositiveButton("OK") { dialog, _ ->
                    viewModel.deleteMessage(chatId, messageId)
                    binding.rvChat.scrollToPosition(messagesAdapter.currentList.size - 1)
                }
                .setNegativeButton("Отмена", null)
                .show()

        }

        binding.rvChat.adapter = messagesAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadData(chatId: Int) {
        viewModel.getMessages(chatId)
    }

    private fun updateUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messagesResult.collect { messages ->
                when (messages) {
                    is ChatViewModel.MessagesResult.Success -> {
                        messagesAdapter.submitList(messages.messages)
                        binding.rvChat.scrollToPosition(messagesAdapter.currentList.size - 1)
                    }
                    is ChatViewModel.MessagesResult.Error -> {
                        Snackbar.make(binding.root, messages.errorMessage, 2000)
                    }
                }
            }
        }
    }

    private fun setupListeners(chatId: Int) {
        binding.btnSendMessage.setOnClickListener {
            viewModel.sendMessage(chatId, ME_ID, 0, binding.etMessageText.text.toString())
            binding.rvChat.scrollToPosition(messagesAdapter.currentList.size - 1)
            binding.etMessageText.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val ME_ID = 1838;
    }
}