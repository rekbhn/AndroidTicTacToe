package com.tictactoe.custom

data class SymbolOption(
    val id: String,
    val label: String,
    val emoji: String,
)

enum class GameMode {
    TwoPlayer,
    VsSystem,
}

val SYMBOL_OPTIONS: List<SymbolOption> = listOf(
    SymbolOption("alien", "Alien", "👽"),
    SymbolOption("rocket", "Rocket", "🚀"),
    SymbolOption("cat", "Cat", "🐱"),
    SymbolOption("dog", "Dog", "🐶"),
    SymbolOption("bird", "Bird", "🐦"),
    SymbolOption("leaves", "Leaves", "🍃"),
    SymbolOption("flower", "Flower", "🌸"),
    SymbolOption("cloud", "Cloud", "☁️"),
    SymbolOption("rainbow", "Rainbow", "🌈"),
    SymbolOption("sun", "Sun", "☀️"),
    SymbolOption("moon", "Moon", "🌙"),
    SymbolOption("pixel_heart", "Pixel heart", "❤️"),
    SymbolOption("ghost", "Ghost", "👻"),
    SymbolOption("pumpkin", "Pumpkin", "🎃"),
    SymbolOption("happy_face", "Happy face", "😊"),
    SymbolOption("winky_face", "Winky face", "😉"),
)

/** e.g. "ghost wins!", "pumpkin wins!" from the winning piece label. */
fun twoPlayerWinTitle(setup: GameSetup, winningPlayer: Int): String {
    val emoji = if (winningPlayer == 0) setup.player0Emoji else setup.player1Emoji
    val label = SYMBOL_OPTIONS.firstOrNull { it.emoji == emoji }?.label
    return if (label != null) "${label.lowercase()} wins!" else "$emoji wins!"
}

data class GameSetup(
    val mode: GameMode,
    val player0Emoji: String,
    val player1Emoji: String,
    /** Only used when [mode] is [GameMode.VsSystem]. */
    val humanIsPlayer0: Boolean,
    /** 0 or 1 — who takes the first turn. */
    val firstPlayer: Int,
)
