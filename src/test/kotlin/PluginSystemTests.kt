import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import pluginSystem.PluginSystem

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
    }
})