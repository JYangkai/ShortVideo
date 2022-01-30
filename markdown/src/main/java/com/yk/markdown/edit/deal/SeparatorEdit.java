package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.widget.EditText;

public class SeparatorEdit extends BaseEdit {
    @Override
    public void dealEdit(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            curContent = "---\n";
            curSelection = curContent.length();
        } else {
            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            if (afterLineFeed != -1) {
                String before = curContent.substring(0, afterLineFeed + 1);
                String after = curContent.substring(afterLineFeed + 1);
                curContent = before + "---\n" + after;
                curSelection = before.length() + "---\n".length();
            } else {
                curContent = curContent + "\n---\n";
                curSelection = curContent.length();
            }
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
