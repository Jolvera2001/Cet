import core.CorePlugin
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import modules.testModulePluginCore
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import pluginSystem.CetEvent
import pluginSystem.EventHandler
import pluginSystem.PluginState

@OptIn(ExperimentalCoroutinesApi::class)
class CorePluginTest : FunSpec(), KoinTest {
    init {
        beforeTest {
            stopKoin()
            startKoin {
                modules(testModulePluginCore)
            }
        }

        afterTest {
            GlobalContext.stopKoin()
        }

        context("core plugin lifecycle tests") {
            test("core plugin is not null") {
                val corePlugin = get<CorePlugin>()

                corePlugin shouldNotBe null
                corePlugin.state shouldBe PluginState.STOPPED
            }
            // Core plugin should subscribe on startup
            test("core plugin initialized success") {
                val mockEventHandler = get<EventHandler>()
                val testScope = CoroutineScope(get<kotlin.coroutines.CoroutineContext>())
                val corePlugin = get<CorePlugin>()

                corePlugin.onInitialize(mockEventHandler, testScope)

                verify { mockEventHandler.subscribe() }
                corePlugin.state shouldBe PluginState.ACTIVE
            }
            test("core plugin disable success") {
                val mockEventHandler = get<EventHandler>()
                val testScope = CoroutineScope(get<kotlin.coroutines.CoroutineContext>())
                val corePlugin = get<CorePlugin>()

                corePlugin.onInitialize(mockEventHandler, testScope)
                corePlugin.onDisable()

                verify { mockEventHandler.subscribe() }
                corePlugin.state shouldBe PluginState.DISABLED
            }
        }

        context("core plugin event related tests") {
            test("core plugin sends event onInitialize") {
                val captureEvents = mutableListOf<CetEvent>()
                val mockEventHandler = get<EventHandler>()
                val corePlugin = get<CorePlugin>()

                // Dispatchers needed for Publish timing
                // within the plugin coroutine scope
                val testDispatcher = StandardTestDispatcher()
                val testScope = CoroutineScope(testDispatcher)

                coEvery { mockEventHandler.publish(capture(captureEvents)) } returns Unit
                corePlugin.onInitialize(mockEventHandler, testScope)

                // Advance time on our specific dispatcher
                testDispatcher.scheduler.advanceUntilIdle()

                coVerify { mockEventHandler.publish(any()) }
                captureEvents.size shouldBe 1
                captureEvents.first().shouldBeTypeOf<CetEvent.BaseEvents.PluginLifecycle>()
            }
            test("core plugin sends events on full lifecycle") {
                val captureEvents = mutableListOf<CetEvent>()
                val mockEventHandler = get<EventHandler>()
                val corePlugin = get<CorePlugin>()

                // Dispatchers needed for Publish timing
                // within the plugin coroutine scope
                val testDispatcher = StandardTestDispatcher()
                val testScope = CoroutineScope(testDispatcher)

                coEvery { mockEventHandler.publish(capture(captureEvents)) } returns Unit
                corePlugin.onInitialize(mockEventHandler, testScope)

                // Advance time on our specific dispatcher
                testDispatcher.scheduler.advanceUntilIdle()

                captureEvents.size shouldBe 1
                captureEvents.first().shouldBeTypeOf<CetEvent.BaseEvents.PluginLifecycle>()
                val firstEvent = captureEvents.first() as CetEvent.BaseEvents.PluginLifecycle
                firstEvent.state shouldBe PluginState.ACTIVE

                corePlugin.onDisable()
                testDispatcher.scheduler.advanceUntilIdle()

                captureEvents.size shouldBe 2
                captureEvents[1].shouldBeTypeOf<CetEvent.BaseEvents.PluginLifecycle>()
                corePlugin.state shouldBe PluginState.DISABLED
                coVerify(exactly = 2) { mockEventHandler.publish(any()) }
            }
        }
    }
}