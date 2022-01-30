package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.widget.EditText;

public class CodeBlockEdit extends BaseEdit {
    @Override
    public void dealEdit(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            // 如果当前文本为空，则直接插入
            curContent = "```\n\n```";
            curSelection = curContent.length() - 4;
        } else {
            // 文本不为空，则需要判断光标位置
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

            curContent = before + "```\n" + curLine + "\n```" + after;

            curSelection = before.length() + "```\n".length() + curLine.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
