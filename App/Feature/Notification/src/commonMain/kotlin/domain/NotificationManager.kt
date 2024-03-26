package domain

import kotlinx.coroutines.flow.Flow
import model.Message
import model.MessageLevel

interface NotificationManager {

    fun notificationFlow():  Flow<List<Message>>

    fun sendNotification(message: Message)

    fun forceClose(message: Message)

    fun sendWarning(
        title: String,
        message: String = "",
    ) = sendNotification(Message(title, message, level = MessageLevel.warning))

    fun sendError(
        title: String,
        message: String = "",
    ) = sendNotification(Message(title, message, level = MessageLevel.error))
}