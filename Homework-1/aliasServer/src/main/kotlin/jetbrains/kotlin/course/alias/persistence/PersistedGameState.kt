package jetbrains.kotlin.course.alias.persistence // <-- Ensure package is correct

// === Imports needed for the data types ===
import jetbrains.kotlin.course.alias.results.GameResult // Type alias for List<Team>
import jetbrains.kotlin.course.alias.team.Team
import jetbrains.kotlin.course.alias.util.Identifier // Your Identifier type alias
import kotlinx.serialization.Serializable
// === End Imports ===

/**
 * Data class representing the complete state of the game to be persisted.
 * This object will be serialized to/from JSON.
 */
@Serializable // Mark this class itself as serializable
data class PersistedGameState(
    // Use immutable List/Map for serialization safety
    val gameHistory: List<GameResult>,
    val teamsStorage: Map<Identifier, Team>,

    // Store the last used ID counters
    val teamIdCounter: Int,
    val cardIdCounter: Int

    // TODO (Optional): Add other fields if needed later, e.g.:
    // val nextCardIndex: Int = 0 // If tracking used cards
)
