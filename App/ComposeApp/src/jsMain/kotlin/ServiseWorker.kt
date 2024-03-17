import kotlinx.browser.window
import org.w3c.dom.url.URL
import ru.online.education.app.feature.navigation.root.RootComponent
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import org.w3c.workers.FetchEvent
import org.w3c.workers.InstallEvent
import kotlin.js.Promise

val rootEndPoints = listOf(
    RootComponent.Child.AccountChild.name,
    RootComponent.Child.HomeChild.name,
    RootComponent.Child.NotFound.name,
).map { "/$it" }

external val caches: dynamic

fun addEndPointsToServiceWorker(
    endpoints: List<String>
) {
    window.addEventListener("fetch", { event ->
        val fetchEvent = event as FetchEvent
        val request = fetchEvent.request
        val url = URL(request.url)
        if (endpoints.contains(url.pathname)) {
            fetchEvent.respondWith(
                window.caches.match(request).then { response: Any? ->
                    response as Response? ?: window.fetch(request)
                }.catch {
                    window.caches.match("fallback.html") as Response
                }

            )
        }
    })
}