package com.yk.media.record.audio;

import com.yk.media.record.config.AudioEncodeConfig;
import com.yk.media.record.config.RecordConfig;

public interface IAudioRecord {

    void prepare(String path);

    void prepare(AudioEncodeConfig audioEncodeConfig, RecordConfig recordConfig);

    void startRecord();

    void stopRecord();

    void cancelRecord();

}
