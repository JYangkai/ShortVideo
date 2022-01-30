package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.widget.EditText;

public class QuoteEdit extends BaseEdit {
    @Override
    public void dealEdit(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            // 如果当前文本为空，则直接插入
            curContent = "> ";
            curSelection = curContent.length();
        } else {
            // 文本不为空，则需要判断光标位置
            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            // 如果当前行的头一个字符已标识，则直接返回
            if (beforeLineFeed + 1 != curSelection) {
                char headChar = curContent.charAt(beforeLineFeed + 1);
                if (headChar == '>') {
                    return;
                }
            }

            if (beforeLineFeed != -1) {
                // 如果上一个换行符存在，则在当前行头部插入
                String before = curContent.substring(0, beforeLineFeed + 1);
                String after = curContent.substring(beforeLineFeed + 1);
                curContent = before + "> " + after;
            } else {
                // 如果上一个换行符不存在，则直接插入
                curContent = "> " + curContent;
            }

            // 将光标移动到当前行的末尾
            curSelection = afterLineFeed != -1 ? (afterLineFeed + "> ".length()) : curContent.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
