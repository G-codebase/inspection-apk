package com.example.myapss.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapss.R;
import com.example.myapss.models.InspectionModel;

import java.util.List;

public class inspectionHistry extends RecyclerView.Adapter<com.example.myapss.adapters.inspectionHistry.ViewHolder> {

    private Context context;
    private List<InspectionModel> inspections;
    private OnInspectionClickListener listener;

    public interface OnInspectionClickListener {
        void onInspectionClick(InspectionModel inspection);
    }

    public inspectionHistry(Context context, List<InspectionModel> inspections,
                            OnInspectionClickListener listener) {
        this.context = context;
        this.inspections = inspections;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inspection_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InspectionModel inspection = inspections.get(position);

        holder.vesselNameText.setText(inspection.vesselName);
        holder.imoText.setText("IMO: " + inspection.imo);
        holder.dateText.setText(inspection.inspectionDate);
        holder.engineerText.setText("Engineer: " + inspection.engineerName);

        String statusColor = inspection.status.equals("COMPLETED") ? "#22C55E" :
                inspection.status.equals("SUBMITTED") ? "#3B82F6" : "#F59E0B";
        holder.statusBadge.setBackgroundColor(android.graphics.Color.parseColor(statusColor));
        holder.statusText.setText(inspection.status);

        holder.cardContainer.setOnClickListener(v -> listener.onInspectionClick(inspection));
    }

    @Override
    public int getItemCount() {
        return inspections.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vesselNameText;
        TextView imoText;
        TextView dateText;
        TextView engineerText;
        TextView statusText;
        LinearLayout statusBadge;
        LinearLayout cardContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.card_container);
            vesselNameText = itemView.findViewById(R.id.vessel_name_text);
            imoText = itemView.findViewById(R.id.imo_text);
            dateText = itemView.findViewById(R.id.date_text);
            engineerText = itemView.findViewById(R.id.engineer_text);
            statusText = itemView.findViewById(R.id.status_text);
            statusBadge = itemView.findViewById(R.id.status_badge);
        }
    }
}