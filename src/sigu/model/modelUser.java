/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.model;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Kakang Rizal
 */
public class modelUser {
    koneksiDatabase koneksi = new koneksiDatabase();
    
    private String kodeUserModel;
    private String usernameUserModel;
    private String passwordUserModel;
    private String namaUserModel;
    private String levelUserModel;

    public String getKodeUserModel() {
        return kodeUserModel;
    }

    public void setKodeUserModel(String kodeUserModel) {
        this.kodeUserModel = kodeUserModel;
    }

    public String getUsernameUserModel() {
        return usernameUserModel;
    }

    public void setUsernameUserModel(String usernameUserModel) {
        this.usernameUserModel = usernameUserModel;
    }

    public String getPasswordUserModel() {
        return passwordUserModel;
    }

    public void setPasswordUserModel(String passwordUserModel) {
        this.passwordUserModel = passwordUserModel;
    }

    public String getNamaUserModel() {
        return namaUserModel;
    }

    public void setNamaUserModel(String namaUserModel) {
        this.namaUserModel = namaUserModel;
    }

    public String getLevelUserModel() {
        return levelUserModel;
    }

    public void setLevelUserModel(String levelUserModel) {
        this.levelUserModel = levelUserModel;
    }
    
     public void simpanDataUser() throws IOException, SQLException {        
        String cek = "SELECT username from user where username = '"+getUsernameUserModel()+"'";
        Statement stat = (Statement) koneksi.getKoneksi().createStatement();
        ResultSet res = stat.executeQuery(cek);
        if (res.next()) {
            if (getUsernameUserModel().equals(res.getString("username"))) {
                JOptionPane.showMessageDialog(null, "Username Sudah Digunakan");
            } 
        } else {
                String sql = "INSERT into user values ('"+getKodeUserModel()+"', '"+getUsernameUserModel()+"', '"
                +getPasswordUserModel()+"', '"+getNamaUserModel()+"', '"+getLevelUserModel()+"')";
        
                try{
                    PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
                    eksekusi.execute();

                    JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                } catch (SQLException ex) {
                    Logger.getLogger(modelUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    public void ubahDataUser() throws IOException{
        String sql = "UPDATE user set username='"+getUsernameUserModel()+"', password='"+getPasswordUserModel()+
                "', nama='"+getNamaUserModel()+"', level='"+getLevelUserModel()+"' where user_id='"+getKodeUserModel()+"'";
        
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah \n"+ex);
        }
    }
    
    public void hapusDataUser() throws IOException{
        String sql = "DELETE from user where user_id='"+getKodeUserModel()+"'";
        
        try {
            PreparedStatement eksekusi = koneksi.getKoneksi().prepareStatement(sql);
            eksekusi.execute();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil di hapus");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data Gagal di hapus \n "+ex);
        } catch (IOException ex) {
            Logger.getLogger(modelUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
