package jetbrains.kotlin.course.alias.team

import jetbrains.kotlin.course.alias.util.Identifier
import jetbrains.kotlin.course.alias.util.IdentifierFactory
import org.springframework.stereotype.Service

@Service
class TeamService {

    // 1. Added identifierFactory property
    val identifierFactory = IdentifierFactory()

    // 2. Added companion object with teamsStorage
    companion object {
        val teamsStorage: MutableMap<Identifier, Team> = mutableMapOf()
    }

    // 3. Implemented the generateTeamsForOneRound method
    fun generateTeamsForOneRound(teamsNumber: Int): List<Team> {
        // Basic validation
        require(teamsNumber > 0) { "Number of teams must be positive" }

        val newTeams = mutableListOf<Team>()
        repeat(teamsNumber) {
            val newId = identifierFactory.uniqueIdentifier()
            val newTeam = Team(newId) // Creates team with new ID and default points (0)
            teamsStorage[newId] = newTeam // Store the team in the central storage
            newTeams.add(newTeam) // Add to the list for this round
        }
        return newTeams // Return the list of teams created in this call
    }

    // Any other existing methods in TeamService would remain here
}
