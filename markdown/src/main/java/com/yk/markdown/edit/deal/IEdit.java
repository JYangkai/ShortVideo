package com.yk.markdown.edit.deal;

import android.widget.EditText;

public interface IEdit {

    void dealEdit(EditText et);

    void dealImageEdit(EditText et, String name, String path);

}
