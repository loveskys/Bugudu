package com.lyh.util;

import java.util.Random;

public class ComUtil {

    public static String gAlphanumeric() {
        Random random = new Random();
        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            numbers.append(random.nextInt(10)); // 生成0到9之间的随机数
        }
        char letter;
        if (random.nextBoolean()) {
            letter = (char) ('A' + random.nextInt(26));
        } else {
            letter = (char) ('a' + random.nextInt(26));
        }
        int position = random.nextInt(6);
        numbers.insert(position, letter);
        return numbers.toString();
    }

}
