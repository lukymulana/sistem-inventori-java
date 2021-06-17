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
public class modelSupplier {
    koneksiDatabase koneksi = new koneksiDatabase();
    
    private String kodeSupplierModel;
    private String namaSupplierModel;
    private String alamatModel;
    private String noTelpModel;

    public String getKodeSupplierModel() {
        return kodeSupplierModel;
    }

    public void setKodeSupplierModel(String kodeSupplierModel) {
        this.kodeSupplierModel = kodeSupplierModel;
    }

    public String getNamaSupplierModel() {
        return namaSupplierModel;
    }

    public void setNamaSupplierModel(String namaSupplierModel) {
        this.namaSupplierModel = namaSupplierModel;
    }

    public String getAlamatModel() {
        return alamatModel;
    }

    public void setAlamatModel(String alamatModel) {
        this.alamatModel = alamatModel;
    }

    public String getNoTelpModel() {
        return noTelpModel;
    }

    public void setNoTelpModel(String notelpModel) {
        this.noTelpModel = notelpModel;
    }

    
    public void simpanDataSupplierModel() throws IOException {
        String sql = "INSERT into supplier values ('"+getKodeSupplierModel()+"', '"+getNamaSupplierModel()+"', '"
                +getAlamatModel()+"', '"+getNoTelpModel()+"')";
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan \n"+ex);
        }
    }
    
    public void ubahDataSupplierModel() throws IOException {
        String sql = "UPDATE supplier set nama_supplier='"+getNamaSupplierModel()+"', alamat='"+getAlamatModel()+
                "', notelp='"+getNoTelpModel()+"' where kode_supplier='"+getKodeSupplierModel()+"'";
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah \n"+ex);
        }
    }
    
    public void hapusDataBarangModel() {
        String sql = "DELETE from supplier where kode_supplier='"+getKodeSupplierModel()+"'";
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
