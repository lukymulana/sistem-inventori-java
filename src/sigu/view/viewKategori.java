/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.view;

import aplikasisigu.Home;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Luky Mulana
 */
public class viewKategori {
    private Home ho;
    private DefaultTableModel modelDataKategori;
    private String kodeKategori;
    
    koneksiDatabase koneksi = new koneksiDatabase();
    
    public viewKategori(Home ho) {
        this.ho = ho;
    }
    
    public void autoKodeKategori() throws IOException {
        String cek = "SELECT right(kode_kategori,1) from kategori order by kode_kategori desc limit 1";

        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(cek);
            res.next();
            int hitung = Integer.parseInt(res.getString("right(kode_kategori,1)")) + 1;
            kodeKategori = "KT-" + String.valueOf(hitung);
            ho.getTfKodeBarangKategori().setText(kodeKategori);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
    }
    
    public void tampilDataKategori() throws IOException {
        modelDataKategori = new DefaultTableModel();
        
        ho.getTabelDataKategori().setModel(modelDataKategori);
        
        modelDataKategori.addColumn("Kode Kategori");
        modelDataKategori.addColumn("Nama Kategori");
        modelDataKategori.addColumn("No Rak");
        
        modelDataKategori.getDataVector().removeAllElements();
        modelDataKategori.fireTableDataChanged();
        
        String sql = "SELECT * from kategori";
        
        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            while (res.next()) {
                Object[] hasil;
                hasil = new Object[3];
                hasil[0] = res.getString("kode_kategori");
                hasil[1] = res.getString("nama_kategori");
                hasil[2] = res.getString("no_rak");
                
                modelDataKategori.addRow(hasil);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ambilDataKategori() {
        int index = ho.getTabelDataKategori().getSelectedRow();
        
        kodeKategori = String.valueOf(ho.getTabelDataKategori().getValueAt(index, 0));
        String namaKategori = String.valueOf(ho.getTabelDataKategori().getValueAt(index, 1));
        String noRak = String.valueOf(ho.getTabelDataKategori().getValueAt(index, 2));
        
        ho.getTfKodeBarangKategori().setText(kodeKategori);
        ho.getTfNamaKategori().setText(namaKategori);
        ho.getTfNoRak().setText(noRak);
        
    }
}
