package domain.logic;

/**
 * Defines a common interface for tagging entities with a display name.
 * This interface is intended to be implemented by enums or classes
 * that categorize or provide additional descriptive information about
 * objects within a domain, such as food freshness levels or food group categories.
 *
 * Implementing this interface allows objects to be tagged with readable,
 * human-friendly names that can be displayed in UIs or used for logging purposes.
 */
public interface Tag {

    /**
     * Retrieves the display name of the tag. The display name is intended
     * to provide a human-readable name for the tag, suitable for display
     * in user interfaces or for use in logging and reporting.
     *
     * @return A {@code String} representing the display name of the tag.
     */
    String getDisplayName();
}
