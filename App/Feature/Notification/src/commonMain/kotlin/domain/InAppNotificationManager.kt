package domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import model.Message

class InAppNotificationManager(
    val scope: CoroutineScope,
): NotificationManager {

    private val _queue = MutableStateFlow<List<Message>>(listOf())
//    val queue = _queue.asStateFlow()
    override fun notificationFlow(): Flow<List<Message>> = _queue.asStateFlow()

    override fun sendNotification(message: Message) {
        _queue.update { it + message }
        scope.launch {
            delay(5000)
            _queue.update { it - message }
        }
    }

    override fun forceClose(message: Message) {
        _queue.update { it - message }
    }
}