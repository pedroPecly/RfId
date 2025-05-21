package com.uro.serialport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uro.serialport.model.Coleta;
import com.urovo.serialport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para exibir coletas na RecyclerView do histórico
 */
public class ColetasAdapter extends RecyclerView.Adapter<ColetasAdapter.ViewHolder> {
    
    private List<Coleta> coletas = new ArrayList<>();
    private OnItemClickListener mListener;
    
    public interface OnItemClickListener {
        void onItemClick(Coleta coleta);
    }
    
    public ColetasAdapter(OnItemClickListener listener) {
        this.mListener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coleta, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coleta coleta = coletas.get(position);
        
        // Formata os dados da coleta para exibição
        String infoText = "Inventário: " + coleta.getInventarioId() + " - " + coleta.getLojaNome();
        String localText = "Local: " + coleta.getLocalColeta();
        String dataText = "Data: " + coleta.getDataHoraColeta();
        String totalText = "Total de itens: " + coleta.getTotalCodigos();
        
        // Define os textos nas views
        holder.tvColetaInfo.setText(infoText);
        holder.tvColetaLocal.setText(localText);
        holder.tvColetaData.setText(dataText);
        holder.tvColetaTotal.setText(totalText);
        
        // Define o listener para o botão de ver detalhes
        holder.btnVerDetalhes.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(coleta);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return coletas.size();
    }
    
    /**
     * Define a lista de coletas a ser exibida
     * @param coletas Lista de coletas
     */
    public void setColetas(List<Coleta> coletas) {
        this.coletas = coletas;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder para item de coleta
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvColetaInfo, tvColetaLocal, tvColetaData, tvColetaTotal;
        Button btnVerDetalhes;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvColetaInfo = itemView.findViewById(R.id.tv_coleta_info);
            tvColetaLocal = itemView.findViewById(R.id.tv_coleta_local);
            tvColetaData = itemView.findViewById(R.id.tv_coleta_data);
            tvColetaTotal = itemView.findViewById(R.id.tv_coleta_total);
            btnVerDetalhes = itemView.findViewById(R.id.btn_ver_detalhes);
        }
    }
}
