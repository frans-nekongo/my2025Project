package jetbrains.kotlin.course.alias.util

// The typealias should already be here
typealias Identifier = Int

/**
 * Factory class responsible for generating unique identifiers ([Identifier]).
 * Allows setting an initial value and resetting the counter for persistence.
 */
class IdentifierFactory(initialValue: Int = 0) { // <-- Modified constructor accepts initial value

    // Counter is now a public var so it can be read externally (for saving)
    // and reset via resetCounter (for loading).
    // 'private set' could be used if you only want external reads and internal sets via resetCounter.
    var counter: Int = initialValue
        private set // Restrict setting the counter directly from outside, use resetCounter

    /**
     * Generates and returns a new unique identifier.
     * Increments the counter before returning.
     *
     * @return A new unique [Identifier].
     */
    fun uniqueIdentifier(): Identifier {
        counter += 1
        return counter
    }

    /**
     * Resets the counter to a specific value, typically used when loading state.
     * Ensures the counter doesn't go below zero.
     *
     * @param value The value to set the counter to.
     */
    fun resetCounter(value: Int) { // <-- New method for loading
        require(value >= 0) { "Counter cannot be set to a negative value." }
        counter = value
    }

    /**
     * Gets the current value of the counter, typically used when saving state.
     *
     * @return The current counter value.
     */
    fun getCurrentValue(): Int = counter // <-- New method for saving
}
