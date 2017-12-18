package de.rubenmaurer.punk.core.irc.parser;

import akka.actor.AbstractActor;
import akka.actor.Props;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.irc.messages.ParseMessage;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Settings;
import de.rubenmaurer.punk.util.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Actor for managing all the parser worker.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Parser extends AbstractActor {

    /**
     * Work plan of with all parser workers.
     */
    private Map<String, Boolean> workerPlan = new HashMap<>();

    /**
     * Worker name.
     */
    private String worker = Settings.parseWorkerName(0);

    /**
     * Delegate an incoming message to a worker.
     *
     * @param message the incoming message
     */
    private void delegate(ParseMessage message) {
        if (workerPlan.size() != 0) {
            if (!workerPlan.get(worker)) {
                context().child(worker).get().tell(message, self());
                workerPlan.replace(worker, true);
                return;
            }

            for (Map.Entry<String, Boolean> entry : workerPlan.entrySet()) {
                if (!entry.getValue()) {
                    worker = entry.getKey();
                    return;
                }
            }

            delegate(message);
        }

        //TODO: Decide what to do (Exception or what?!)
    }

    /**
     * Get the props for this actor.
     *
     * @return the props
     */
    public static Props props() {
        return Props.create(Parser.class);
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Start the parser worker.
     */
    private void startWorker() {
        Guardian.reporter().tell(Report.create(Report.Type.NONE, ""), self());
        Guardian.reporter().tell(Report.create(Report.Type.INFO, Template.get("startSystem").single("system", "parser worker")), self());

        for (int i = 0; i < Settings.parseWorker(); i++) {
            String name = Settings.parseWorkerName(i);
            workerPlan.put(name, false);
            context().watch(context().actorOf(Worker.props(), name));
        }
    }

    /**
     * Receives and processes messages.
     *
     * @return a receive object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ParseMessage.class, this::delegate)
                .matchEquals(Template.get("parserWorkStart").toString(), s -> startWorker())
                .match(String.class, s -> workerPlan.replace(s, false))
                .build();
    }
}
