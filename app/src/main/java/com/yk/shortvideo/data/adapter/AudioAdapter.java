package com.yk.shortvideo.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.media.source.Audio;
import com.yk.shortvideo.R;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    private Context context;

    private final List<Audio> list;

    private OnAudioItemClickListener onAudioItemClickListener;

    public AudioAdapter(List<Audio> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_audio, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAudioItemClickListener == null) {
                    return;
                }
                int pos = holder.getAdapterPosition();
                onAudioItemClickListener.onAudioItemClick(list.get(pos));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Audio audio = list.get(position);
        holder.tvName.setText(audio.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public void setOnAudioItemClickListener(OnAudioItemClickListener onAudioItemClickListener) {
        this.onAudioItemClickListener = onAudioItemClickListener;
    }

    public interface OnAudioItemClickListener {
        void onAudioItemClick(Audio audio);
    }
}
