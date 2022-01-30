package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.widget.EditText;

public class TitleEdit extends BaseEdit {
    @Override
    public void dealEdit(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            curContent = "# ";
            curSelection = curContent.length();
        } else {
            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            String before = "";
            if (beforeLineFeed != -1) {
                before = curContent.substring(0, beforeLineFeed + 1);
            }

            String after = "";
            if (afterLineFeed != -1) {
                after = curContent.substring(afterLineFeed);
            }

            String curLine = curContent.substring(beforeLineFeed + 1, afterLineFeed != -1 ? afterLineFeed : curContent.length());

            boolean isHeadMark = (curLine.charAt(0) == '#');
            if (isHeadMark) {
                curLine = "#" + curLine;
            } else {
                curLine = "# " + curLine;
            }

            curContent = before + curLine + after;
            curSelection = before.length() + curLine.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
