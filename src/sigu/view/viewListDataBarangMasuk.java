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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import sigu.controller.controllerBarangMasuk;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Luky Mulana
 */
public class viewListDataBarangMasuk extends javax.swing.JFrame {

    /**
     * Creates new form viewListDataBarang
     */
    private Home ho;
    private DefaultTableModel modelDataListBarang;
    private String sql = "";
    
    public viewListDataBarangMasuk(Home ho) throws IOException {
        initComponents();
        
        this.ho = ho;
        
        modelDataListBarang = new DefaultTableModel();
        tabelListDataBarang.setModel(modelDataListBarang);
        modelDataListBarang.addColumn("Kode Barang");
        modelDataListBarang.addColumn("Nama Barang");
        modelDataListBarang.addColumn("Nama Kategori");
        modelDataListBarang.addColumn("ID Kategori");
        modelDataListBarang.addColumn("Nama Supplier");
        modelDataListBarang.addColumn("ID Supplier");
        modelDataListBarang.addColumn("Stok");
        modelDataListBarang.addColumn("Satuan");
        
        TableColumnModel tcm = tabelListDataBarang.getColumnModel();
        tcm.removeColumn(tabelListDataBarang.getColumnModel().getColumn(3));
        tcm.removeColumn(tabelListDataBarang.getColumnModel().getColumn(4));
        
        tampilDataListBarang("");
    }
    
    public void tampilDataListBarang(String data) throws IOException {
        modelDataListBarang.getDataVector().removeAllElements();
        modelDataListBarang.fireTableDataChanged();
        
        //kondisi pengecekan
        if (data.equals("")) {
            sql = "SELECT b.kode_barang, b.nama_barang, k.nama_kategori, k.kode_kategori, s.nama_supplier, s.kode_supplier,b.stok, b.satuan "
                + "from barang b, kategori k, supplier s where b.kode_kategori = k.kode_kategori "
                + "and b.kode_supplier = s.kode_supplier order by b.kode_barang asc";
            
            
        } else sql = "SELECT b.kode_barang, b.nama_barang, k.nama_kategori, k.kode_kategori, s.nama_supplier, s.kode_supplier,b.stok, b.satuan "
                + "from barang b, kategori k, supplier s where b.kode_kategori = k.kode_kategori "
                + "and b.kode_supplier = s.kode_supplier and b.kode_barang like '%"+data+"%' order by b.kode_barang asc";
        
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
                
                modelDataListBarang.addRow(hasil);
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(viewListDataBarangMasuk.class.getName()).log(Level.SEVERE, null, ex);
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
        tabelListDataBarang = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtKodeBarangViewListDataBarang = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(182, 205, 205));

        tabelListDataBarang.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelListDataBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelListDataBarangMouseClicked(evt);
            }
        });
        tabelListDataBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelListDataBarangKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabelListDataBarang);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(55, 78, 78));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LIST DATA BARANG");

        txtKodeBarangViewListDataBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKodeBarangViewListDataBarangKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(55, 78, 78));
        jLabel2.setText("Kode Barang");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtKodeBarangViewListDataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 80, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtKodeBarangViewListDataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addContainerGap(341, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtKodeBarangViewListDataBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKodeBarangViewListDataBarangKeyPressed
        try {
            // TODO add your handling code here:
            // fungsi ini akan di eksekusi ketika kita mencari nama barang
            tampilDataListBarang(txtKodeBarangViewListDataBarang.getText());
            
        } catch (IOException ex) {
            Logger.getLogger(viewListDataBarangMasuk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtKodeBarangViewListDataBarangKeyPressed

    private void tabelListDataBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelListDataBarangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelListDataBarangKeyPressed

    private void tabelListDataBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelListDataBarangMouseClicked
        // TODO add your handling code here:
        int ambilRow = tabelListDataBarang.getSelectedRow();
        ho.getTfKodeBarangMasuk().setText((String) tabelListDataBarang.getValueAt(ambilRow, 0));
        ho.getTfNamaBarangMasuk().setText(tabelListDataBarang.getValueAt(ambilRow, 1).toString());
        ho.getTfNamaSupplierBarangMasuk().setText(tabelListDataBarang.getValueAt(ambilRow, 3).toString());
        ho.getTfKodeSupplierBarangMasuk().setText(tabelListDataBarang.getModel().getValueAt(ambilRow, 5).toString());
                
        ho.getTfNoFakturBarangMasuk().requestFocus();
        ho.setEnabled(true);
        controllerBarangMasuk cBM = new controllerBarangMasuk(ho);
        cBM.kontrolButtonTiga();
        this.dispose();
    }//GEN-LAST:event_tabelListDataBarangMouseClicked

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelListDataBarang;
    private javax.swing.JTextField txtKodeBarangViewListDataBarang;
    // End of variables declaration//GEN-END:variables
}
