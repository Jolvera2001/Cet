import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestScope
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import pluginSystem.IPlugin
import pluginSystem.PluginSystem
import kotlin.collections.emptyMap

class PluginSystemTests : FunSpec({
    lateinit var pluginSystem: pluginSystem.PluginSystem

    beforeTest {
        pluginSystem = PluginSystem()
    }

    context("plugin system lifecycle related tests") {
        test("plugin system has required public methods") {
            val methods = pluginSystem::class.java.methods.map { it.name }
            methods.contains("stop") shouldBe true
            methods.contains("startup") shouldBe true
        }
        test("plugin system starts without being null") {
            pluginSystem shouldNotBe null
        }

        // this should be replaced later methinks..
        // we can't assume that the system should startup with at LEAST
        // one plugin, or should we? Could be a design thing
        // but to me it seems to favor a sort of foundation startup
        // rather than being truly clean
        test("plugin system starts without being empty") {
            pluginSystem.startup()
            val pluginMap = pluginSystem.getPlugins()
            pluginMap.keys shouldNotBe emptyMap<String, IPlugin>()
        }
    }
})