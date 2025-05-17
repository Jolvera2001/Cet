package modules

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.dsl.module
import pluginSystem.EventHandler
import pluginSystem.PluginSystem

val testModulePluginSystem = module {
    single<EventHandler>{ EventHandler() }
    factory { StandardTestDispatcher() + SupervisorJob() }
    single { PluginSystem(get(), get()) }
}