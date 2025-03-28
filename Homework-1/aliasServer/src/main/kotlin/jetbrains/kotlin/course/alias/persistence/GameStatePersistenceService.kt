package jetbrains.kotlin.course.alias.persistence

// === Imports ===
import jetbrains.kotlin.course.alias.card.CardService
import jetbrains.kotlin.course.alias.results.GameResultsService
import jetbrains.kotlin.course.alias.team.TeamService
import kotlinx.serialization.decodeFromString // For reading JSON
import kotlinx.serialization.encodeToString   // For writing JSON
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service // Spring annotation
import java.io.File
import java.io.IOException // For file exceptions
// === End Imports ===

@Service // Mark as a Spring service so it can be injected later
class GameStatePersistenceService {

    // Configure the JSON serializer instance
    // prettyPrint makes the JSON file human-readable
    // ignoreUnknownKeys helps if you add fields later and load an older save file
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    // Define the file where the game state will be saved
    // This will typically be in the root directory where the application is run
    private val saveFile = File("alias_gamestate.json")

    /**
     * Saves the current state of the game services to the JSON file.
     * Gathers data from the provided services, creates a PersistedGameState object,
     * serializes it, and writes it to the file.
     *
     * @param teamService The service holding team data and its ID factory.
     * @param cardService The service holding card data and its ID factory.
     * @param gameResultsService The service holding the game history.
     */
    fun saveState(
        teamService: TeamService,
        cardService: CardService,
        gameResultsService: GameResultsService
    ) {
        try {
            println("Attempting to save game state...") // Log the action

            // 1. Gather the current state from services
            val currentState = PersistedGameState(
                // Convert mutable collections to immutable Lists/Maps for serialization
                gameHistory = GameResultsService.gameHistory.toList(),
                teamsStorage = TeamService.teamsStorage.toMap(),
                // Get counter values using the methods we added to IdentifierFactory
                teamIdCounter = teamService.identifierFactory.getCurrentValue(),
                cardIdCounter = cardService.identifierFactory.getCurrentValue()
                // Add other state fields here if you implemented them (e.g., nextCardIndex)
            )

            // 2. Serialize the state object to a JSON string
            val jsonString = json.encodeToString(currentState)

            // 3. Write the JSON string to the file, overwriting previous content
            saveFile.writeText(jsonString)

            println("Game state saved successfully to ${saveFile.absolutePath}") // Log success

        } catch (e: IOException) {
            // Handle errors specifically related to file writing
            System.err.println("Error writing game state file '${saveFile.absolutePath}': ${e.message}")
        } catch (e: Exception) {
            // Handle any other unexpected errors during state gathering or serialization
            System.err.println("Unexpected error saving game state: ${e.message}")
            e.printStackTrace() // Print stack trace for debugging
        }
    }

    /**
     * Loads game state from the JSON file and applies it to the services.
     * Reads the file, deserializes the JSON into a PersistedGameState object,
     * and updates the state within the provided services.
     * Should be called on application startup.
     *
     * @param teamService The service to apply team data and ID counter to.
     * @param cardService The service to apply card ID counter to.
     * @param gameResultsService The service to apply game history to.
     */
    fun loadAndApplyState(
        teamService: TeamService,
        cardService: CardService,
        gameResultsService: GameResultsService
    ) {
        // 1. Check if the save file exists and is readable
        if (!saveFile.exists() || !saveFile.canRead()) {
            println("No readable save file found at ${saveFile.absolutePath}. Starting with fresh state.")
            return // Exit the function, nothing to load
        }

        try {
            println("Attempting to load game state from ${saveFile.absolutePath}...") // Log action

            // 2. Read the entire file content as a string
            val jsonString = saveFile.readText()

            // Handle case where file exists but is empty
            if (jsonString.isBlank()) {
                println("Save file is empty. Starting with fresh state.")
                return
            }

            // 3. Deserialize the JSON string back into our PersistedGameState object
            val loadedState = json.decodeFromString<PersistedGameState>(jsonString)

            // 4. Apply the loaded state to the actual services
            // Reset counters in the factories
            teamService.identifierFactory.resetCounter(loadedState.teamIdCounter)
            cardService.identifierFactory.resetCounter(loadedState.cardIdCounter)
            // Apply other loaded state fields here if needed (e.g., cardService.nextCardIndex = ...)

            // Restore collections: Clear existing data and add all loaded data
            TeamService.teamsStorage.clear()
            TeamService.teamsStorage.putAll(loadedState.teamsStorage)

            GameResultsService.gameHistory.clear()
            GameResultsService.gameHistory.addAll(loadedState.gameHistory)

            println("Game state loaded successfully.") // Log success

        } catch (e: IOException) {
            // Handle errors specifically related to file reading
            System.err.println("Error reading game state file '${saveFile.absolutePath}': ${e.message}. Starting fresh.")
        } catch (e: Exception) {
            // Handle errors during JSON parsing or applying the state (e.g., corrupted file)
            System.err.println("Error parsing or applying loaded game state: ${e.message}. Starting fresh.")
            e.printStackTrace() // Print stack trace for debugging
            // Optional: Consider deleting the corrupted file to prevent repeated errors on next start
            // saveFile.delete()
        }
    }
}
