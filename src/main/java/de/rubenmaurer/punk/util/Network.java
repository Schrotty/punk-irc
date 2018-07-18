package de.rubenmaurer.punk.util;

import scala.Predef;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class Network {

    private static final Pattern IPV4 = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
    );

    public static String addressIsAvailable() {
        if (Settings.debug()) return Template.get("addressAvailable").toString();
        if (hasInterface(Settings.host())) {
            try (Socket ignored = new Socket(Settings.host(), Settings.port())) {
                return Template.get("addressInUse").toString();
            } catch (IOException ignored) {
                return Template.get("addressAvailable").toString();
            }
        }

        return Template.get("addressNotAvailable").toString();
    }

    public static String availableAddresses() {
        StringBuilder builder = new StringBuilder("[ ");

        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    String ip = inetAddress.toString().substring(1);

                    if (IPV4.matcher(ip).matches()) builder.append(String.format("%s, ", ip));
                }
            }
        } catch (SocketException ignored) {
            //nothing
        }

        return builder.deleteCharAt(builder.lastIndexOf(",")).append("]").toString();
    }

    private static boolean hasInterface(String address) {
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (inetAddress.toString().substring(1).equals(address)) return true;
                }
            }
        } catch (SocketException ignored) {
            //nothing
        }

        return false;
    }
}
