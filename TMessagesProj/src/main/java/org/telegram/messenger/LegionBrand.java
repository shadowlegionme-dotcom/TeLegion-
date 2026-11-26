/*
 * TeLegion (unofficial Telegram client), GNU GPL v. 2 or later. Added 2026-11-26.
 */
package org.telegram.messenger;

import java.util.regex.Pattern;

/**
 * App er nam bodlay. Telegram er service (Premium, Stars, account, server, Terms ityadi)
 * bujhale "Telegram" lekha thake, jate user vul na bojhe.
 */
public final class LegionBrand {

    private static final Pattern APP_NAME = Pattern.compile(
            "\\bTelegram\\b(?![\\s*\u00a0-]+(?i:premium|stars?|business|ads?|passport|gifts?|account|servers?|cloud|support|team|"
            + "moderators|terms|privacy|faq|desktop|web|api|features)\\b)(?!['\u2019]s\\b)(?!\\.[a-z])");

    public static String apply(String value) {
        if (value == null || value.indexOf("Telegram") < 0) {
            return value;
        }
        return APP_NAME.matcher(value).replaceAll("TeLegion");
    }
}
