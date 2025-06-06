package modules

import EventHandler
import PluginSystem
import core.CorePlugin
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val testModulePluginSystem = module {
    // TODO: learn how to define this better
    single<EventHandler>{
        mockk<EventHandler>(relaxed = true).apply {
            every { subscribe() } returns mockk(relaxed = true)
        }
    }
    factory<CoroutineContext> { StandardTestDispatcher() + SupervisorJob() }
    single { PluginSystem(get(), get()) }
}

val testModulePluginCore = module {
    single<EventHandler>{
        mockk<EventHandler>(relaxed = true).apply {
            every { subscribe() } returns mockk(relaxed = true)
        }
    }
    factory<CoroutineContext> { StandardTestDispatcher() + SupervisorJob() }
    factory { CorePlugin() }
}