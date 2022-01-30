package com.yk.markdown.edit;

import android.widget.EditText;

import com.yk.markdown.bean.MdType;
import com.yk.markdown.edit.deal.BoldEdit;
import com.yk.markdown.edit.deal.BoldItalicsEdit;
import com.yk.markdown.edit.deal.CodeBlockEdit;
import com.yk.markdown.edit.deal.CodeEdit;
import com.yk.markdown.edit.deal.IEdit;
import com.yk.markdown.edit.deal.ImageEdit;
import com.yk.markdown.edit.deal.ItalicsEdit;
import com.yk.markdown.edit.deal.OrderedListEdit;
import com.yk.markdown.edit.deal.QuoteEdit;
import com.yk.markdown.edit.deal.SeparatorEdit;
import com.yk.markdown.edit.deal.TitleEdit;
import com.yk.markdown.edit.deal.UnorderedListEdit;

public class EditManager {
    private static volatile EditManager instance;

    private final IEdit quoteEdit;
    private final IEdit codeBlockEdit;
    private final IEdit orderedListEdit;
    private final IEdit unorderedListEdit;
    private final IEdit titleEdit;
    private final IEdit separatorEdit;
    private final IEdit codeEdit;
    private final IEdit boldEdit;
    private final IEdit italicsEdit;
    private final IEdit boldItalicsEdit;
    private final IEdit imageEdit;

    private EditManager() {
        quoteEdit = new QuoteEdit();
        codeBlockEdit = new CodeBlockEdit();
        orderedListEdit = new OrderedListEdit();
        unorderedListEdit = new UnorderedListEdit();
        titleEdit = new TitleEdit();
        separatorEdit = new SeparatorEdit();
        codeEdit = new CodeEdit();
        boldEdit = new BoldEdit();
        italicsEdit = new ItalicsEdit();
        boldItalicsEdit = new BoldItalicsEdit();
        imageEdit = new ImageEdit();
    }

    public static EditManager getInstance() {
        if (instance == null) {
            synchronized (EditManager.class) {
                if (instance == null) {
                    instance = new EditManager();
                }
            }
        }
        return instance;
    }

    public void insert(MdType type, EditText et) {
        IEdit edit = getEdit(type);

        if (edit == null) {
            return;
        }

        edit.dealEdit(et);
    }

    public void insertImage(EditText et, String name, String path) {
        imageEdit.dealImageEdit(et, name, path);
    }

    private IEdit getEdit(MdType type) {
        switch (type) {
            case QUOTE:
                return quoteEdit;
            case CODE_BLOCK:
                return codeBlockEdit;
            case ORDERED_LIST:
                return orderedListEdit;
            case UNORDERED_LIST:
                return unorderedListEdit;
            case TITLE:
                return titleEdit;
            case SEPARATOR:
                return separatorEdit;
            case CODE:
                return codeEdit;
            case BOLD:
                return boldEdit;
            case ITALICS:
                return italicsEdit;
            case BOLD_ITALICS:
                return boldItalicsEdit;
            default:
                break;
        }
        return null;
    }
}
