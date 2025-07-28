package com.mikhailovskii

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.webhook
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.FileInputStream
import java.util.*

fun main() {
    val props = Properties()
    try {
        FileInputStream("keys.properties").use { props.load(it) }
    } catch (e: Exception) {
        println("Failed to load keys.properties: ${e.message}")
    }

    val token = System.getenv("TOKEN") ?: props.getProperty("TOKEN")
    val url = System.getenv("URL") ?: props.getProperty("URL")
    val redirects = mutableMapOf<Long, Long>()

    val bot = bot {
        this.token = token
        webhook {
            this.url = url
        }
        dispatch {
            command("config") {
                val (from, to) = this.args.map(String::toLong)
                redirects[from] = to
            }
            command("chatId") {
                val chatId = message.chat.id
                bot.sendMessage(ChatId.fromId(chatId), "Chat id=$chatId")
            }
            command("save") {
                val targetMessageId = message.replyToMessage?.messageId ?: message.messageId
                bot.forwardMessage(
                    chatId = ChatId.fromId(redirects[message.chat.id]!!),
                    fromChatId = ChatId.fromId(message.chat.id),
                    messageId = targetMessageId,
                )
            }
        }
    }

    bot.startWebhook()

    embeddedServer(Netty, port = 8080) {
        routing {
            post("/webhook") {
                val response = call.receiveText()
                bot.processUpdate(response)
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}