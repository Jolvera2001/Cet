package sharedItems

data class MenuItem(val id: String)

data class SubMenuItem(
    val text: String,
    val callback: Unit
)