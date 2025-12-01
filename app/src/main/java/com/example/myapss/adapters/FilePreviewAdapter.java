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

public class FilePreviewAdapter extends RecyclerView.Adapter<FilePreviewAdapter.ViewHolder> {

    private final Context context;
    private List<FileUploadModel> files;
    private final OnFileRemoveListener removeListener;
    private final String fieldName;

    public interface OnFileRemoveListener {
        void onRemove(String fieldName, FileUploadModel file);
    }

    public FilePreviewAdapter(Context context,
                              String fieldName,
                              List<FileUploadModel> files,
                              OnFileRemoveListener removeListener) {
        this.context = context;
        this.fieldName = fieldName;
        this.files = files;
        this.removeListener = removeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_file_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileUploadModel file = files.get(position);

        // ✅ SAFE FILE NAME
        holder.fileNameText.setText(
                file.getFileName() == null || file.getFileName().isEmpty()
                        ? "File"
                        : file.getFileName()
        );

        // ✅ SAFE IMAGE / FILE PREVIEW
        try {
            Uri uri = Uri.parse(file.fileUri);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;   // ✅ PREVENT OOM

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            holder.thumbnail.setImageBitmap(bitmap);

        } catch (Exception e) {
            // ✅ IF NOT IMAGE → SHOW FILE ICON
            holder.thumbnail.setImageResource(R.drawable.ic_file_placeholder);
        }

        // ✅ REMOVE BUTTON CALLBACK
        holder.removeButton.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemove(fieldName, file);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }

    // ✅ ADAPTER UPDATE HOOK
    public void setFiles(List<FileUploadModel> models) {
        this.files = models;
        notifyDataSetChanged();
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