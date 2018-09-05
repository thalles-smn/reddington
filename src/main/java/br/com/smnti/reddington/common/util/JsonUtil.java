package br.com.smnti.reddington.common.util;

import java.util.Optional;

public class JsonUtil {

    public static String removeNewlineTabFromString(final String jsonString) {
        return Optional.ofNullable(jsonString)
                .filter((t) -> !t.isEmpty())
                .map((t) -> jsonString.replaceAll("\"", "").replaceAll("\n", ""))
                .orElse(null);
    }
}
