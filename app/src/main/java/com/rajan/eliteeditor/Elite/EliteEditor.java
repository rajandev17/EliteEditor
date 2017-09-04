package com.rajan.eliteeditor.Elite;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajan.kali on 8/31/2017.
 * Elite Editor Rich Text
 */
public class EliteEditor extends AppCompatEditText {

    public static final int BOLD = 1;
    public static final int ITALIC = 2;

   //Constructors
    public EliteEditor(Context context) {
        super(context);
    }

    public EliteEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EliteEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //Methods for modifying Style
    public void modifyBold() {
        if (!hasStyle(BOLD)) {
            applyStyle(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
        } else {
            clearStyle(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
        }
    }
    
    public void modifyItalic() {
        if (!hasStyle(ITALIC)) {
            applyStyle(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
        } else {
            clearStyle(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
        }
    }

    public void modifyUnderline() {
        if (!hasUnderline()) {
            underlineText(getSelectionStart(), getSelectionEnd());
        } else {
            clearUnderline(getSelectionStart(), getSelectionEnd());
        }
    }

    public void modifyStrike() {
        if (!hasStrike()) {
            strike(getSelectionStart(), getSelectionEnd());
        } else {
            clearStrike(getSelectionStart(), getSelectionEnd());
        }
    }

    public void modifyBullet() {
        if (!hasBullet()) {
            addBullet();
        } else {
            removeBullet();
        }
    }

    public void modifyQuote() {
        if (!hasQuote()) {
            quoteText();
        } else {
            removeQuote();
        }
    }

    public void modifyLink(String link) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        if (link != null && !TextUtils.isEmpty(link.trim())) {
            link(link, start, end);
        } else {
            unlink(start, end);
        }
    }

    //invalidates all the styles
    public void invalidateStyle() {
        setText(getEditableText().toString());
        setSelection(getEditableText().length());
    }

    //gets Active Link
    public String getSelectedLink(){
        URLSpan[] spans = getEditableText().getSpans(getSelectionStart(), getSelectionEnd(), URLSpan.class);
        return spans.length > 0 ? spans[0].getURL() : "";
    }


    // Apply styles for selection
    private void applyStyle(int style,int start, int end) {
        if(start > end) return;
        
        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return;
        }
        getEditableText().setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void underlineText(int start, int end) {
        if (start >= end) {
            return;
        }
        getEditableText().setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void link(String link, int start, int end) {
        if (start >= end) {
            return;
        }
        unlink(start, end);
        getEditableText().setSpan(new URLSpan(link), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void quoteText() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (hasQuote(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1; // \n
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            int quoteStart = 0;
            int quoteEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            }

            if (quoteStart < quoteEnd) {
                getEditableText().setSpan(new QuoteSpan(), quoteStart, quoteEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void strike(int start, int end) {
        if (start >= end) {
            return;
        }

        getEditableText().setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void addBullet() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (hasBullet(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1; // \n
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart > lineEnd) {
                continue;
            }

            // Find selection area inside
            int bulletStart = 0;
            int bulletEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            }

            if (bulletStart < bulletEnd) {
                getEditableText().setSpan(new EliteBulletSpan(), bulletStart, bulletEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    //clear styles for selection
    private void removeBullet() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (!hasBullet(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            int bulletStart = 0;
            int bulletEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            }

            if (bulletStart < bulletEnd) {
                BulletSpan[] spans = getEditableText().getSpans(bulletStart, bulletEnd, EliteBulletSpan.class);
                for (BulletSpan span : spans) {
                    getEditableText().removeSpan(span);
                }
            }
        }
    }

    private void clearStrike(int start, int end) {
        if (start >= end) {
            return;
        }

        StrikethroughSpan[] spans = getEditableText().getSpans(start, end, StrikethroughSpan.class);
        List<Selection> list = new ArrayList<>();

        for (StrikethroughSpan span : spans) {
            list.add(new Selection(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (Selection part : list) {
            if (part.isValid()) {
                if (part.getSelectionStart() < start) {
                    strike(part.getSelectionStart(), start);
                }

                if (part.getSelectionEnd() > end) {
                    strike(end, part.getSelectionEnd());
                }
            }
        }
    }

    private void clearStyle(int style,int start,int end) {
        if(start > end) return;

        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return;
        }

        StyleSpan[] spans = getEditableText().getSpans(start, end, StyleSpan.class);
        List<Selection> list = new ArrayList<>();

        for (StyleSpan span : spans) {
            if (span.getStyle() == style) {
                list.add(new Selection(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
                getEditableText().removeSpan(span);
            }
        }

        for (Selection selection : list) {
            if (selection.isValid()) {
                if (selection.getSelectionStart() < start) {
                    applyStyle(style, selection.getSelectionStart(), start);
                }

                if (selection.getSelectionEnd() > end) {
                    applyStyle(style, end, selection.getSelectionEnd());
                }
            }
        }
    }

    private void clearUnderline(int start, int end) {
        if (start >= end) {
            return;
        }

        UnderlineSpan[] spans = getEditableText().getSpans(start, end, UnderlineSpan.class);
        List<Selection> list = new ArrayList<>();

        for (UnderlineSpan span : spans) {
            list.add(new Selection(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (Selection part : list) {
            if (part.isValid()) {
                if (part.getSelectionStart() < start) {
                    underlineText(part.getSelectionStart(), start);
                }

                if (part.getSelectionEnd() > end) {
                    underlineText(end, part.getSelectionEnd());
                }
            }
        }
    }

    private boolean hasBullet(int index) {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        if (index < 0 || index >= lines.length) {
            return false;
        }

        int start = 0;
        for (int i = 0; i < index; i++) {
            start = start + lines[i].length() + 1;
        }

        int end = start + lines[index].length();
        if (start >= end) {
            return false;
        }

        BulletSpan[] spans = getEditableText().getSpans(start, end, EliteBulletSpan.class);
        return spans.length > 0;
    }

    private void removeQuote() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (!hasQuote(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            int quoteStart = 0;
            int quoteEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            }

            if (quoteStart < quoteEnd) {
                QuoteSpan[] spans = getEditableText().getSpans(quoteStart, quoteEnd, QuoteSpan.class);
                for (QuoteSpan span : spans) {
                    getEditableText().removeSpan(span);
                }
            }
        }
    }

    private void unlink(int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }

        URLSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, URLSpan.class);
        for (URLSpan span : spans) {
            getEditableText().removeSpan(span);
        }
    }

    //Verify existing styles for selection

    public boolean hasQuote() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                list.add(i);
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                list.add(i);
            }
        }

        for (Integer i : list) {
            if (!hasQuote(i)) {
                return false;
            }
        }

        return list.size() > 0;
    }

    private boolean hasStyle(int style) {
        if (getSelectionStart() > getSelectionEnd())  return false;

        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return false;
        }

        if (getSelectionStart() == getSelectionEnd()) {
            if (getSelectionStart() - 1 < 0 || getSelectionEnd() + 1 > getEditableText().length()) {
                return false;
            } else {
                StyleSpan[] before = getEditableText().getSpans(getSelectionStart() - 1, getSelectionEnd(), StyleSpan.class);
                StyleSpan[] after = getEditableText().getSpans(getSelectionStart(), getSelectionEnd() + 1, StyleSpan.class);
                return before.length > 0 && after.length > 0 && before[0].getStyle() == style && after[0].getStyle() == style;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = getSelectionStart(); i < getSelectionEnd(); i++) {
                StyleSpan[] spans = getEditableText().getSpans(i, i + 1, StyleSpan.class);
                for (StyleSpan span : spans) {
                    if (span.getStyle() == style) {
                        builder.append(getEditableText().subSequence(i, i + 1).toString());
                        break;
                    }
                }
            }

            return getEditableText().subSequence(getSelectionStart(), getSelectionEnd()).toString().equals(builder.toString());
        }
    }

    private boolean hasStrike() {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                StrikethroughSpan[] before = getEditableText().getSpans(start - 1, start, StrikethroughSpan.class);
                StrikethroughSpan[] after = getEditableText().getSpans(start, start + 1, StrikethroughSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();

            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, StrikethroughSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }

            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    private boolean hasUnderline() {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                UnderlineSpan[] before = getEditableText().getSpans(start - 1, start, UnderlineSpan.class);
                UnderlineSpan[] after = getEditableText().getSpans(start, start + 1, UnderlineSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();

            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, UnderlineSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }

            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    public boolean hasQuote(int index) {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        if (index < 0 || index >= lines.length) {
            return false;
        }

        int start = 0;
        for (int i = 0; i < index; i++) {
            start = start + lines[i].length() + 1;
        }

        int end = start + lines[index].length();
        if (start >= end) {
            return false;
        }

        QuoteSpan[] spans = getEditableText().getSpans(start, end, QuoteSpan.class);
        return spans.length > 0;
    }

    public boolean hasBullet() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length() + 1;
            if (lineStart >= lineEnd) {
                continue;
            }

            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                list.add(i);
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                list.add(i);
            }
        }

        for (Integer i : list) {
            if (!hasBullet(i)) {
                return false;
            }
        }

        return lines.length > 0;
    }
}
