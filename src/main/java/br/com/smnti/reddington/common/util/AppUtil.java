package br.com.smnti.reddington.common.util;

import java.net.InetAddress;

public class AppUtil {

    public static String getServer() {
        try {
            return InetAddress.getLocalHost().toString();
        } catch (Exception var2) {
            return "unkown";
        }
    }

}
