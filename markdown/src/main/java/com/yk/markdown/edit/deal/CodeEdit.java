package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.widget.EditText;

public class CodeEdit extends BaseEdit {
    @Override
    public void dealEdit(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            curContent = "``";
            curSelection = 1;
        } else {
            String before = curContent.substring(0, curSelection);
            String after = curContent.substring(curSelection);
            curContent = before + "``" + after;
            curSelection = before.length() + 1;
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
