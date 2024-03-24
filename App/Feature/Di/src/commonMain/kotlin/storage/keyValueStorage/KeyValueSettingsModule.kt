package storage.keyValueStorage

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.online.education.app.core.util.store.KeyValueStorage

expect fun getSettings(): Settings

val keyValueStorageModule = module {
    singleOf(::getSettings)
    factory<KeyValueStorage<String, String>> { DefaultKeyValueStorage(get()) }
}