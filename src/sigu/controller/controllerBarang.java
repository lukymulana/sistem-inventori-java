/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.controller;

import aplikasisigu.Home;
import java.io.IOException;
import sigu.model.modelBarang;
import sigu.model.modelKategori;

/**
 *
 * @author Luky Mulana
 */
public class controllerBarang {
    private modelBarang mB;
    private Home ho;
    
    public controllerBarang(Home ho) {
        this.ho = ho;
    }
    
    public void bersihBarang() {
        ho.getTfKodeBarangKategori().setText("");
        ho.getTfNamaBarang().setText("");
        ho.getCbKategoriBarang().setSelectedIndex(0);
        ho.getCbSupplierBarang().setSelectedIndex(0);
        ho.getTfStokBarang().setText("");
        ho.getTfSatuanBarang().setText("");
    }
    
    public void kontrolButton() {
        ho.getBtnTambahDataBarang().setEnabled(true);
        ho.getBtnHapusDataBarang().setEnabled(false);
        ho.getBtnEditDataBarang().setEnabled(false);
        ho.getBtnBatalDataBarang().setEnabled(true);
    }
    
    public void kontrolButtonDua() {
        ho.getBtnTambahDataBarang().setEnabled(false);
        ho.getBtnHapusDataBarang().setEnabled(true);
        ho.getBtnEditDataBarang().setEnabled(true);
        ho.getBtnBatalDataBarang().setEnabled(true);
    }
    
    public void simpanDataBarang() throws IOException {
        mB = new modelBarang();
        
        mB.setKodeBarangModel(ho.getTfKodeBarang().getText());
        mB.setNamaBarangModel(ho.getTfNamaBarang().getText());
        mB.setKodeKategoriModel(ho.getTfKodeKategoriBarang().getText());
        mB.setKodeSupplierModel(ho.getTfKodeSupplierBarang().getText());
        mB.setStokModel(Integer.parseInt(ho.getTfStokBarang().getText()));
        mB.setSatuanModel(ho.getTfSatuanBarang().getText());
        
        mB.simpanDataBarang();
        bersihBarang();
        kontrolButton();
    }
    
    public void ubahDataBarang() throws IOException {
        mB = new modelBarang();
        
        mB.setKodeBarangModel(ho.getTfKodeBarang().getText());
        mB.setNamaBarangModel(ho.getTfNamaBarang().getText());
        mB.setKodeKategoriModel(ho.getTfKodeKategoriBarang().getText());
        mB.setKodeSupplierModel(ho.getTfKodeSupplierBarang().getText());
        mB.setStokModel(Integer.parseInt(ho.getTfStokBarang().getText()));
        mB.setSatuanModel(ho.getTfSatuanBarang().getText());
        
        mB.ubahDataBarang();
        bersihBarang();
        kontrolButton();
    }
    
    public void hapusDataBarang() {
        mB = new modelBarang();
        
        mB.setKodeBarangModel(ho.getTfKodeBarang().getText());
                
        mB.hapusDataBarang();
        bersihBarang();
        kontrolButton();
    }
}
