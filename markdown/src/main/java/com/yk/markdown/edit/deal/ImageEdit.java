package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.widget.EditText;

public class ImageEdit extends BaseEdit {

    @Override
    public void dealImageEdit(EditText et, String name, String path) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        String mdImage = "![" + name + "](" + path + ")";

        if (TextUtils.isEmpty(curContent)) {
            curContent = mdImage;
            curSelection = curContent.length();
        } else {
            String before = curContent.substring(0, curSelection);
            String after = curContent.substring(curSelection);
            curContent = before + mdImage + after;
            curSelection = before.length() + mdImage.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
