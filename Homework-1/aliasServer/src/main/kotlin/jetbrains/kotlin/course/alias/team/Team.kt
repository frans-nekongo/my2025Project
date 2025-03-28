package jetbrains.kotlin.course.alias.team

// Import the Identifier type alias we created in the previous step
import jetbrains.kotlin.course.alias.util.Identifier
import kotlinx.serialization.Serializable

/**
 * Represents a team playing the Alias game.
 *
 * @property id The unique identifier for the team.
 * @property points The current score of the team, defaults to 0.
 * @property name The display name of the team, automatically generated based on the id.
 */
@Serializable
data class Team(val id: Identifier, var points: Int = 0) {
    // This property is initialized when a Team object is created,
    // using the 'id' from the primary constructor.

    @Transient // <-- PASTE THIS ANNOTATION (to ignore 'name' during serialization)
    val name: String = "Team#${id + 1}"
}
