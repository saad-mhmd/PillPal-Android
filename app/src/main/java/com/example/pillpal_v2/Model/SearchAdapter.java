package com.example.pillpal_v2.Model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pillpal_v2.PatientClasses.FindMedActivity;
import com.example.pillpal_v2.PharmacyClasses.MedInfoAct;
import com.example.pillpal_v2.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<String> documentIds;

    public SearchAdapter(List<String> documentIds) {
        this.documentIds = documentIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String documentId = documentIds.get(position);
        holder.documentIdView.setText(documentId);

        // Set the document ID to the button's tag
        holder.button.setTag(documentId);
        holder.findmidbtn.setTag(documentId);
        holder.findmidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the document ID from the button's tag
                String clickedDocumentId = (String) view.getTag();

                // Open the new activity and pass the document ID
                Intent intent = new Intent(view.getContext(), FindMedActivity.class);
                intent.putExtra("DOCUMENT_ID", clickedDocumentId);
                view.getContext().startActivity(intent);
            }
        });

        // Set a click listener for the button
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the document ID from the button's tag
                String clickedDocumentId = (String) view.getTag();

                // Open the new activity and pass the document ID
                Intent intent = new Intent(view.getContext(), MedInfoAct.class);
                intent.putExtra("DOCUMENT_ID", clickedDocumentId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentIds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView documentIdView;
        public Button button,findmidbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            documentIdView = itemView.findViewById(R.id.document_id_view);
            button = itemView.findViewById(R.id.view_document_button); // Replace with the actual ID of your button in search_result_item.xml
            findmidbtn=itemView.findViewById(R.id.find_medd_button);

        }
    }
}
