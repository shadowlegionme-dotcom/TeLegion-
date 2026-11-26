/*
 * TeLegion (unofficial Telegram client), GNU GPL v. 2 or later. Added 2026-11-26.
 */
package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** Hidden chat: ei device e, account onujayi. Server e kichu bodlay na. */
public final class LegionHidden {

    private static volatile boolean revealed;

    private static SharedPreferences prefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences("legion_hidden", Context.MODE_PRIVATE);
    }

    private static String key(int account) {
        return "u_" + UserConfig.getInstance(account).getClientUserId();
    }

    private static Set<String> hidden(int account) {
        return prefs().getStringSet(key(account), new HashSet<>());
    }

    public static boolean isHidden(int account, long dialogId) {
        return hidden(account).contains(String.valueOf(dialogId));
    }

    public static void toggle(int account, long dialogId) {
        HashSet<String> set = new HashSet<>(hidden(account));
        String id = String.valueOf(dialogId);
        if (!set.remove(id)) {
            set.add(id);
        }
        prefs().edit().putStringSet(key(account), set).apply();
        reload(account);
    }

    public static boolean isRevealed() {
        return revealed;
    }

    public static void setRevealed(boolean value) {
        if (revealed == value) {
            return;
        }
        revealed = value;
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                reload(a);
            }
        }
    }

    public static ArrayList<TLRPC.Dialog> filter(int account, ArrayList<TLRPC.Dialog> dialogs) {
        if (revealed || dialogs == null) {
            return dialogs;
        }
        Set<String> set = hidden(account);
        if (set.isEmpty()) {
            return dialogs;
        }
        ArrayList<TLRPC.Dialog> result = new ArrayList<>(dialogs.size());
        for (int i = 0; i < dialogs.size(); i++) {
            TLRPC.Dialog dialog = dialogs.get(i);
            if (!set.contains(String.valueOf(dialog.id))) {
                result.add(dialog);
            }
        }
        return result;
    }

    private static void reload(int account) {
        AndroidUtilities.runOnUIThread(() ->
                NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.dialogsNeedReload));
    }
}
