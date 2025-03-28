package jetbrains.kotlin.course.alias

// === Add these imports ===
import jetbrains.kotlin.course.alias.card.CardService
import jetbrains.kotlin.course.alias.persistence.GameStatePersistenceService
import jetbrains.kotlin.course.alias.results.GameResultsService
import jetbrains.kotlin.course.alias.team.TeamService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext // Needed to get beans
// === End imports ===

@SpringBootApplication
class AliasApplication // Keep this

@Suppress("SpreadOperator") // Keep this if you need it
fun main(args: Array<String>) {
    // --- Capture the context returned by runApplication ---
    val context: ConfigurableApplicationContext = runApplication<AliasApplication>(*args)
    // --- End capture ---

    // === PASTE THIS BLOCK TO LOAD STATE AFTER STARTUP ===
    println("Application context initialized. Attempting to load game state...")
    try {
        // Get the instances of our services from the Spring context
        val persistenceService = context.getBean(GameStatePersistenceService::class.java)
        val teamService = context.getBean(TeamService::class.java)
        val cardService = context.getBean(CardService::class.java)
        val gameResultsService = context.getBean(GameResultsService::class.java)

        // Call the loading function in the persistence service
        persistenceService.loadAndApplyState(teamService, cardService, gameResultsService)

    } catch (e: Exception) {
        // Catch potential errors during bean retrieval or the loading process
        System.err.println("FATAL: Failed during initial game state loading: ${e.message}")
        e.printStackTrace()
        println("Continuing with potentially fresh state due to loading error.")
    }
    // === END PASTE ===

    println("Alias Application Started and ready.") // Add a final ready message
}
