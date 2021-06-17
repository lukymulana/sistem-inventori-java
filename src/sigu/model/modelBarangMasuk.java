/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.model;

import aplikasisigu.Home;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Luky Mulana
 */
public class modelBarangMasuk {
    koneksiDatabase koneksi = new koneksiDatabase();
    
    private String noFaktur;
    private String tanggalMasuk;
    private String kodeBarang;
    private int jumlah;
    private String kodeSupplier;
    private String userID;
    private String kondisi;
    private int stokAkhir;


    public String getNoFaktur() {
        return noFaktur;
    }

    public int getStokAkhir() {
        return stokAkhir;
    }

    public void setStokAkhir(int stokAkhir) {
        this.stokAkhir = stokAkhir;
    }

    public void setNoFaktur(String noFaktur) {
        this.noFaktur = noFaktur;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(String tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }
    

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getKodeSupplier() {
        return kodeSupplier;
    }

    public void setKodeSupplier(String kodeSupplier) {
        this.kodeSupplier = kodeSupplier;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    
    public void tambahStok() throws IOException{
        String cek = "SELECT stok from barang where kode_barang='"+getKodeBarang()+"'";
        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(cek);
            res.next();
            int stok_akhir = Integer.parseInt(res.getString("stok")) + getJumlah();
            setStokAkhir(stok_akhir);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
    }
    
    public void simpanDataBarangMasuk() throws IOException {
       
       String query = "INSERT INTO barang_masuk VALUES ('"+getNoFaktur()+"', '"+getTanggalMasuk()+"', '"+getKodeBarang()+
                    "', '"+getJumlah()+"', '"+getKodeSupplier()+"', '"+getUserID()+"', '"+getKondisi()+"')";
       
       String edit_stok = "UPDATE barang set stok = '"+getStokAkhir()+"' where kode_barang = '"+getKodeBarang()+"'";

        try {
            
            PreparedStatement eksekusi_query = koneksi.getKoneksi().prepareStatement(query);
            eksekusi_query.execute();
            
            PreparedStatement eksekusi_stok = koneksi.getKoneksi().prepareStatement(edit_stok);
            eksekusi_stok.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan \n"+ex);
        }
    }
    
    public void kurangStok() throws IOException{
        String stok_barang = "SELECT stok from barang where kode_barang='"+getKodeBarang()+"'";
        String jumlah_barang_masuk = "SELECT jumlah from barang_masuk where no_faktur='"+getNoFaktur()+"'";
        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res_stok = stat.executeQuery(stok_barang);
            res_stok.next();
            int stokBarang = Integer.parseInt(res_stok.getString("stok"));
            
            ResultSet res_jumlah = stat.executeQuery(jumlah_barang_masuk);
            res_jumlah.next();
            int stok_akhir = stokBarang - Integer.parseInt(res_jumlah.getString("jumlah"));
            setStokAkhir(stok_akhir);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
    }
    
    public void hapusDataBarangMasuk() throws IOException {
       
       String edit_stok = "UPDATE barang set stok = '"+getStokAkhir()+"' where kode_barang = '"+getKodeBarang()+"'";
       
       String delete_barang_masuk = "DELETE from barang_masuk where no_faktur = '"+getNoFaktur()+"'";

        try {
            PreparedStatement eksekusi_stok = koneksi.getKoneksi().prepareStatement(edit_stok);
            eksekusi_stok.execute();
            
            PreparedStatement eksekusi_delete = koneksi.getKoneksi().prepareStatement(delete_barang_masuk);
            eksekusi_delete.execute();
                     
            JOptionPane.showMessageDialog(null, "Data Berhasil Di Hapus");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Di Hapus \n"+ex);
        }
    }
}
