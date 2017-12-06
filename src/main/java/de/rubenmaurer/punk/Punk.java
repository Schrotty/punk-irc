package de.rubenmaurer.punk;

import akka.actor.ActorSystem;
import de.rubenmaurer.punk.core.Guardian;
import org.fusesource.jansi.AnsiConsole;

/**
 * PunkIRC Server.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Punk {

    /**
     * The main entrance point of this application.
     *
     * @param args start params
     */
    public static void main(String args[]) {
        AnsiConsole.systemInstall();

        ActorSystem.apply("punk-irc").actorOf(Guardian.props());
    }
}
