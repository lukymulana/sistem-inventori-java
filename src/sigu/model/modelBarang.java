/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.model;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Luky Mulana
 */
public class modelBarang {
    
    koneksiDatabase koneksi = new koneksiDatabase();
    
    private String kodeBarangModel;
    private String namaBarangModel;
    private String kodeKategoriModel;
    private String kodeSupplierModel;
    private int stokModel;
    private String satuanModel;

    public String getKodeBarangModel() {
        return kodeBarangModel;
    }

    public void setKodeBarangModel(String kodeBarangModel) {
        this.kodeBarangModel = kodeBarangModel;
    }

    public String getNamaBarangModel() {
        return namaBarangModel;
    }

    public void setNamaBarangModel(String namaBarangModel) {
        this.namaBarangModel = namaBarangModel;
    }

    public String getKodeKategoriModel() {
        return kodeKategoriModel;
    }

    public void setKodeKategoriModel(String kodeKategoriModel) {
        this.kodeKategoriModel = kodeKategoriModel;
    }

    public String getKodeSupplierModel() {
        return kodeSupplierModel;
    }

    public void setKodeSupplierModel(String kodeSupplierModel) {
        this.kodeSupplierModel = kodeSupplierModel;
    }

    public int getStokModel() {
        return stokModel;
    }

    public void setStokModel(int stokModel) {
        this.stokModel = stokModel;
    }

    public String getSatuanModel() {
        return satuanModel;
    }

    public void setSatuanModel(String satuanModel) {
        this.satuanModel = satuanModel;
    }

    public void simpanDataBarang() throws IOException {
        String sql = "INSERT into barang values ('"+getKodeBarangModel()+"', '"+getNamaBarangModel()+"', '"+getKodeKategoriModel()+
                "', '"+getKodeSupplierModel()+"', '"+getStokModel()+"', '"+getSatuanModel()+"')";
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan \n"+ex);
        }
    }
    
    public void ubahDataBarang() throws IOException {
        String sql = "UPDATE barang set nama_barang='"+getNamaBarangModel()+"', kode_kategori='"+getKodeKategoriModel()+
                "', kode_supplier='"+getKodeSupplierModel()+"', stok='"+getStokModel()+"', satuan='"+getSatuanModel()+
                "' WHERE kode_barang='"+getKodeBarangModel()+"'";
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah \n"+ex);
        }
    }
    
    public void hapusDataBarang() {
        String sql = "DELETE from barang where kode_barang='"+getKodeBarangModel()+"'";
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil di hapus");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal di hapus \n "+ex);
        } catch (IOException ex) {
            Logger.getLogger(modelSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
