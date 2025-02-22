package com.shabelnikd.danielapichat.model.models

enum class ReservedChatsEnum(val chatId: Int, val chatName: String) {
    M06GROUP(2101, "M06 Группа"),
    SPAM(2102, "Спам"),
    USEFUL_URLS(2103, "Полезные ссылки"),
    PHOTOS(2104, "Фото"),
    TESTING(2105, "Тестовый чат")
}