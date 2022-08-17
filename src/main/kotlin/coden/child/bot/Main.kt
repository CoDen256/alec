package coden.child.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId


fun main() {
    val bot = bot {
        token = "5651703557:AAFyw2eTxvFDxTiq4w1A-moeWxHhLqNnL6k"
        dispatch {
            text {
                bot.sendMessage(ChatId.fromId(message.chat.id), text = text)
            }
        }
    }
    bot.startPolling()
}


