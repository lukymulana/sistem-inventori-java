/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.view;

import aplikasisigu.Home;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import sigu.db.koneksiDatabase;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;

/**
 *
 * @author Luky Mulana
 */

public class viewLaporanStokBarang extends javax.swing.JFrame {

    /**
     * Creates new form viewLaporanStokBarang
     */
    private Home ho;
    private DefaultTableModel modelLaporanStokBarang;
    private String periodeAwal;
    private String periodeAkhir;

    public String getPeriodeAwal() {
        return periodeAwal;
    }

    public void setPeriodeAwal(String periodeAwal) {
        this.periodeAwal = periodeAwal;
    }

    public String getPeriodeAkhir() {
        return periodeAkhir;
    }

    public void setPeriodeAkhir(String periodeAkhir) {
        this.periodeAkhir = periodeAkhir;
    }

    public JLabel getLblPeriodeAkhirView() {
        return lblPeriodeAkhirView;
    }

    public JLabel getLblPeriodeAwalView() {
        return lblPeriodeAwalView;
    }
    
    
    
    public viewLaporanStokBarang(Home ho) throws IOException, SQLException {
        initComponents();
        
        this.ho = ho;
        
        modelLaporanStokBarang = new DefaultTableModel();
        
        tabelLaporanStokBarang.setModel(modelLaporanStokBarang);
        
        modelLaporanStokBarang.addColumn("Kode Barang");
        modelLaporanStokBarang.addColumn("Nama Barang");
        modelLaporanStokBarang.addColumn("Kategori");
        modelLaporanStokBarang.addColumn("Supplier");
        modelLaporanStokBarang.addColumn("Stok Awal");
        modelLaporanStokBarang.addColumn("In");
        modelLaporanStokBarang.addColumn("Out");
        modelLaporanStokBarang.addColumn("Stok Akhir");
        modelLaporanStokBarang.addColumn("Satuan");        
    }
    
