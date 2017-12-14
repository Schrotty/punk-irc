package de.rubenmaurer.punk.core.irc.channel;

import akka.actor.AbstractActor;
import akka.actor.Props;
import de.rubenmaurer.punk.core.reporter.Report;

import static de.rubenmaurer.punk.core.Guardian.reporter;

/**
 * Actor for managing channels.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ChannelManager extends AbstractActor {

    /**
     * Get needed props for creating a new actor.
     *
     * @return the props for creating
     */
    public static Props props() {
        return Props.create(ChannelManager.class);
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
