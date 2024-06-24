package com.myf.common.util;

import java.util.Random;

/**
 * @Author: myf
 * @CreateTime: 2024-05-19  13:36
 * @Description: InvitationCodeUtils
 */
public class InvitationCodeUtils {

    public static String getRandomLetters(boolean uppercase,Integer num) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < num; i++) {
            char letter;
            if (uppercase) {
                // 生成大写字母
                letter = (char) (random.nextInt(26) + 'A');
            } else {
                // 生成小写字母
                letter = (char) (random.nextInt(26) + 'a');
            }
            sb.append(letter);
        }

        return sb.toString();
    }

}
