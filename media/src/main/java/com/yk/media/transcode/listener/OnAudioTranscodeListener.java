package com.yk.media.transcode.listener;

import com.yk.media.transcode.bean.DecodeResult;
import com.yk.media.transcode.bean.EncodeResult;

public interface OnAudioTranscodeListener {
    void onAudioTranscodeStart();

    void onAudioTranscoding(long progress);

    void onAudioTranscodeStop(DecodeResult decodeResult, EncodeResult encodeResult);

    void onAudioTranscodeCancel();

    void onAudioTranscodeError(Exception e);
}
