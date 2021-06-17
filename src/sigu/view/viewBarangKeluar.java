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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
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
public class viewBarangKeluar {
    private Home ho;
    private DefaultTableModel modelDataBarangKeluar;
    private String kodeBarang;
    private String noFaktur;
    
    koneksiDatabase koneksi = new koneksiDatabase();
    
    public viewBarangKeluar(Home ho) {
        this.ho = ho;
    }
    
    public void autoNoFakturBarangKeluar() throws IOException {
        String cek = "SELECT left(no_faktur,1) from barang_keluar order by no_faktur desc limit 1";

        try {
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(cek);
            res.next();
            int hitung = Integer.parseInt(res.getString("left(no_faktur,1)")) + 1;
            Date date = new Date(); // your date
            // Choose time zone in which you want to interpret your Date
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);

            noFaktur = String.valueOf(hitung) + "/BK/" + year;
            ho.getTfNoFakturBarangKeluar().setText(noFaktur);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
    }
    
    public void tampilDataBarangKeluar() throws IOException {
        modelDataBarangKeluar = new DefaultTableModel();
        
        ho.getTabelBarangKeluar().setModel(modelDataBarangKeluar);
        
        modelDataBarangKeluar.addColumn("No Faktur");
        modelDataBarangKeluar.addColumn("Tanggal Keluar");
        modelDataBarangKeluar.addColumn("Nama Barang");
        modelDataBarangKeluar.addColumn("Kode Barang");
        modelDataBarangKeluar.addColumn("Jumlah");
        modelDataBarangKeluar.addColumn("Supplier");
        modelDataBarangKeluar.addColumn("Kode Supplier");
        modelDataBarangKeluar.addColumn("Keterangan");
        modelDataBarangKeluar.addColumn("Kondisi");
        modelDataBarangKeluar.addColumn("Petugas");
        
        modelDataBarangKeluar.getDataVector().removeAllElements();
        modelDataBarangKeluar.fireTableDataChanged();
        
        String sql = "SELECT no_faktur, barang.kode_barang, nama_barang, tanggal_keluar, jumlah, supplier.kode_supplier,"
                + "nama_supplier, "
                + "nama, keterangan, kondisi from barang_keluar, barang, supplier, user where "
                + "barang_keluar.kode_barang=barang.kode_barang and "
                + "barang_keluar.kode_supplier = supplier.kode_supplier and barang_keluar.user_id = user.user_id";
        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            while (res.next()) {
                Object[] hasil;
                hasil = new Object[10];
                hasil[0] = res.getString("no_faktur");
                hasil[1] = res.getString("tanggal_keluar");
                hasil[2] = res.getString("nama_barang");
                hasil[3] = res.getString("kode_barang");
                hasil[4] = res.getString("jumlah");
                hasil[5] = res.getString("nama_supplier");
                hasil[6] = res.getString("kode_supplier");
                hasil[7] = res.getString("keterangan");
                hasil[8] = res.getString("kondisi");
                hasil[9] = res.getString("nama");
                
                modelDataBarangKeluar.addRow(hasil);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TableColumnModel tcm = ho.getTabelBarangKeluar().getColumnModel();
        tcm.removeColumn(ho.getTabelBarangKeluar().getColumnModel().getColumn(3));
        tcm.removeColumn(ho.getTabelBarangKeluar().getColumnModel().getColumn(5));
    }
    
    public void ambilDataBarangKeluar() throws ParseException {
        int index = ho.getTabelBarangKeluar().getSelectedRow();
        
        kodeBarang = String.valueOf(ho.getTabelBarangKeluar().getModel().getValueAt(index, 3));
        noFaktur = String.valueOf(ho.getTabelBarangKeluar().getValueAt(index, 0));
        String tanggalKeluar = String.valueOf(ho.getTabelBarangKeluar().getValueAt(index, 1));
        Date tglKlr = new SimpleDateFormat("yyyy-MM-dd").parse(tanggalKeluar);
        
        String namaBarang = String.valueOf(ho.getTabelBarangKeluar().getValueAt(index, 2));
        String jumlah = String.valueOf(ho.getTabelBarangKeluar().getValueAt(index, 3));
        String namaSupplier = String.valueOf(ho.getTabelBarangKeluar().getValueAt(index, 4));
        String kodeSupplier = String.valueOf(ho.getTabelBarangKeluar().getModel().getValueAt(index, 6));
        String keterangan = String.valueOf(ho.getTabelBarangKeluar().getValueAt(index, 5));
        String kondisi = String.valueOf(ho.getTabelBarangKeluar().getValueAt(index, 6));
                
        ho.getTfKodeBarangKeluar().setText(kodeBarang);
        ho.getTfNamaBarangKeluar().setText(namaBarang);
        ho.getTfNoFakturBarangKeluar().setText(noFaktur);
        ho.getDateTanggalBarangKeluar().setDate(tglKlr);
        ho.getTfJumlahBarangKeluar().setText(jumlah);
        ho.getTfSupplierBarangKeluar().setText(namaSupplier);
        ho.getTaKeteranganBarangKeluar().setText(keterangan);
        ho.getCbKondisiBarangKeluar().setSelectedItem(kondisi);
        ho.getTfKodeSupplierBarangKeluar().setText(kodeSupplier);
    }
}
