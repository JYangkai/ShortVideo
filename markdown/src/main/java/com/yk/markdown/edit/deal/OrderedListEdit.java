package com.yk.markdown.edit.deal;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

public class OrderedListEdit extends BaseEdit {
    private static final String TAG = "OrderedListEdit";

    @Override
    public void dealEdit(EditText et) {
        String curContent = getContent(et);
        int curSelection = getSelection(et);

        if (TextUtils.isEmpty(curContent)) {
            // 如果当前文本为空，则直接插入
            curContent = "1. ";
            curSelection = curContent.length();
        } else {
            // 文本不为空，则需要判断光标位置
            int beforeLineFeed = getBeforeLineFeed(curContent, curSelection); // 上一个换行符
            int afterLineFeed = getAfterLineFeed(curContent, curSelection); // 下一个换行符

            Log.d(TAG, "dealEdit: beforeLineFeed:" + beforeLineFeed + " afterLineFeed:" + afterLineFeed + " selection:" + curSelection);

            if (beforeLineFeed + 1 != curSelection) {
                // 如果当前行的头一个字符已标识，则直接返回
                String headStr = curContent.substring(beforeLineFeed + 1, beforeLineFeed + 2);
                if (headStr.matches("\\d")) {
                    return;
                }
            }

            String numStr;
            if (beforeLineFeed != -1) {
                // 如果上一个换行符存在，则在当前行头部插入
                String before = curContent.substring(0, beforeLineFeed + 1);
                String after = curContent.substring(beforeLineFeed + 1);
                numStr = (getBeforeNum(before) + 1) + ". ";
                curContent = before + numStr + after;
            } else {
                // 如果上一个换行符不存在，则直接插入
                numStr = "1. ";
                curContent = numStr + curContent;
            }

            // 将光标移动到当前行的末尾
            curSelection = afterLineFeed != -1 ? (afterLineFeed + numStr.length()) : curContent.length();
        }

        et.setText(curContent);
        et.setSelection(curSelection);
    }
}
