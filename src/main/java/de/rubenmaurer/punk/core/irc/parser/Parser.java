package de.rubenmaurer.punk.core.irc.parser;

import akka.actor.AbstractActor;
import akka.actor.Props;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Settings;
import de.rubenmaurer.punk.util.Template;

import java.util.HashMap;
import java.util.Map;

public class Parser extends AbstractActor {

    private Map<String, Boolean> workerPlan = new HashMap<>();

    private String worker = Settings.parseWorkerName(0);

    private void delegate(Message message) {
        if (workerPlan.size() != 0) {
            if (!workerPlan.get(worker)) {
                context().child(worker).get().tell(message, self());
                workerPlan.replace(worker, true);
                return;
            }

            for (Map.Entry<String, Boolean> entry : workerPlan.entrySet()) {
                if (!entry.getValue()) worker = entry.getKey();
            }

            delegate(message);
        }

        //TODO: Decide what to do (Exception or what?!)
    }

    public static Props props() {
        return Props.create(Parser.class);
    }

    @Override
    public void preStart() {
        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    private void startWorker() {
        Guardian.reporter().tell(Report.create(Report.Type.NONE, ""), self());
        Guardian.reporter().tell(Report.create(Report.Type.INFO, Template.get("startSystem").single("system", "parser worker")), self());

        for (int i = 0; i < Settings.parseWorker(); i++) {
            String name = Settings.parseWorkerName(i);
            workerPlan.put(name, false);
            context().watch(context().actorOf(Worker.props(), name));
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, this::delegate)
                .matchEquals(Template.get("parserWorkStart").toString(), s -> startWorker())
                .match(String.class, s -> workerPlan.replace(s, false))
                .build();
    }
}
