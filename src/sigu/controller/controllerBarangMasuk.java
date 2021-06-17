/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.controller;

import aplikasisigu.Home;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import sigu.model.modelBarangMasuk;

/**
 *
 * @author Luky Mulana
 */
public class controllerBarangMasuk {
    private modelBarangMasuk mBM;
    private Home ho;
    
    public controllerBarangMasuk(Home ho) {
        this.ho = ho;
    }
    
    public void bersihBarangMasuk() {
        ho.getTfKodeBarangMasuk().setText("");
        ho.getTfNamaBarangMasuk().setText("");
        ho.getTfNoFakturBarangMasuk().setText("");
        ho.getTfJumlahBarangMasuk().setText("");
        Date date = new Date();
        ho.getDateTanggalBarangMasuk().setDate(date);
        ho.getTfNamaSupplierBarangMasuk().setText("");
        ho.getCbKondisiBarangMasuk().setSelectedIndex(0);
    }
    
    public void kontrolButton() {
        ho.getBtnTambahBarangMasuk().setEnabled(false);
        ho.getBtnHapusBarangMasuk().setEnabled(false);
        ho.getBtnBatalBarangMasuk().setEnabled(true);
    }
    
    public void kontrolButtonDua() {
        ho.getBtnTambahBarangMasuk().setEnabled(false);
        ho.getBtnHapusBarangMasuk().setEnabled(true);
        ho.getBtnBatalBarangMasuk().setEnabled(true);
    }
    
    public void kontrolButtonTiga() {
        ho.getBtnTambahBarangMasuk().setEnabled(true);
        ho.getBtnHapusBarangMasuk().setEnabled(false);
        ho.getBtnBatalBarangMasuk().setEnabled(true);
    }
    
    public void simpanDataBarangMasuk() throws IOException, ParseException {
        mBM = new modelBarangMasuk();
        
        mBM.setNoFaktur(ho.getTfNoFakturBarangMasuk().getText());

        Date date = ho.getDateTanggalBarangMasuk().getDate();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);
        
        mBM.setTanggalMasuk(strDate);
        mBM.setKodeBarang(ho.getTfKodeBarangMasuk().getText());
        mBM.setJumlah(Integer.parseInt(ho.getTfJumlahBarangMasuk().getText()));
        mBM.setKodeSupplier(ho.getTfKodeSupplierBarangMasuk().getText());
        mBM.setUserID(ho.getLblUserID().getText());
        mBM.setKondisi(ho.getCbKondisiBarangMasuk().getSelectedItem().toString());
        
        mBM.tambahStok();
        mBM.simpanDataBarangMasuk();
        
        bersihBarangMasuk();
        kontrolButton();
    }
    
    public void hapusDataBarangMasuk() throws IOException {
        mBM = new modelBarangMasuk();
        
        mBM.setNoFaktur(ho.getTfNoFakturBarangMasuk().getText());
        mBM.setKodeBarang(ho.getTfKodeBarangMasuk().getText());
        
        mBM.kurangStok();
        mBM.hapusDataBarangMasuk();
        
        bersihBarangMasuk();
        kontrolButton();
    }
}
