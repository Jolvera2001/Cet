import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import EventHandler
import PluginSystem

val appModules = module {
    single<EventHandler>{ EventHandler() }
    factory{ SupervisorJob() + Dispatchers.Default }
    single{ PluginSystem(get(), get()) }
}

fun startAppKoin() {
    startKoin {
        modules(appModules)
    }
}