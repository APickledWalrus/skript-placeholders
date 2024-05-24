package io.github.apickledwalrus.skriptplaceholders.placeholder;

/**
 * A placeholder listener is responsible for registering, unregistering, and handling placeholder requests.
 */
interface PlaceholderListener {

	/**
	 * Registers this listener with the providing plugin.
	 */
	void registerListener();

	/**
	 * Unregisters this listener with the providing plugin.
	 */
	void unregisterListener();

	/**
	 * Adds an evaluator to be used when trying to evaluate a placeholder.
	 * @param evaluator The evaluator to add.
	 */
	void addEvaluator(PlaceholderEvaluator evaluator);

	/**
	 * Removes an evaluator from this listener. It will no longer be used when trying to evaluate a placeholder.
	 * @param evaluator The evaluator to remove.
	 */
	void removeEvaluator(PlaceholderEvaluator evaluator);

	/**
	 * @return Whether this listener has any evaluators.
	 */
	boolean hasEvaluators();

}
