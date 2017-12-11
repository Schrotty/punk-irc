package de.rubenmaurer.punk.core.irc.parser;

import akka.actor.AbstractActor;
import akka.actor.Props;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Settings;

import java.util.HashMap;
import java.util.Map;

public class Parser extends AbstractActor {

    private Map<String, Boolean> workerPlan = new HashMap<>();

    private String worker = Settings.parseWorkerName(0);

    private void delegate(Message message) {
        if (!workerPlan.get(worker)) {
            context().child(worker).get().tell(message, self());
            workerPlan.replace(worker, true);
            return;
        }

        for(Map.Entry<String, Boolean> entry : workerPlan.entrySet()) {
            if (!entry.getValue()) worker = entry.getKey();
        }

        delegate(message);
    }

    public static Props props() {
        return Props.create(Parser.class);
    }

    @Override
    public void preStart() {
        for (int i = 0; i < Settings.parseWorker(); i++) {
            String name = Settings.parseWorkerName(i);
            workerPlan.put(name, false);
            context().watch(context().actorOf(Worker.props(), name));
        }

        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, this::delegate)
                .match(String.class, s -> workerPlan.replace(s, false))
                .build();
    }
}
