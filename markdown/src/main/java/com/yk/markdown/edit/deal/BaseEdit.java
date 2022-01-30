package com.yk.markdown.edit.deal;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

public abstract class BaseEdit implements IEdit {

    @Override
    public void dealEdit(EditText et) {

    }

    @Override
    public void dealImageEdit(EditText et, String name, String path) {

    }

    public String getContent(EditText et) {
        Editable editable = et.getText();

        if (editable == null) {
            return "";
        }

        return editable.toString();
    }

    public int getSelection(EditText et) {
        return et.getSelectionStart();
    }

    public int getBeforeLineFeed(String content, int selection) {
        String before = content.substring(0, selection);
        return before.lastIndexOf("\n");
    }

    public int getAfterLineFeed(String content, int selection) {
        String before = content.substring(selection);
        int index = before.indexOf("\n");
        return index != -1 ? (index + selection) : -1;
    }

    public int getBeforeNum(String content) {
        if (TextUtils.isEmpty(content)) {
            return 0;
        }

        String[] split = content.split("\n");
        if (split == null) {
            return 0;
        }
        if (split.length == 0) {
            return 0;
        }

        String num = split[split.length - 1].substring(0, 1);
        if (!num.matches("\\d")) {
            return 0;
        }

        return Integer.parseInt(num);
    }
}
