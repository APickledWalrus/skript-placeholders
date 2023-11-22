package io.github.apickledwalrus.skriptplaceholders.placeholder;

/**
 * A placeholder listener is responsible for registering, unregistering, and handling placeholder requests.
 */
public interface PlaceholderListener {

	/**
	 * Registers this listener with the providing plugin.
	 */
	void registerListener();

	/**
	 * Unregisters this listener with the providing plugin.
	 */
	void unregisterListener();

}
