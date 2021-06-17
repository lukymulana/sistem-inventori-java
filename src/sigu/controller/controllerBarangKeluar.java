/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.controller;

import aplikasisigu.Home;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import sigu.model.modelBarangKeluar;
import sigu.model.modelBarangKeluar;

/**
 *
 * @author Luky Mulana
 */
public class controllerBarangKeluar {
    private modelBarangKeluar mBK;
    private Home ho;
    
    public controllerBarangKeluar(Home ho) {
        this.ho = ho;
    }
    
    public void bersihBarangKeluar() {
        ho.getTfKodeBarangKeluar().setText("");
        ho.getTfNamaBarangKeluar().setText("");
        ho.getTfNoFakturBarangKeluar().setText("");
        ho.getTfJumlahBarangKeluar().setText("");
        Date date = new Date();
        ho.getDateTanggalBarangKeluar().setDate(date);
        ho.getTfSupplierBarangKeluar().setText("");
        ho.getCbKondisiBarangKeluar().setSelectedIndex(0);
        ho.getTaKeteranganBarangKeluar().setText("");
    }
    
    public void kontrolButton() {
        ho.getBtnTambahBarangKeluar().setEnabled(false);
        ho.getBtnHapusBarangKeluar().setEnabled(false);
        ho.getBtnBatalBarangKeluar().setEnabled(true);
    }
    
    public void kontrolButtonDua() {
        ho.getBtnTambahBarangKeluar().setEnabled(false);
        ho.getBtnHapusBarangKeluar().setEnabled(true);
        ho.getBtnBatalBarangKeluar().setEnabled(true);
    }
    
    public void kontrolButtonTiga() {
        ho.getBtnTambahBarangKeluar().setEnabled(true);
        ho.getBtnHapusBarangKeluar().setEnabled(false);
        ho.getBtnBatalBarangKeluar().setEnabled(true);
    }
    
    public void simpanDataBarangKeluar() throws IOException, SQLException {
        mBK = new modelBarangKeluar();
        
        mBK.setNoFaktur(ho.getTfNoFakturBarangKeluar().getText());

        Date date = ho.getDateTanggalBarangKeluar().getDate();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);
        
        mBK.setTanggalKeluar(strDate);
        mBK.setKodeBarang(ho.getTfKodeBarangKeluar().getText());
        mBK.setJumlah(Integer.parseInt(ho.getTfJumlahBarangKeluar().getText()));
        mBK.setKodeSupplier(ho.getTfKodeSupplierBarangKeluar().getText());
        mBK.setUserID(ho.getLblUserID().getText());
        mBK.setKeterangan(ho.getTaKeteranganBarangKeluar().getText());
        mBK.setKondisi(ho.getCbKondisiBarangKeluar().getSelectedItem().toString());
        
        mBK.kurangStok();
        mBK.simpanDataBarangKeluar();
        
        bersihBarangKeluar();
        kontrolButton();
    }
    
    public void hapusDataBarangKeluar() throws IOException {
        mBK = new modelBarangKeluar();
        
        mBK.setNoFaktur(ho.getTfNoFakturBarangKeluar().getText());
        mBK.setKodeBarang(ho.getTfKodeBarangKeluar().getText());
        
        mBK.tambahStok();
        mBK.hapusDataBarangKeluar();
        
        bersihBarangKeluar();
        kontrolButton();
    }
}
