import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import modules.testModulePluginSystem
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import pluginSystem.IPlugin
import pluginSystem.PluginSystem

class PluginSystemTests : FunSpec(), KoinTest {
    init {
        beforeTest {
            stopKoin()
            startKoin {
                modules(testModulePluginSystem)
            }
        }

        afterTest {
            stopKoin()
        }

        context("plugin system lifecycle related tests") {
            test("plugin system has required public methods") {
                val pluginSystem = get<PluginSystem>()

                val methods = pluginSystem::class.java.methods.map { it.name }
                methods.contains("stop") shouldBe true
                methods.contains("startup") shouldBe true
            }
            test("plugin system starts without being null") {
                val pluginSystem = get<PluginSystem>()

                pluginSystem shouldNotBe null
            }

            // this should be replaced later methinks..
            // we can't assume that the system should startup with at LEAST
            // one plugin, or should we? Could be a design thing
            // but to me it seems to favor a sort of foundation startup
            // rather than being truly clean
            test("plugin system starts without being empty") {
                val pluginSystem = get<PluginSystem>()

                pluginSystem.start()

                val pluginMap = pluginSystem.getPlugins()
                pluginMap.keys shouldNotBe emptyMap<String, IPlugin>()
            }

            // I don't think that the plugins should just be taken out of the map
            // if the plugin system shuts down, it just stops every plugin
            test("plugin system stop still tracks plugins") {
                val pluginSystem = get<PluginSystem>()

                pluginSystem.start()
                pluginSystem.stop()

                val pluginMap = pluginSystem.getPlugins()
                pluginMap.keys shouldNotBe emptyMap<String, IPlugin>()
            }
        }

        context("plugin system coroutine related tests") {

        }

        context("plugin system event system related tests") {

        }
    }
}