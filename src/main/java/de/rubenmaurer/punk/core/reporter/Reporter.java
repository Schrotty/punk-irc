package de.rubenmaurer.punk.core.reporter;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * Actor for printing system messages.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Reporter extends AbstractActor {

    /**
     * ANSI reset code.
     */
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * ANSI code for red.
     */
    private static final String ANSI_RED = "\u001B[31m";

    /**
     * ANSI code for green.
     */
    private static final String ANSI_GREEN = "\u001B[32m";

    /**
     * ANSI code for blue.
     */
    private static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Get properties for a new actor.
     *
     * @return a {@link akka.actor.Props} object.
     */
    public static Props getProps() {
        return Props.create(Reporter.class);
    }

    /**
     * Build new message from sender ref ans message string.
     *
     * @param message the message to print
     * @param type the report type
     * @param sender the report sender
     * @return the final message
     */
    private String messageBuilder(String message, Report.Type type, ActorRef sender) {
        String color = ANSI_BLUE;
        String msgType;

        if (type == Report.Type.ONLINE || type == Report.Type.SUCCESS) {
            color = ANSI_GREEN;
        }

        if (type == Report.Type.OFFLINE || type == Report.Type.ERROR) {
            color = ANSI_RED;
        }

        msgType = String.format("[%s %s %s]", color, type, ANSI_RESET);
        return String.format("%s %s", type == Report.Type.NONE ? "" : msgType, message == null ? sender : message);
    }

    /**
     * Print a message on stdin.
     *
     * @param message the message to print
     */
    private void print(String message) {
        System.out.println(String.format(">> %s", message));
    }

    /**
     * Process a message.
     *
     * @param message the message to process
     */
    private void processMessage(Report message) {
        print(messageBuilder(message.getMessage(), message.getType(), sender()));
    }

    /**
     * Receives a message an process it.
     *
     * @return a Receive object
     */
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Report.class, this::processMessage)
                .build();
    }
}

