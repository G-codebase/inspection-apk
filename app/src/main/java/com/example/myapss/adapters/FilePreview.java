// FilePreviewAdapter.java
package com.example.myapss.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapss.R;
import com.example.myapss.models.FileUploadModel;

import java.io.InputStream;
import java.util.List;

public class FilePreview extends RecyclerView.Adapter<FilePreview.ViewHolder> {

    private Context context;
    private List<FileUploadModel> files;
    private OnFileRemoveListener removeListener;

    public interface OnFileRemoveListener {
        void onRemove(String fieldName, FileUploadModel file);
    }

    public FilePreview(Context context, List<FileUploadModel> files,
                       OnFileRemoveListener removeListener) {
        this.context = context;
        this.files = files;
        this.removeListener = removeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileUploadModel file = files.get(position);

        holder.fileNameText.setText(file.getFileName());

        // Try to load image thumbnail
        try {
            Uri uri = Uri.parse(file.fileUri);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.thumbnail.setImageBitmap(bitmap);
        } catch (Exception e) {
            holder.thumbnail.setImageResource(R.drawable.ic_file_placeholder);
        }

        holder.removeButton.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemove("", file);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView fileNameText;
        Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.file_thumbnail);
            fileNameText = itemView.findViewById(R.id.file_name);
            removeButton = itemView.findViewById(R.id.btn_remove);
        }
    }
}