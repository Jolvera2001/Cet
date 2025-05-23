package modules

import core.CorePlugin
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.dsl.module
import pluginSystem.EventHandler
import pluginSystem.PluginSystem

val testModulePluginSystem = module {
    // TODO: learn how to define this better
    single<EventHandler>{
        mockk<EventHandler>(relaxed = true).apply {
            every { Subscribe() } returns mockk(relaxed = true)
        }
    }
    factory<kotlin.coroutines.CoroutineContext> { StandardTestDispatcher() + SupervisorJob() }
    single { PluginSystem(get(), get()) }
}

val testModulePluginCore = module {
    single<EventHandler>{
        mockk<EventHandler>(relaxed = true).apply {
            every { Subscribe() } returns mockk(relaxed = true)
        }
    }
    factory<kotlin.coroutines.CoroutineContext> { StandardTestDispatcher() + SupervisorJob() }
    factory { CorePlugin() }
}