package com.shabelnikd.danielapichat

import com.shabelnikd.danielapichat.model.core.RetrofitClient
import com.shabelnikd.danielapichat.model.service.ChatApiServiceImpl
import com.shabelnikd.danielapichat.view.fragments.chat.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitClient(androidContext()) }
    single { get<RetrofitClient>().retrofitService }
    single { ChatApiServiceImpl(get()) }
}

val viewModelModule = module() {
    viewModel { ChatViewModel() }
}