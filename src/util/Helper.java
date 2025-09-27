package util;

import java.util.UUID;

public class Helper {
    public static String generateAbonnementId() {
       return UUID.randomUUID().toString();
    }
}
