package jetbrains.kotlin.course.alias.card

import jetbrains.kotlin.course.alias.util.Identifier // Import Identifier
import kotlinx.serialization.Serializable

/**
 * Represents a single word used on an Alias card.
 * Using a value class for type safety and potential performance benefits.
 *
 * @property word The actual word string.
 */
@Serializable // this ish is for serillization
@JvmInline
value class Word(val word: String)

/**
 * Represents a single card in the Alias game.
 * Each card has a unique ID and a list of words.
 *
 * @property id The unique identifier for the card.
 * @property words The list of words ([Word]) present on this card.
 */
@Serializable
data class Card(val id: Identifier, val words: List<Word>)
