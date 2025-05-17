import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import pluginSystem.EventHandler
import pluginSystem.PluginSystem

val appModules = module {
    single<EventHandler>{ EventHandler() }
    factory{ SupervisorJob() + Dispatchers.Default }
    single{ PluginSystem(get(), get()) }
}