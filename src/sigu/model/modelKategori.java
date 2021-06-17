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
public class modelKategori {
    koneksiDatabase koneksi = new koneksiDatabase();
    
    private String kodeKategoriModel;
    private String namaKategotiModel;
    private String noRakModel;

    public String getKodeKategoriModel() {
        return kodeKategoriModel;
    }

    public void setKodeKategoriModel(String kodeKategoriModel) {
        this.kodeKategoriModel = kodeKategoriModel;
    }

    public String getNamaKategoriModel() {
        return namaKategotiModel;
    }

    public void setNamaKategoriModel(String namaKategotiModel) {
        this.namaKategotiModel = namaKategotiModel;
    }

    public String getNoRakModel() {
        return noRakModel;
    }

    public void setNoRakModel(String noRakModel) {
        this.noRakModel = noRakModel;
    }
    
    public void simpanDataKategori() throws IOException {
        String sql = "INSERT into kategori values ('"+getKodeKategoriModel()+"', '"+getNamaKategoriModel()+"', '"
                +getNoRakModel()+"')";
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan \n"+ex);
        }
    }
    
    public void ubahDataKategori() throws IOException {
        String sql = "UPDATE kategori set nama_kategori='"+getNamaKategoriModel()+"', no_rak='"+getNoRakModel()+
                "' where kode_kategori='"+getKodeKategoriModel()+"'";
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah \n"+ex);
        }
    }
    
    public void hapusDataBarangModel() {
        String sql = "DELETE from kategori where kode_kategori='"+getKodeKategoriModel()+"'";
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
