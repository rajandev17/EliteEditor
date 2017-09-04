package com.rajan.eliteeditor.Elite;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.StrikethroughSpan;
import org.xml.sax.XMLReader;

/**
 * Created by rajan.kali on 8/31/2017.
 * Xml Handler
 */
public class EliteTagHandler implements Html.TagHandler {
    private static final String BULLET = "li";
    private static final String STRIKE_S = "s";
    private static final String STRIKE = "strike";
    private static final String STRIKE_DEL = "del";

    private static class Li {}
    private static class Strike {}

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (opening) {
            if (tag.equalsIgnoreCase(BULLET)) {
                if (output.length() > 0 && output.charAt(output.length() - 1) != '\n') {
                    output.append("\n");
                }
                start(output, new Li());
            } else if (tag.equalsIgnoreCase(STRIKE_S) || tag.equalsIgnoreCase(STRIKE) || tag.equalsIgnoreCase(STRIKE_DEL)) {
                start(output, new Strike());
            }
        } else {
            if (tag.equalsIgnoreCase(BULLET)) {
                if (output.length() > 0 && output.charAt(output.length() - 1) != '\n') {
                    output.append("\n");
                }
                end(output, Li.class, new BulletSpan());
            } else if (tag.equalsIgnoreCase(STRIKE_S) || tag.equalsIgnoreCase(STRIKE) || tag.equalsIgnoreCase(STRIKE_DEL)) {
                end(output, Strike.class, new StrikethroughSpan());
            }
        }
    }

    private void start(Editable output, Object mark) {
        output.setSpan(mark, output.length(), output.length(), Spanned.SPAN_MARK_MARK);
    }

    private void end(Editable output, Class kind, Object... replaces) {
        Object last = getLast(output, kind);
        int start = output.getSpanStart(last);
        int end = output.length();
        output.removeSpan(last);

        if (start != end) {
            for (Object replace : replaces) {
                output.setSpan(replace, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static Object getLast(Editable text, Class kind) {
        Object[] spans = text.getSpans(0, text.length(), kind);

        if (spans.length == 0) {
            return null;
        } else {
            for (int i = spans.length; i > 0; i--) {
                if (text.getSpanFlags(spans[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return spans[i - 1];
                }
            }

            return null;
        }
    }
}
