package jetbrains.kotlin.course.alias.card

// === Added Imports ===
import jetbrains.kotlin.course.alias.util.IdentifierFactory
import jetbrains.kotlin.course.alias.util.words // Assuming 'words' is imported from here
import org.springframework.stereotype.Service

// === 4. Moved and Implemented toWords() Extension Function ===
// Defined outside the class for better scope management
fun List<String>.toWords(): List<Word> = this.map { Word(it) }

@Service
class CardService {

    // === 1. Added identifierFactory property ===
    val identifierFactory = IdentifierFactory()

    // === 3. Added companion object ===
    companion object {
        const val WORDS_IN_CARD: Int = 4
        // Assumes 'words' is accessible (imported)
        val cardsAmount = words.size / WORDS_IN_CARD
    }

    // === 2. Added cards property with lazy initialization ===
    val cards: List<Card> by lazy { generateCards() }

    // === 5. Implemented generateCards() ===
    // Kept private as it's mainly for initialization
    private fun generateCards(): List<Card> {
        return words
            .shuffled() // Randomize word order
            .toWords() // Convert strings to Word objects
            .chunked(WORDS_IN_CARD) // Group into chunks for cards
            .take(cardsAmount) // Limit to the calculated number of cards
            .map { cardWords -> // Create a Card for each chunk
                Card(identifierFactory.uniqueIdentifier(), cardWords)
            }
    }

    // Note: The original private fun List<String>.toWords() is removed as it's now a top-level function.

    // === 6. Implemented getCardByIndex() ===
    fun getCardByIndex(index: Int): Card {
        require(index in cards.indices) {
            "Index $index is out of bounds for cards list with size ${cards.size}"
        }
        return cards[index]
    }
}
