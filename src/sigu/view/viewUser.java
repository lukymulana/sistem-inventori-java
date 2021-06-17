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
 * @author Kakang Rizal
 */
public class viewUser {
    private Home ho;
    private DefaultTableModel modelDataUser;
    private String kodeUser;
    
    koneksiDatabase koneksi = new koneksiDatabase();

    public viewUser(Home ho) {
        this.ho = ho;
    }
    
    public void autoKodeUser() throws IOException {
        String cek = "SELECT right(user_id,1) from user order by user_id desc limit 1";
        
        try{
            Statement stat = (Statement) koneksi.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(cek);
            res.next();
            int hitung = Integer.parseInt(res.getString("right(user_id,1)")) + 1;
            kodeUser = "US-" + String.valueOf(hitung);
            ho.getTfKodeUser().setText(kodeUser);
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Gagal \n"+ex);
        }
    }
    
    public void tampilDataUser() throws IOException {
        modelDataUser = new DefaultTableModel();
        
        ho.getTabelDaftarUser().setModel(modelDataUser);
        
        modelDataUser.addColumn("Kode User");
        modelDataUser.addColumn("Username");
        modelDataUser.addColumn("Password");
        modelDataUser.addColumn("Nama User");
        modelDataUser.addColumn("Level");
        
        modelDataUser.getDataVector().removeAllElements();
        modelDataUser.fireTableDataChanged();
        
        String sql = "SELECT * from user";
        
        try{
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            while (res.next()){
                Object[] hasil;
                hasil = new Object[5];
                hasil[0] = res.getString("user_id");
                hasil[1] = res.getString("username");
                hasil[2] = res.getString("password");
                hasil[3] = res.getString("nama");
                hasil[4] = res.getString("level");
                
                modelDataUser.addRow(hasil);
            }
        } catch (SQLException ex){
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ambilDataUser(){
        int index = ho.getTabelDaftarUser().getSelectedRow();
        
        kodeUser = String.valueOf(ho.getTabelDaftarUser().getValueAt(index, 0));
        String username = String.valueOf(ho.getTabelDaftarUser().getValueAt(index, 1));
        String password = String.valueOf(ho.getTabelDaftarUser().getValueAt(index, 2));
        String nama = String.valueOf(ho.getTabelDaftarUser().getValueAt(index, 3));
        String level = String.valueOf(ho.getTabelDaftarUser().getValueAt(index, 4));
        
        ho.getTfKodeUser().setText(kodeUser);
        ho.getTfUsernameUser().setText(username);
        ho.getTfPasswordUser().setText(password);
        ho.getTfNamaUser().setText(nama);
        ho.getCbLevelUser().setSelectedItem(level);
        
         ho.getTfUsernameUser().setEnabled(false);
    }
}
