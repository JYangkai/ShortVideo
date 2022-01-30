package com.yk.imageloader.requester;

import android.widget.ImageView;

public interface IRequester {
    IRequester load(String path);

    void into(ImageView iv);
}