    public void tampilLaporanStokBarang() throws IOException, SQLException {
        modelLaporanStokBarang.getDataVector().removeAllElements();
        modelLaporanStokBarang.fireTableDataChanged();
        String query = "";
        String query1 = "";
        String query2 = "";
        String query3 = "";
        
        int masuk = 0;
        int keluar = 0;
        int in = 0;
        int out = 0;
        
        String cek = "SELECT b.kode_barang, b.nama_barang, k.nama_kategori, s.nama_supplier, b.satuan "
                + "from barang b, kategori k, supplier s where b.kode_kategori = k.kode_kategori and "
                + "b.kode_supplier = s.kode_supplier";
        
        Statement stat1 = (Statement) koneksiDatabase.getKoneksi().createStatement();
        ResultSet res = stat1.executeQuery(cek);
        
        while (res.next()) {            
            String kodeBarang = res.getString("kode_barang");
            String namaBarang = res.getString("nama_barang");
            String kategori = res.getString("nama_kategori");
            String supplier = res.getString("nama_supplier");
            String satuan = res.getString("satuan");
            
            //masuk
            if (ho.getDatePeriodeAwal().equals("")) {
                query = "SELECT barang.*, sum(barang_masuk.jumlah) as jumlah from barang, barang_masuk where "
                    + "barang.kode_barang = '"+kodeBarang+"' and "
                    + "barang.kode_barang = barang_masuk.kode_barang"
                    + "group by barang.kode_barang";
            } else 
                query = "SELECT barang.*, sum(barang_masuk.jumlah) as jumlah from barang, barang_masuk where "
                    + "barang.kode_barang = '"+kodeBarang+"' and "
                    + "barang.kode_barang = barang_masuk.kode_barang and "
                    + "tanggal_masuk < '"+getPeriodeAwal()+"' group by barang.kode_barang";
            
            Statement stat2 = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet arraymasuk = stat2.executeQuery(query);
                
            if (arraymasuk.next()) {
                masuk = Integer.parseInt(arraymasuk.getString("jumlah"));
            } else masuk = 0;   
            
            //keluar
            if (ho.getDatePeriodeAkhir().equals("")) {
                query1 = "SELECT barang.*, sum(barang_keluar.jumlah) as jumlah from barang, barang_keluar where "
                    + "barang.kode_barang = '"+res.getString("kode_barang")+"' and "
                    + "barang.kode_barang = barang_keluar.kode_barang and "
                    + "group by barang.kode_barang";
            } else 
                query1 = "SELECT barang.*, sum(barang_keluar.jumlah) as jumlah from barang, barang_keluar where "
                    + "barang.kode_barang = '"+res.getString("kode_barang")+"' and "
                    + "barang.kode_barang = barang_keluar.kode_barang and "
                    + "tanggal_keluar < '"+getPeriodeAwal()+"' group by barang.kode_barang";
            
            Statement stat3 = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet arraykeluar = stat3.executeQuery(query1);
                
            if (arraykeluar.next()) {
                keluar = Integer.parseInt(arraykeluar.getString("jumlah"));
            } else keluar = 0;   
            
            //arrayin
            if (ho.getDatePeriodeAwal().equals("") && ho.getDatePeriodeAkhir().equals("")) {
                query2 = "SELECT sum(jumlah) as jumlah from barang, barang_masuk where "
                        + "barang.kode_barang = '"+kodeBarang+"' and "
                        + "barang.kode_barang = barang_masuk.kode_barang group by barang.kode_barang";
            } else 
                query2 = "SELECT sum(jumlah) as jumlah from barang, barang_masuk where "
                        + "barang.kode_barang = '"+kodeBarang+"' and "
                        + "tanggal_masuk between '"+getPeriodeAwal()+"' and '"+getPeriodeAkhir()+"' "
                        + "and barang.kode_barang = barang_masuk.kode_barang group by barang.kode_barang";
            
            Statement stat4 = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet arrayin = stat4.executeQuery(query2);
                
            if (arrayin.next()) {
                if (arrayin.getString("jumlah") == null){
                    in = 0;
                } else {
                    in = Integer.parseInt(arrayin.getString("jumlah"));
                }
            } else in = 0;  
            
            //arrayout
            if (ho.getDatePeriodeAwal().equals("") && ho.getDatePeriodeAkhir().equals("")) {
                query3 = "SELECT sum(jumlah) as jumlah from barang, barang_keluar where "
                    + "barang.kode_barang = '"+kodeBarang+"' and "
                    + "barang.kode_barang = barang_keluar.kode_barang group by barang.kode_barang";
            } else 
                query3 = "SELECT sum(jumlah) as jumlah from barang, barang_keluar where "
                    + "barang.kode_barang = '"+kodeBarang+"' and "
                    + "tanggal_keluar between '"+getPeriodeAwal()+"' and '"+getPeriodeAkhir()+"' "
                    + "and barang.kode_barang = barang_keluar.kode_barang group by barang.kode_barang";
            
            Statement stat5 = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet arrayout = stat5.executeQuery(query3);
                
            if (arrayout.next()) {
                if (arrayout.getString("jumlah") == null){
                    out = 0;
                } else {
                    out = Integer.parseInt(arrayout.getString("jumlah"));
                }
            } else out = 0;  
            
            //hitung
            int stok_awal = masuk - keluar;
            int stok_akhir = stok_awal + (in - out);            
            
            Object[] hasil;
                    hasil = new Object[9];
                    hasil[0] = kodeBarang;
                    hasil[1] = namaBarang;
                    hasil[2] = kategori;
                    hasil[3] = supplier;
                    hasil[4] = stok_awal;
                    hasil[5] = in;
                    hasil[6] = out;
                    hasil[7] = stok_akhir;
                    hasil[8] = satuan;
                
                    modelLaporanStokBarang.addRow(hasil);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelLaporanStokBarang = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnCetakLaporan = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblPeriodeAwalView = new javax.swing.JLabel();
        lblPeriodeAkhirView = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(182, 205, 205));

        jPanel1.setBackground(new java.awt.Color(182, 205, 205));

        tabelLaporanStokBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelLaporanStokBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelLaporanStokBarangMouseClicked(evt);
            }
        });
        tabelLaporanStokBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelLaporanStokBarangKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabelLaporanStokBarang);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(55, 78, 78));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LAPORAN STOK BARANG");

        btnCetakLaporan.setBackground(new java.awt.Color(55, 78, 78));
        btnCetakLaporan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCetakLaporan.setText("CETAK");
        btnCetakLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakLaporanActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(55, 78, 78));
        jLabel2.setText("Periode Awal :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(55, 78, 78));
        jLabel3.setText("Periode Akhir :");

        lblPeriodeAwalView.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPeriodeAwalView.setForeground(new java.awt.Color(55, 78, 78));
        lblPeriodeAwalView.setText("[periodeAwal]");

        lblPeriodeAkhirView.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPeriodeAkhirView.setForeground(new java.awt.Color(55, 78, 78));
        lblPeriodeAkhirView.setText("[periodeAkhir]");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(960, 960, 960)
                        .addComponent(btnCetakLaporan))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPeriodeAwalView))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPeriodeAkhirView)))
                .addGap(9, 10, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblPeriodeAwalView))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblPeriodeAkhirView))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCetakLaporan)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tabelLaporanStokBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelLaporanStokBarangMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelLaporanStokBarangMouseClicked

    private void tabelLaporanStokBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelLaporanStokBarangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelLaporanStokBarangKeyPressed

    private void btnCetakLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakLaporanActionPerformed
        // TODO add your handling code here:
        try {
            tabelLaporanStokBarang.print(PrintMode.FIT_WIDTH, new MessageFormat("STOK BARANG"), null);
        } catch (PrinterException ex) {
            Logger.getLogger(viewLaporanStokBarang.class.getName()).log(Level.SEVERE,null,ex);
        }
    }//GEN-LAST:event_btnCetakLaporanActionPerformed

    /**
     * @param args the command line arguments
     */    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakLaporan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPeriodeAkhirView;
    private javax.swing.JLabel lblPeriodeAwalView;
    private javax.swing.JTable tabelLaporanStokBarang;
    // End of variables declaration//GEN-END:variables
}
