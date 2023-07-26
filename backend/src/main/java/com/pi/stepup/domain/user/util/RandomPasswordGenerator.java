package com.pi.stepup.domain.user.util;

import java.util.Random;

public class RandomPasswordGenerator {

    private static final int PASSWORD_LENGTH = 8;
    private static final char[] characterTable = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
        'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public static String generateRandomPassword() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder buffer = new StringBuilder();

        do {
            buffer.setLength(0);
            for (int i = 0; i < PASSWORD_LENGTH; i++) {
                buffer.append(characterTable[random.nextInt(characterTable.length)]);
            }
        } while (!buffer.toString().matches("^.*(?=.*\\d)(?=.*[a-zA-Z]).*$"));

        return buffer.toString();
    }
}
