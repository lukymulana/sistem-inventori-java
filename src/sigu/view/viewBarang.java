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
import javax.swing.table.TableColumnModel;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Luky Mulana
 */
public class viewBarang {
    private Home ho;
    private DefaultTableModel modelDataBarang;
    private String kodeBarang;
    
    koneksiDatabase koneksi = new koneksiDatabase();
    
    public viewBarang(Home ho) {
        this.ho = ho;
    }
    
    public void autoKodeBarang() throws IOException {
        String cek = "SELECT right(kode_barang,1) from barang order by kode_barang desc limit 1";

        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(cek);
            res.next();
            int hitung = Integer.parseInt(res.getString("right(kode_barang,1)")) + 1;
            kodeBarang = "BR-" + String.valueOf(hitung);
            ho.getTfKodeBarang().setText(kodeBarang);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
    }
    
    public void tampilDataBarang() throws IOException {
        modelDataBarang = new DefaultTableModel();
        
        ho.getTabelDataBarang().setModel(modelDataBarang);
        
        modelDataBarang.addColumn("Kode Barang");
        modelDataBarang.addColumn("Nama Barang");
        modelDataBarang.addColumn("Nama Kategori");
        modelDataBarang.addColumn("ID Kategori");
        modelDataBarang.addColumn("Nama Supplier");
        modelDataBarang.addColumn("ID Supplier");
        modelDataBarang.addColumn("Stok");
        modelDataBarang.addColumn("Satuan");
        
        modelDataBarang.getDataVector().removeAllElements();
        modelDataBarang.fireTableDataChanged();
        
        String sql = "SELECT b.kode_barang, b.nama_barang, k.nama_kategori, k.kode_kategori, s.nama_supplier, s.kode_supplier,b.stok, b.satuan "
                + "from barang b, kategori k, supplier s where b.kode_kategori = k.kode_kategori "
                + "and b.kode_supplier = s.kode_supplier order by b.kode_barang asc";
        
        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            while (res.next()) {
                Object[] hasil;
                hasil = new Object[8];
                hasil[0] = res.getString("kode_barang");
                hasil[1] = res.getString("nama_barang");
                hasil[2] = res.getString("nama_kategori");
                hasil[3] = res.getString("kode_kategori");
                hasil[4] = res.getString("nama_supplier");
                hasil[5] = res.getString("kode_supplier");
                hasil[6] = res.getString("stok");
                hasil[7] = res.getString("satuan");
                
                modelDataBarang.addRow(hasil);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TableColumnModel tcm = ho.getTabelDataBarang().getColumnModel();
        tcm.removeColumn(ho.getTabelDataBarang().getColumnModel().getColumn(3));
        tcm.removeColumn(ho.getTabelDataBarang().getColumnModel().getColumn(4));
    }
    
    public void ambilDataBarang() {
        int index = ho.getTabelDataBarang().getSelectedRow();
        
        kodeBarang = String.valueOf(ho.getTabelDataBarang().getValueAt(index, 0));
        String namaBarang = String.valueOf(ho.getTabelDataBarang().getValueAt(index, 1));
        String namaKategori = String.valueOf(ho.getTabelDataBarang().getValueAt(index, 2));
        String namaSupplier = String.valueOf(ho.getTabelDataBarang().getValueAt(index, 3));
        String kodeKategori = (String) ho.getTabelDataBarang().getModel().getValueAt(index, 3);
        String stok = String.valueOf(ho.getTabelDataBarang().getValueAt(index, 4));
        String kodeSupplier = (String) ho.getTabelDataBarang().getModel().getValueAt(index, 5);
        String satuan = String.valueOf(ho.getTabelDataBarang().getValueAt(index, 5));
        
        ho.getTfKodeBarang().setText(kodeBarang);
        ho.getTfNamaBarang().setText(namaBarang);
        ho.getCbKategoriBarang().setSelectedItem(namaKategori);
        ho.getCbSupplierBarang().setSelectedItem(namaSupplier);
        ho.getTfStokBarang().setText(stok);
        ho.getTfSatuanBarang().setText(satuan);
        ho.getTfKodeKategoriBarang().setText(kodeKategori);
        ho.getTfKodeSupplierBarang().setText(kodeSupplier);
    }
    
    public void dataKategoriToComboBox() throws IOException {
        String sql = "SELECT nama_kategori from kategori";
        
        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
                      
            while (res.next()) {                
                ho.getCbKategoriBarang().addItem(res.getString("nama_kategori"));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
            
    }
    
    public void dataSupplierToComboBox() throws IOException {
        String sql = "SELECT nama_supplier from supplier";
        
        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
                      
            while (res.next()) {                
                ho.getCbSupplierBarang().addItem(res.getString("nama_supplier"));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
            
    }
}
