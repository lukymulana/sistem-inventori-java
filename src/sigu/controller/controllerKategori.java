/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.controller;

import aplikasisigu.Home;
import java.io.IOException;
import sigu.model.modelKategori;

/**
 *
 * @author Luky Mulana
 */
public class controllerKategori {
    private modelKategori mK;
    private Home ho;
    
    public controllerKategori(Home ho) {
        this.ho = ho;
    }
    
    public void bersihKategori() {
        ho.getTfKodeBarangKategori().setText("");
        ho.getTfNamaKategori().setText("");
        ho.getTfNoRak().setText("");
    }
    
    public void kontrolButton() {
        ho.getBtnTambahKategori().setEnabled(true);
        ho.getBtnHapusKategori().setEnabled(false);
        ho.getBtnEditKategori().setEnabled(false);
        ho.getBtnBatalKategori().setEnabled(true);
    }
    
    public void kontrolButtonDua() {
        ho.getBtnTambahKategori().setEnabled(false);
        ho.getBtnHapusKategori().setEnabled(true);
        ho.getBtnEditKategori().setEnabled(true);
        ho.getBtnBatalKategori().setEnabled(true);
    }
    
    public void simpanDataKategori() throws IOException {
        mK = new modelKategori();
        
        mK.setKodeKategoriModel(ho.getTfKodeBarangKategori().getText());
        mK.setNamaKategoriModel(ho.getTfNamaKategori().getText());
        mK.setNoRakModel(ho.getTfNoRak().getText());
        
        mK.simpanDataKategori();
        bersihKategori();
        kontrolButton();
    }
    
    public void ubahDataKategori() throws IOException {
        mK = new modelKategori();
        
        mK.setKodeKategoriModel(ho.getTfKodeBarangKategori().getText());
        mK.setNamaKategoriModel(ho.getTfNamaKategori().getText());
        mK.setNoRakModel(ho.getTfNoRak().getText());
        
        mK.ubahDataKategori();
        bersihKategori();
        kontrolButton();
    }
    
    public void hapusDataKategori() {
        mK = new modelKategori();
        
        mK.setKodeKategoriModel(ho.getTfKodeBarangKategori().getText());
        mK.hapusDataBarangModel();
        bersihKategori();
        kontrolButton();
    }
}
