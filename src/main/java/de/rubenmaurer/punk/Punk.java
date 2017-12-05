package de.rubenmaurer.punk;

import akka.actor.ActorSystem;
import de.rubenmaurer.punk.core.Guardian;
import org.fusesource.jansi.AnsiConsole;

public class Punk {

    public static void main(String args[]) {
        AnsiConsole.systemInstall();

        ActorSystem.apply("punk-irc").actorOf(Guardian.getProps());
    }
}
