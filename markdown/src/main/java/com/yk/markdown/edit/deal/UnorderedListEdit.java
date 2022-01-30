package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.widget.EditText;

public class UnorderedListEdit extends BaseEdit {
    @Override
    public void dealEdit(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            curContent = "- ";
            curSelection = curContent.length();
        } else {
            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            if (beforeLineFeed + 1 != curSelection) {
                char headChar = curContent.charAt(beforeLineFeed + 1);
                if (headChar == '-') {
                    return;
                }
            }

            if (beforeLineFeed != -1) {
                String before = curContent.substring(0, beforeLineFeed + 1);
                String after = curContent.substring(beforeLineFeed + 1);
                curContent = before + "- " + after;
            } else {
                curContent = "- " + curContent;
            }

            curSelection = afterLineFeed != -1 ? (afterLineFeed + "- ".length()) : curContent.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
