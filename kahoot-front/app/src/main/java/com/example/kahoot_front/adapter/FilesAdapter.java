package com.example.kahoot_front.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kahoot_front.R;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private List<String> filesUrls;

    public FilesAdapter(List<String> filesUrls) {
        this.filesUrls = filesUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.fileImage.getContext())
                .load(filesUrls.get(position))
                .into(holder.fileImage);

        holder.deleteButton.setOnClickListener(v -> {
            filesUrls.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, filesUrls.size());
        });
    }

    @Override
    public int getItemCount() {
        return filesUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fileImage;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileImage = itemView.findViewById(R.id.fileImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

}