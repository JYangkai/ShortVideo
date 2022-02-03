package com.yk.media.transcode.listener;

import com.yk.media.transcode.bean.DecodeAudioResult;
import com.yk.media.transcode.bean.EncodeResult;

public interface OnAudioTranscodeListener {
    void onAudioTranscodeStart();

    void onAudioTranscoding(long progress);

    void onAudioTranscodeStop(DecodeAudioResult decodeAudioResult, EncodeResult encodeResult);

    void onAudioTranscodeCancel();

    void onAudioTranscodeError(Exception e);
}
