package com.tictactoe.custom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                TicTacToeRoot()
            }
        }
    }
}

private const val ROUTE_SPLASH = "splash"
private const val ROUTE_SETUP = "setup"
private const val ROUTE_GAME = "game"

@Composable
private fun TicTacToeRoot() {
    var route by rememberSaveable { mutableStateOf(ROUTE_SPLASH) }
    var modeName by rememberSaveable { mutableStateOf(GameMode.TwoPlayer.name) }
    var activeSetup by remember { mutableStateOf<GameSetup?>(null) }
    val selectedMode = GameMode.valueOf(modeName)

    when (route) {
        ROUTE_SPLASH -> SplashScreen(
            onTwoPlayers = {
                modeName = GameMode.TwoPlayer.name
                route = ROUTE_SETUP
            },
            onSinglePlayer = {
                modeName = GameMode.VsSystem.name
                route = ROUTE_SETUP
            },
        )
        ROUTE_SETUP -> SetupScreen(
            mode = selectedMode,
            onBack = { route = ROUTE_SPLASH },
            onStart = { setup ->
                activeSetup = setup
                route = ROUTE_GAME
            },
        )
        ROUTE_GAME -> {
            val setup = activeSetup
            if (setup == null) {
                route = ROUTE_SETUP
            } else {
                GameScreen(
                    setup = setup,
                    onExit = { route = ROUTE_SETUP },
                )
            }
        }
    }
}

