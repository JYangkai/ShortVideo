package com.yk.media.record.listener;

import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;

public interface OnAudioRecordListener {

    void onRecordStart(
            AudioEncodeConfig audioEncodeConfig,
            RecordConfig recordConfig
    );

    void onRecording(long pts);

    void onRecordStop(RecordConfig recordConfig);

    void onRecordCancel();

    void onRecordError(Exception e);

}
