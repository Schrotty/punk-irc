package de.rubenmaurer.punk.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.punk.Punk;
import de.rubenmaurer.punk.core.irc.PunkServer;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.core.reporter.Reporter;
import de.rubenmaurer.punk.util.Template;

/**
 * The root guardian for monitoring the system.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Guardian extends AbstractActor {

    /**
     * The reporter for system messages.
     */
    private static ActorRef reporter;

    /**
     * Get the reporter.
     *
     * @return the reporter
     */
    public static ActorRef reporter() {
        return reporter;
    }

    /**
     * Get the props needed for a new actor.
     *
     * @return the props
     */
    public static Props props() {
        return Props.create(Guardian.class);
    }

    /**
     * Gets fired before start.
     */
    @Override
    public void preStart() {
        reporter = context().actorOf(Reporter.getProps());

        reporter.tell(Report.create(Report.Type.INFO, Template.get("startUp").single("version",
                Punk.class.getPackage().getImplementationVersion())), self());
        reporter.tell(Report.create(Report.Type.NONE, ""), self());

        reporter().tell(Report.create(Report.Type.INFO, Template.get("startSystem").single("system", "guardian actor")), self());
        reporter().tell(Report.create(Report.Type.ONLINE), self());
        reporter().tell(Report.create(Report.Type.NONE, ""), self());

        reporter().tell(Report.create(Report.Type.INFO, Template.get("startSystem").single("system", "system/ manager actors")), self());
        context().actorOf(PunkServer.props(), "punk-irc-server");
    }

    /**
     * Receives messages and process them.
     *
     * @return a Receive object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
