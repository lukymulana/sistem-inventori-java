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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Luky Mulana
 */
public class viewBarangMasuk {
    private final Home ho;
    private DefaultTableModel modelDataBarangMasuk;
    private String kodeBarang;
    
    koneksiDatabase koneksi = new koneksiDatabase();
    
    public viewBarangMasuk(Home ho) {
        this.ho = ho;
    }
    
    public void tampilDataBarangMasuk() throws IOException {
        modelDataBarangMasuk = new DefaultTableModel();
        
        ho.getTabelBarangMasuk().setModel(modelDataBarangMasuk);
        
        modelDataBarangMasuk.addColumn("No Faktur");
        modelDataBarangMasuk.addColumn("Tanggal Masuk");
        modelDataBarangMasuk.addColumn("Nama Barang");
        modelDataBarangMasuk.addColumn("Kode Barang");
        modelDataBarangMasuk.addColumn("Jumlah");
        modelDataBarangMasuk.addColumn("Supplier");
        modelDataBarangMasuk.addColumn("Kode Supplier");
        modelDataBarangMasuk.addColumn("Kondisi");
        modelDataBarangMasuk.addColumn("Petugas");
        
        modelDataBarangMasuk.getDataVector().removeAllElements();
        modelDataBarangMasuk.fireTableDataChanged();
        
        String sql = "SELECT no_faktur, barang.kode_barang, nama_barang, tanggal_masuk, jumlah, supplier.kode_supplier, "
                + "nama_supplier, nama, "
                + "kondisi from barang_masuk, barang, supplier, user where barang_masuk.kode_barang=barang.kode_barang "
                + "and barang_masuk.kode_supplier = supplier.kode_supplier and barang_masuk.user_id = user.user_id";
        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            while (res.next()) {
                Object[] hasil;
                hasil = new Object[9];
                hasil[0] = res.getString("no_faktur");
                hasil[1] = res.getString("tanggal_masuk");
                hasil[2] = res.getString("nama_barang");
                hasil[3] = res.getString("kode_barang");
                hasil[4] = res.getString("jumlah");
                hasil[5] = res.getString("nama_supplier");
                hasil[6] = res.getString("kode_supplier");
                hasil[7] = res.getString("kondisi");
                hasil[8] = res.getString("nama");
                
                modelDataBarangMasuk.addRow(hasil);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TableColumnModel tcm = ho.getTabelBarangMasuk().getColumnModel();
        tcm.removeColumn(ho.getTabelBarangMasuk().getColumnModel().getColumn(3));
        tcm.removeColumn(ho.getTabelBarangMasuk().getColumnModel().getColumn(5));
    }
    
    public void ambilDataBarangMasuk() throws ParseException {
        int index = ho.getTabelBarangMasuk().getSelectedRow();
        
        kodeBarang = String.valueOf(ho.getTabelBarangMasuk().getModel().getValueAt(index, 3));
        String noFaktur = String.valueOf(ho.getTabelBarangMasuk().getValueAt(index, 0));
        String tanggalMasuk = String.valueOf(ho.getTabelBarangMasuk().getValueAt(index, 1));
        Date tglMsk = new SimpleDateFormat("yyyy-MM-dd").parse(tanggalMasuk);
        
        String namaBarang = String.valueOf(ho.getTabelBarangMasuk().getValueAt(index, 2));
        String jumlah = String.valueOf(ho.getTabelBarangMasuk().getValueAt(index, 3));
        String namaSupplier = String.valueOf(ho.getTabelBarangMasuk().getValueAt(index, 4));
        String kodeSupplier = String.valueOf(ho.getTabelBarangMasuk().getModel().getValueAt(index, 6));
        String kondisi = String.valueOf(ho.getTabelBarangMasuk().getValueAt(index, 5));
        String namaPetugas = String.valueOf(ho.getTabelBarangMasuk().getValueAt(index, 6));
        
        ho.getTfKodeBarangMasuk().setText(kodeBarang);
        ho.getTfNamaBarangMasuk().setText(namaBarang);
        ho.getTfNoFakturBarangMasuk().setText(noFaktur);
        ho.getDateTanggalBarangMasuk().setDate(tglMsk);
        ho.getTfJumlahBarangMasuk().setText(jumlah);
        ho.getTfNamaSupplierBarangMasuk().setText(namaSupplier);
        ho.getCbKondisiBarangMasuk().setSelectedItem(kondisi);
        ho.getTfKodeSupplierBarangMasuk().setText(kodeSupplier);
    }
}
