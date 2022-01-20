package com.yk.media.record.listener;

import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;
import com.yk.media.record.config.VideoEncodeConfig;

public interface OnVideoRecordListener {

    void onRecordStart(
            AudioEncodeConfig audioEncodeConfig,
            VideoEncodeConfig videoEncodeConfig,
            RecordConfig recordConfig
    );

    void onRecording(long pts);

    void onRecordStop(RecordConfig recordConfig);

    void onRecordCancel();

    void onRecordError(Exception e);

}