@Composable
private fun SplashScreen(
    onTwoPlayers: () -> Unit,
    onSinglePlayer: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        scheme.surface,
                        scheme.surfaceContainerLowest,
                        scheme.surfaceContainerLow,
                    ),
                ),
            )
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        scheme.primaryContainer.copy(alpha = 0.5f),
                        Color.Transparent,
                    ),
                    center = Offset(w * 0.88f, h * 0.08f),
                    radius = w * 0.5f,
                ),
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        scheme.secondaryContainer.copy(alpha = 0.4f),
                        Color.Transparent,
                    ),
                    center = Offset(w * 0.05f, h * 0.58f),
                    radius = h * 0.42f,
                ),
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        scheme.tertiaryContainer.copy(alpha = 0.28f),
                        Color.Transparent,
                    ),
                    center = Offset(w * 0.55f, h * 0.92f),
                    radius = w * 0.45f,
                ),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Tic Tac Toe",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.onSurface,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.5).sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Pick a mode to begin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = scheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                SplashGhostPumpkinForeground()
            }
            Spacer(modifier = Modifier.weight(1f, fill = true))
            SplashMenuCard(
                onTwoPlayers = onTwoPlayers,
                onSinglePlayer = onSinglePlayer,
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

private const val SPLASH_GHOST = "👻"
private const val SPLASH_PUMPKIN = "🎃"

@Composable
private fun SplashGhostPumpkinForeground() {
    val scheme = MaterialTheme.colorScheme
    val winningIndices = remember { setOf(0, 1, 2) }
    val pieces = remember {
        listOf(
            "g", "g", "g",
            "p", null, "p",
            null, "p", null,
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = SPLASH_PUMPKIN,
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 10.dp),
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = scheme.surfaceContainerHigh.copy(alpha = 0.9f),
                ),
                border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    for (row in 0 until 3) {
                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            for (col in 0 until 3) {
                                val idx = row * 3 + col
                                val key = pieces[idx]
                                val isWinning = idx in winningIndices
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when {
                                                isWinning ->
                                                    scheme.primaryContainer.copy(alpha = 0.75f)
                                                key != null ->
                                                    scheme.surfaceVariant.copy(alpha = 0.45f)
                                                else ->
                                                    scheme.surface.copy(alpha = 0.35f)
                                            },
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = when (key) {
                                            "g" -> SPLASH_GHOST
                                            "p" -> SPLASH_PUMPKIN
                                            else -> ""
                                        },
                                        fontSize = 22.sp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Text(
                text = SPLASH_GHOST,
                fontSize = 48.sp,
                modifier = Modifier.padding(start = 10.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ghost wins this round",
            style = MaterialTheme.typography.labelLarge,
            color = scheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SplashMenuCard(
    onTwoPlayers: () -> Unit,
    onSinglePlayer: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val cardShape = RoundedCornerShape(20.dp)
    val buttonShape = RoundedCornerShape(14.dp)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = scheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.45f)),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            FilledTonalButton(
                onClick = onTwoPlayers,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = buttonShape,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = scheme.primaryContainer,
                    contentColor = scheme.onPrimaryContainer,
                ),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                    )
                    Text(
                        "Two players",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            FilledTonalButton(
                onClick = onSinglePlayer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = buttonShape,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = scheme.secondaryContainer,
                    contentColor = scheme.onSecondaryContainer,
                ),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SmartToy,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                    )
                    Text(
                        "Single player vs system",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SetupScreen(
    mode: GameMode,
    onBack: () -> Unit,
    onStart: (GameSetup) -> Unit,
) {
    var p0SymbolId by rememberSaveable { mutableStateOf(SYMBOL_OPTIONS.first().id) }
    var p1SymbolId by rememberSaveable { mutableStateOf(SYMBOL_OPTIONS[1].id) }
    var humanIsPlayer0 by rememberSaveable { mutableStateOf(true) }
    var firstPlayer by rememberSaveable { mutableIntStateOf(0) }

    val p0Emoji = SYMBOL_OPTIONS.firstOrNull { it.id == p0SymbolId }?.emoji ?: SYMBOL_OPTIONS.first().emoji
    val p1Emoji = SYMBOL_OPTIONS.firstOrNull { it.id == p1SymbolId }?.emoji ?: SYMBOL_OPTIONS[1].emoji
    val symbolsClash = p0Emoji == p1Emoji

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customize game") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                when (mode) {
                    GameMode.TwoPlayer -> "Two players"
                    GameMode.VsSystem -> "Vs system"
                },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    "Player 1",
                    modifier = Modifier
                        .widthIn(min = 68.dp, max = 68.dp)
                        .padding(top = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )
                CompactSymbolGrid(
                    selectedId = p0SymbolId,
                    onSelected = { p0SymbolId = it },
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    if (mode == GameMode.TwoPlayer) "Player 2" else "System",
                    modifier = Modifier
                        .widthIn(min = 68.dp, max = 68.dp)
                        .padding(top = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )
                CompactSymbolGrid(
                    selectedId = p1SymbolId,
                    onSelected = { p1SymbolId = it },
                    modifier = Modifier.weight(1f),
                )
            }

            if (symbolsClash) {
                Text(
                    "Pick two different pieces.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                )
            }

            if (mode == GameMode.VsSystem) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        "You",
                        modifier = Modifier.widthIn(min = 68.dp, max = 68.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        RadioButton(
                            selected = humanIsPlayer0,
                            onClick = { humanIsPlayer0 = true },
                        )
                        Text(
                            p0Emoji,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(end = 4.dp),
                        )
                        RadioButton(
                            selected = !humanIsPlayer0,
                            onClick = { humanIsPlayer0 = false },
                        )
                        Text(p1Emoji, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "First",
                    modifier = Modifier.widthIn(min = 68.dp, max = 68.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = firstPlayer == 0,
                        onClick = { firstPlayer = 0 },
                        label = {
                            Text(
                                "P1 $p0Emoji",
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                    )
                    FilterChip(
                        selected = firstPlayer == 1,
                        onClick = { firstPlayer = 1 },
                        label = {
                            Text(
                                "P2 $p1Emoji",
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onStart(
                        GameSetup(
                            mode = mode,
                            player0Emoji = p0Emoji,
                            player1Emoji = p1Emoji,
                            humanIsPlayer0 = humanIsPlayer0,
                            firstPlayer = firstPlayer,
                        ),
                    )
                },
                enabled = !symbolsClash,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Start game")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CompactSymbolGrid(
    selectedId: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 8,
    ) {
        SYMBOL_OPTIONS.forEach { opt ->
            val selected = opt.id == selectedId
            Surface(
                onClick = { onSelected(opt.id) },
                shape = RoundedCornerShape(8.dp),
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                },
                modifier = Modifier
                    .size(36.dp)
                    .then(
                        if (selected) {
                            Modifier.border(
                                2.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(8.dp),
                            )
                        } else {
                            Modifier
                        },
                    ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = opt.emoji, fontSize = 18.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScreen(
    setup: GameSetup,
    onExit: () -> Unit,
) {
    val board = remember(setup) {
        mutableStateListOf<Int?>(null, null, null, null, null, null, null, null, null)
    }
    var currentPlayer by remember(setup) { mutableIntStateOf(setup.firstPlayer) }
    var showEndDialog by remember { mutableStateOf<GameOutcome?>(null) }

    val humanIndex = if (setup.mode == GameMode.VsSystem) {
        if (setup.humanIsPlayer0) 0 else 1
    } else {
        -1
    }
    val aiIndex = if (setup.mode == GameMode.VsSystem) 1 - humanIndex else -1

    fun boardArray(): Array<Int?> = Array(9) { board[it] }

    fun checkTerminal(): Boolean {
        when (val o = outcome(boardArray())) {
            GameOutcome.Playing -> return false
            else -> {
                showEndDialog = o
                return true
            }
        }
    }

    fun applyMove(index: Int, player: Int) {
        if (board[index] != null) return
        board[index] = player
        if (!checkTerminal()) {
            currentPlayer = 1 - currentPlayer
        }
    }

    LaunchedEffect(setup, currentPlayer, board.toList()) {
        if (setup.mode != GameMode.VsSystem) return@LaunchedEffect
        if (showEndDialog != null) return@LaunchedEffect
        if (currentPlayer != aiIndex) return@LaunchedEffect

        delay(450)
        val snapshot = boardArray()
        if (outcome(snapshot) != GameOutcome.Playing) return@LaunchedEffect

        val move = bestAiMove(
            board = snapshot,
            aiPlayer = aiIndex,
            humanPlayer = humanIndex,
        )
        if (move != null) {
            applyMove(move, aiIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tic Tac Toe") },
                navigationIcon = {
                    IconButton(onClick = onExit) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        },
    ) { inner ->
        val end = showEndDialog
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .padding(bottom = if (end != null) 168.dp else 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val status = when {
                    showEndDialog != null -> ""
                    setup.mode == GameMode.VsSystem && currentPlayer == aiIndex ->
                        "System is thinking…"
                    setup.mode == GameMode.VsSystem && currentPlayer == humanIndex ->
                        "Your turn (${emojiFor(setup, humanIndex)})"
                    setup.mode == GameMode.TwoPlayer ->
                        "Player ${currentPlayer + 1}'s turn (${emojiFor(setup, currentPlayer)})"
                    else -> ""
                }
                if (status.isNotEmpty()) {
                    Text(
                        status,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                val boardSnapshot = board.toList()
                val winHighlight = remember(boardSnapshot) {
                    winningLineIndices(Array(9) { boardSnapshot[it] })?.toSet()
                }

                BoardGrid(
                    setup = setup,
                    board = board,
                    winHighlight = winHighlight,
                    enabled = showEndDialog == null &&
                        (setup.mode == GameMode.TwoPlayer || currentPlayer == humanIndex),
                    onCellClick = { idx ->
                        applyMove(idx, currentPlayer)
                    },
                )

                if (end == null) {
                    TextButton(onClick = onExit) { Text("Change setup") }
                }
            }

            if (end != null) {
                val title = when (end) {
                    GameOutcome.Draw -> "Draw"
                    is GameOutcome.Win -> {
                        when (setup.mode) {
                            GameMode.TwoPlayer -> twoPlayerWinTitle(setup, end.player)
                            GameMode.VsSystem ->
                                if (end.player == humanIndex) "You Win!"
                                else "Try again!"
                        }
                    }
                    GameOutcome.Playing -> ""
                }
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        when (end) {
                            GameOutcome.Draw -> Text(
                                "The grid is full with no winner.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            is GameOutcome.Win -> { }
                            GameOutcome.Playing -> { }
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            TextButton(
                                onClick = {
                                    showEndDialog = null
                                    for (i in 0 until 9) board[i] = null
                                    currentPlayer = setup.firstPlayer
                                },
                            ) { Text("Play again") }
                        }
                    }
                }
            }
        }
    }
}

private fun emojiFor(setup: GameSetup, player: Int): String =
    if (player == 0) setup.player0Emoji else setup.player1Emoji

@Composable
private fun BoardGrid(
    setup: GameSetup,
    board: List<Int?>,
    enabled: Boolean,
    winHighlight: Set<Int>?,
    onCellClick: (Int) -> Unit,
) {
    val cellShape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .widthIn(max = 360.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        for (row in 0 until 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                for (col in 0 until 3) {
                    val idx = row * 3 + col
                    val mark = board[idx]
                    val emoji = mark?.let { emojiFor(setup, it) }
                    val isWinCell = winHighlight?.contains(idx) == true
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .then(
                                if (isWinCell) {
                                    Modifier.border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = cellShape,
                                    )
                                } else {
                                    Modifier
                                },
                            )
                            .clickable(enabled = enabled && mark == null) {
                                onCellClick(idx)
                            },
                        shape = cellShape,
                        colors = CardDefaults.cardColors(
                            containerColor = if (isWinCell) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isWinCell) 6.dp else 2.dp,
                        ),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = emoji.orEmpty(),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}
