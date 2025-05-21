package com.uro.serialport.model;

import java.io.Serializable;

/**
 * Classe modelo para armazenar informações dos códigos RFID lidos
 */
public class RfidCode implements Serializable {
    private String epc;
    private String timestamp;

    public RfidCode(String epc, String timestamp) {
        this.epc = epc;
        this.timestamp = timestamp;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RfidCode rfidCode = (RfidCode) o;
        return epc.equals(rfidCode.epc);
    }

    @Override
    public int hashCode() {
        return epc.hashCode();
    }
}
