package com.rajan.eliteeditor.Elite;

/**
 * Created by rajan.kali on 8/31/2017.
 * Holds Selection start and end
 */
class Selection {
    private int selectionStart;
    private int selectionEnd;

    Selection(int selectionStart, int selectionEnd) {
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    }

    int getSelectionStart() {
        return selectionStart;
    }

    int getSelectionEnd() {
        return selectionEnd;
    }

    boolean isValid() {
        return selectionStart < selectionEnd;
    }
}
