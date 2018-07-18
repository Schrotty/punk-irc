package de.rubenmaurer.punk.core.irc.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.irc.messages.MessageBuilder;
import de.rubenmaurer.punk.core.irc.messages.impl.WhoIs;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Settings;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;

/**
 * Actor for managing all established connections.
 *
 * @author Ruben Maurer
 * @version 1.2
 * @since 1.0
 */
public class ConnectionManager extends AbstractActor {

    /**
     * The used tcp-manager.
     */
    private final ActorRef manager;

    /**
     * List of all established connections.
     */
    static HashMap<String ,ActorRef> connections = new HashMap<>();

    /**
     * Create a new connection-manager object.
     *
     * @param manager the tcp-manager to use
     */
    public ConnectionManager(ActorRef manager) {
        this.manager = manager;
    }

    /**
     * Get the props needed for creating a new actor.
     *
     * @param manager the tcp-manager to use
     * @return the props for creating new actor
     */
    public static Props props(ActorRef manager) {
        return Props.create(ConnectionManager.class, manager);
    }

    /**
     * Server has connection with given nickname?
     *
     * @param nickname the given nickname
     * @return has connection with nickname?
     */
    public static boolean hasNickname(String nickname) {
        return connections.get(nickname) != null;
    }

    /**
     * Replies to a whoIs message.
     *
     * @param message the whoIs message
     */
    private void whoIs(WhoIs message) {
        try {
            if (hasNickname(message.getNickname())) {
                if (connections.get(message.getNickname()) != null) {
                    CompletableFuture<Object> result = ask(sender(), 42, 1000).toCompletableFuture();

                    sender().tell(MessageBuilder.whoIs(message.getNickname(), result.get().toString()), self());
                    return;
                }
            }
        } catch (Exception exception) {
            Guardian.reporter().tell(Report.create(Report.Type.ERROR, exception.getMessage()), self());
        }

        sender().tell(MessageBuilder.whoIs("", "",
                Notification.errNoSuchNick(Settings.hostname(), message.getNickname())), self());
    }

    /**
     * Gets fired before actor startup.
     */
    @Override
    public void preStart() {
        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());

        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.bind(getSelf(), new InetSocketAddress(Settings.host(), Settings.port()), 100), getSelf());
    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Bound.class, msg -> manager.tell(msg, getSelf()))
                .match(Tcp.CommandFailed.class, msg -> getContext().stop(getSelf()))
                .match(Tcp.Connected.class, conn -> {
                    manager.tell(conn, getSelf());
                    getSender().tell(TcpMessage.register(getContext().actorOf(ConnectionHandler.props(conn.remoteAddress().getHostName()))), getSelf());
                })
                .match(WhoIs.class, this::whoIs)
                .build();
    }
}
