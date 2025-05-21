package com.uro.serialport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uro.serialport.model.RfidCode;
import com.urovo.serialport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para exibir códigos RFID na RecyclerView
 */
public class RfidCodesAdapter extends RecyclerView.Adapter<RfidCodesAdapter.ViewHolder> {
    
    private List<RfidCode> rfidCodes = new ArrayList<>();
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_codigo_rfid, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RfidCode rfidCode = rfidCodes.get(position);
        holder.tvCodigoRfid.setText("EPC: " + rfidCode.getEpc() + " (" + rfidCode.getTimestamp() + ")");
    }
    
    @Override
    public int getItemCount() {
        return rfidCodes.size();
    }
    
    /**
     * Adiciona um novo código RFID à lista
     * @param rfidCode O código a ser adicionado
     * @return true se foi adicionado (não existia), false se já existia
     */
    public boolean addRfidCode(RfidCode rfidCode) {
        if (!rfidCodes.contains(rfidCode)) {
            rfidCodes.add(rfidCode);
            notifyItemInserted(rfidCodes.size() - 1);
            return true;
        }
        return false;
    }
    
    /**
     * Limpa todos os códigos RFID
     */
    public void clearRfidCodes() {
        int size = rfidCodes.size();
        rfidCodes.clear();
        notifyItemRangeRemoved(0, size);
    }
    
    /**
     * Retorna a lista de códigos RFID
     */
    public List<RfidCode> getRfidCodes() {
        return new ArrayList<>(rfidCodes);
    }
    
    /**
     * ViewHolder para item do código RFID
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigoRfid;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoRfid = itemView.findViewById(R.id.tv_codigo_rfid);
        }
    }
}
