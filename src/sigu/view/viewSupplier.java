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
public class viewSupplier {
    private Home ho;
    private DefaultTableModel modelDataSupplier;
    private String kodeSupplier;
    
    koneksiDatabase koneksi = new koneksiDatabase();
    
    public viewSupplier(Home ho) {
        this.ho = ho;
    }
    
    public void autoKodeSupplier() throws IOException {
        String cek = "SELECT right(kode_supplier,1) from supplier order by kode_supplier desc limit 1";

        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(cek);
            res.next();
            int hitung = Integer.parseInt(res.getString("right(kode_supplier,1)")) + 1;
            kodeSupplier = "SP-" + String.valueOf(hitung);
            ho.getTfKodeSupplier().setText(kodeSupplier);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
    }
    
    public void tampilDataSupplier() throws IOException {
        modelDataSupplier = new DefaultTableModel();
        
        ho.getTabelDataSupplier().setModel(modelDataSupplier);
        
        modelDataSupplier.addColumn("Kode Supplier");
        modelDataSupplier.addColumn("Nama Supplier");
        modelDataSupplier.addColumn("Alamat");
        modelDataSupplier.addColumn("No Telp");
        
        modelDataSupplier.getDataVector().removeAllElements();
        modelDataSupplier.fireTableDataChanged();
        
        String sql = "SELECT * from supplier";
        
        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            while (res.next()) {
                Object[] hasil;
                hasil = new Object[4];
                hasil[0] = res.getString("kode_supplier");
                hasil[1] = res.getString("nama_supplier");
                hasil[2] = res.getString("alamat");
                hasil[3] = res.getString("notelp");
                
                modelDataSupplier.addRow(hasil);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ambilDataSupplier() {
        int index = ho.getTabelDataSupplier().getSelectedRow();
        
        kodeSupplier = String.valueOf(ho.getTabelDataSupplier().getValueAt(index, 0));
        String namaSupplier = String.valueOf(ho.getTabelDataSupplier().getValueAt(index, 1));
        String alamat = String.valueOf(ho.getTabelDataSupplier().getValueAt(index, 2));
        String notelp = String.valueOf(ho.getTabelDataSupplier().getValueAt(index, 3));
        
        ho.getTfKodeSupplier().setText(kodeSupplier);
        ho.getTfNamaSupplier().setText(namaSupplier);
        ho.getTaAlamatSupplier().setText(alamat);
        ho.getTfNoTelpSupplier().setText(notelp);
    }
}
