package jetbrains.kotlin.course.alias.results

// === Add these imports ===
import jetbrains.kotlin.course.alias.card.CardService // Needed for saveState
import jetbrains.kotlin.course.alias.persistence.GameStatePersistenceService // The persistence service
import jetbrains.kotlin.course.alias.team.Team // Keep this
import jetbrains.kotlin.course.alias.team.TeamService // Needed for saveState
import org.springframework.beans.factory.annotation.Autowired // For dependency injection
import org.springframework.stereotype.Service // Keep this
// === End imports ===

// Keep the type alias
typealias GameResult = List<Team>

@Service
class GameResultsService {

    // === Inject dependencies needed for saving state ===
    // Spring will automatically provide instances of these @Service classes
    @Autowired
    private lateinit var persistenceService: GameStatePersistenceService

    @Autowired
    private lateinit var teamService: TeamService

    @Autowired
    private lateinit var cardService: CardService
    // === End injection ===

    // Keep the companion object and gameHistory
    companion object {
        // Stores the results of all completed games.
        val gameHistory: MutableList<GameResult> = mutableListOf()
    }

    /**
     * Saves the results of a completed game after validation,
     * and then triggers saving the entire application state.
     *
     * @param result The [GameResult] (List<Team>) to save.
     * @throws IllegalArgumentException if the result is empty or contains unknown team IDs.
     */
    fun saveGameResults(result: GameResult) {
        // --- Keep existing validation ---
        require(result.isNotEmpty()) {
            "Cannot save empty game results!"
        }
        val allTeamIdsExist = result.all { team ->
            TeamService.teamsStorage.containsKey(team.id)
        }
        require(allTeamIdsExist) {
            "Cannot save game results with unknown team IDs! " +
                    "Found IDs: ${result.map { it.id }}. Known IDs: ${TeamService.teamsStorage.keys}"
        }
        // --- End validation ---

        // Add the current game result to the history list
        gameHistory.add(result)
        println("Game result added to history.") // Log this step

        // === PASTE THIS BLOCK TO TRIGGER SAVE STATE ===
        println("Attempting to trigger game state save...")
        try {
            // Call the save function on the injected persistence service
            // Pass the other injected services so it can get their current state
            persistenceService.saveState(teamService, cardService, this)
        } catch (e: Exception) {
            // Log errors during the save trigger, but don't let it crash the main operation
            System.err.println("Error triggering game state save after saving results: ${e.message}")
            e.printStackTrace()
        }
        // === END PASTE ===
    }

    // Keep the getAllGameResults method
    fun getAllGameResults(): List<GameResult> {
        // Return a reversed copy of the history list
        return gameHistory.reversed()
    }
}
