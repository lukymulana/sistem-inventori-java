/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.controller;

import aplikasisigu.Home;
import java.io.IOException;
import sigu.model.modelSupplier;

/**
 *
 * @author Luky Mulana
 */
public class controllerSupplier {
    private modelSupplier mS;
    private Home ho;
    
    public controllerSupplier(Home ho) {
        this.ho = ho;
    }
    
    public void bersihSupplier() {
        ho.getTfKodeSupplier().setText("");
        ho.getTfNamaSupplier().setText("");
        ho.getTaAlamatSupplier().setText("");
        ho.getTfNoTelpSupplier().setText("");
    }
    
    public void kontrolButton() {
        ho.getBtnTambahSupplier().setEnabled(true);
        ho.getBtnHapusSupplier().setEnabled(false);
        ho.getBtnEditSupplier().setEnabled(false);
        ho.getBtnBatalSupplier().setEnabled(true);
    }
    
    public void kontrolButtonDua() {
        ho.getBtnTambahSupplier().setEnabled(false);
        ho.getBtnHapusSupplier().setEnabled(true);
        ho.getBtnEditSupplier().setEnabled(true);
        ho.getBtnBatalSupplier().setEnabled(true);
    }
    
    public void simpanDataSupplier() throws IOException {
        mS = new modelSupplier();
        
        mS.setKodeSupplierModel(ho.getTfKodeSupplier().getText());
        mS.setNamaSupplierModel(ho.getTfNamaSupplier().getText());
        mS.setAlamatModel(ho.getTaAlamatSupplier().getText());
        mS.setNoTelpModel(ho.getTfNoTelpSupplier().getText());
        
        mS.simpanDataSupplierModel();
        bersihSupplier();
        kontrolButton();
    }
    
    public void ubahDataSupplier() throws IOException {
        mS = new modelSupplier();
        
        mS.setKodeSupplierModel(ho.getTfKodeSupplier().getText());
        mS.setNamaSupplierModel(ho.getTfNamaSupplier().getText());
        mS.setAlamatModel(ho.getTaAlamatSupplier().getText());
        mS.setNoTelpModel(ho.getTfNoTelpSupplier().getText());
        
        mS.ubahDataSupplierModel();
        bersihSupplier();
        kontrolButton();
    }
    
    public void hapusDataSupplier() {
        mS = new modelSupplier();
        
        mS.setKodeSupplierModel(ho.getTfKodeSupplier().getText());
        mS.hapusDataBarangModel();
        bersihSupplier();
        kontrolButton();
    }
}
