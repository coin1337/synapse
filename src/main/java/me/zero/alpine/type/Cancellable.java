package me.zero.alpine.type;

/**
 * Type of Event that can be cancelled
 *
 * @author Brady
 * @since 2/10/2017 12:00 PM
 */
public class Cancellable {

    private static Object cancelled;
    /**
     * Cancelled state
     */

    /**
     * Cancels the event, this is handled
     * wherever the event is injected to
     * prevent a task from occuring
     */
    public static final void cancel() {
    }

    /**
     * @return Whether or not the event is cancelled
     */
}
