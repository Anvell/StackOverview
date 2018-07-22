package io.github.anvell.stackoverview.extension;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class Utils {

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            return Html.fromHtml(text);
        }
    }

    public static boolean isNotBlank(String text) {
        boolean isNotBlank = false;
        for (char c : text.toCharArray()) {
            if(c != ' ') isNotBlank = true;
        }
        return isNotBlank;
    }

}