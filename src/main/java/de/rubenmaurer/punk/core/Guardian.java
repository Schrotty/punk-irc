package de.rubenmaurer.punk.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.punk.Punk;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.core.reporter.Reporter;
import de.rubenmaurer.punk.util.Template;

public class Guardian extends AbstractActor {

    private ActorRef reporter;

    @Override
    public void preStart() {
        reporter = context().actorOf(Reporter.getProps());

        reporter.tell(Report.create(Report.Type.INFO, Template.get("startUp").single("version",
                Punk.class.getPackage().getImplementationVersion())), self());
        reporter.tell(Report.create(Report.Type.NONE, ""), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    public static Props getProps() {
        return Props.create(Guardian.class);
    }
}
