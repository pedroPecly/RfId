package com.uro.serialport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe modelo para armazenar informações de uma coleta completa
 */
public class Coleta implements Serializable {
    private String inventarioId;
    private String lojaNome;
    private String localColeta;
    private String dataHoraColeta;
    private List<RfidCode> codigosRfid;
    
    public Coleta(String inventarioId, String lojaNome, String localColeta, String dataHoraColeta) {
        this.inventarioId = inventarioId;
        this.lojaNome = lojaNome;
        this.localColeta = localColeta;
        this.dataHoraColeta = dataHoraColeta;
        this.codigosRfid = new ArrayList<>();
    }
    
    public String getInventarioId() {
        return inventarioId;
    }
    
    public void setInventarioId(String inventarioId) {
        this.inventarioId = inventarioId;
    }
    
    public String getLojaNome() {
        return lojaNome;
    }
    
    public void setLojaNome(String lojaNome) {
        this.lojaNome = lojaNome;
    }
    
    public String getLocalColeta() {
        return localColeta;
    }
    
    public void setLocalColeta(String localColeta) {
        this.localColeta = localColeta;
    }
    
    public String getDataHoraColeta() {
        return dataHoraColeta;
    }
    
    public void setDataHoraColeta(String dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
    }
    
    public List<RfidCode> getCodigosRfid() {
        return codigosRfid;
    }
    
    public void setCodigosRfid(List<RfidCode> codigosRfid) {
        this.codigosRfid = codigosRfid;
    }
    
    public void addCodigoRfid(RfidCode rfidCode) {
        if (!codigosRfid.contains(rfidCode)) {
            codigosRfid.add(rfidCode);
        }
    }
    
    public int getTotalCodigos() {
        return codigosRfid.size();
    }
}
