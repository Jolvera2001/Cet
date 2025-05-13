import PluginSystem.PluginSystem
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe

class PluginSystemTests : FunSpec({
    lateinit var pluginSystem: PluginSystem

    beforeTest {
        pluginSystem = PluginSystem()
    }

    afterTest {
        pluginSystem.stop()
    }

    context("plugin system lifecycle related tests") {
        test("plugin system starts without being null") {
            pluginSystem shouldNotBe null
        }
    }
})