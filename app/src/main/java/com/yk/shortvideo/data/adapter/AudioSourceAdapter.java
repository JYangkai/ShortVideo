package com.yk.shortvideo.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.shortvideo.R;
import com.yk.shortvideo.data.bean.AudioSource;

import java.util.List;

public class AudioSourceAdapter extends RecyclerView.Adapter<AudioSourceAdapter.ViewHolder> {

    private Context context;

    private List<AudioSource> list;

    public AudioSourceAdapter(List<AudioSource> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_audio_source, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUseClickListener == null) {
                    return;
                }
                int position = holder.getAdapterPosition();
                AudioSource audioSource = list.get(position);
                onUseClickListener.onPlayClick(audioSource);
            }
        });
        holder.btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUseClickListener == null) {
                    return;
                }
                int position = holder.getAdapterPosition();
                AudioSource audioSource = list.get(position);
                onUseClickListener.onUseClick(audioSource);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioSource audioSource = list.get(position);
        holder.tvName.setText(audioSource.getAudio().getName());
        switch (audioSource.getState()) {
            case UNKNOWN:
                holder.btnUse.setText("未知");
                break;
            case CAN_USE:
                holder.btnUse.setText("使用");
                break;
            case NEED_TRANSCODE:
                holder.btnUse.setText("转码");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;
        AppCompatButton btnPlay;
        AppCompatButton btnUse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnUse = itemView.findViewById(R.id.btnUse);
        }
    }

    private OnUseClickListener onUseClickListener;

    public void setOnUseClickListener(OnUseClickListener onUseClickListener) {
        this.onUseClickListener = onUseClickListener;
    }

    public interface OnUseClickListener {
        void onUseClick(AudioSource source);

        void onPlayClick(AudioSource source);
    }
}
