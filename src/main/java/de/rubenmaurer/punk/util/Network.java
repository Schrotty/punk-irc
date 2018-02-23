package de.rubenmaurer.punk.util;

import java.io.IOException;
import java.net.Socket;

public class Network {
    public static boolean addressIsAvailable() {
        try (Socket ignored = new Socket("localhost", Settings.port())) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }
}
