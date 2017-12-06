package de.rubenmaurer.punk.core;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.io.Tcp;
import de.rubenmaurer.punk.core.reporter.Report;

public class PunkServer extends AbstractActor {

    public static Props props() {
        return Props.create(PunkServer.class);
    }

    @Override
    public void preStart() {
        context().actorOf(ConnectionManager.props(Tcp.get(getContext().getSystem()).manager()), "connection-manager");

        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
