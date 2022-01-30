package com.yk.markdown.core.render;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.bean.MdWord;
import com.yk.markdown.core.render.word.IMdWordRender;
import com.yk.markdown.core.render.word.MdBoldItalicsRender;
import com.yk.markdown.core.render.word.MdBoldRender;
import com.yk.markdown.core.render.word.MdCodeRender;
import com.yk.markdown.core.render.word.MdImageRender;
import com.yk.markdown.core.render.word.MdItalicsRender;
import com.yk.markdown.core.render.word.MdNormalRender;
import com.yk.markdown.style.style.BaseMdStyle;

/**
 * 渲染word
 */
public class MdWordRender {
    private static final String TAG = "MdWordRender";

    private final IMdWordRender normalRender = new MdNormalRender();
    private final IMdWordRender codeRender = new MdCodeRender();
    private final IMdWordRender boldItalicsRender = new MdBoldItalicsRender();
    private final IMdWordRender boldRender = new MdBoldRender();
    private final IMdWordRender italicsRender = new MdItalicsRender();
    private final IMdWordRender imageRender = new MdImageRender();

    /**
     * 渲染word
     */
    public SpannableStringBuilder render(Context context, MdWord word, BaseMdStyle style) {
        MdType type = word.getType();

        SpannableStringBuilder spanStrBuilder = new SpannableStringBuilder();

        Log.d(TAG, "render: " + word);

        switch (type) {
            case NORMAL:
                spanStrBuilder.append(normalRender.render(context, word, style));
                break;
            case CODE:
                spanStrBuilder.append(codeRender.render(context, word, style));
                break;
            case BOLD_ITALICS:
                spanStrBuilder.append(boldItalicsRender.render(context, word, style));
                break;
            case BOLD:
                spanStrBuilder.append(boldRender.render(context, word, style));
                break;
            case ITALICS:
                spanStrBuilder.append(italicsRender.render(context, word, style));
                break;
            case IMAGE:
                spanStrBuilder.append("\n").append(imageRender.render(context, word, style)).append("\n");
                break;
            default:
                spanStrBuilder.append(word.getSrc());
                break;
        }
        return spanStrBuilder;
    }
}
