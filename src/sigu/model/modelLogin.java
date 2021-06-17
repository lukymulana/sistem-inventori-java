/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.model;

import aplikasisigu.Home;
import aplikasisigu.Login;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sigu.db.koneksiDatabase;

/**
 *
 * @author Luky Mulana
 */
public class modelLogin {
    koneksiDatabase koneksi = new koneksiDatabase();
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void login() throws IOException {
        String sql = "SELECT * FROM user where username='"+getUsername()+"' and password='"+getPassword()+"'";
        
        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            if (res.next()) {
                if (getUsername().equals(res.getString("username")) && getPassword().equals(res.getString("password"))) {
                    JOptionPane.showMessageDialog(null, "Login Berhasil", "Message" , JOptionPane.INFORMATION_MESSAGE);
                    if (res.getString("level").equals("admin")) {
                        Home ho = new Home();
                        ho.setVisible(true);
                        ho.getLblSession().setText(res.getString("level"));
                        ho.getLblSession().setVisible(false);
                        ho.getLblUserID().setText(res.getString("user_id"));
                        ho.getLblUserID().setVisible(false);
                        ho.getLbNamaAkun().setText(res.getString("nama"));
                    } else {
                        Home ho = new Home();
                        ho.setVisible(true);
                        ho.getLblSession().setText(res.getString("level"));
                        ho.getLblSession().setVisible(false);
                        ho.getLblUserID().setText(res.getString("user_id"));
                        ho.getLblUserID().setVisible(false);
                        ho.getLbNamaAkun().setText(res.getString("nama"));
                        ho.getBtnUser().setEnabled(false);
                        ho.getLblUserMenu().setEnabled(false);
                        ho.getBtnSupplier().setEnabled(false);
                        ho.getLblSupplier().setEnabled(false);
                        ho.getBtnKategori().setEnabled(false);
                        ho.getLblKategori().setEnabled(false);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Username Atau Password Salah");
                Login lo = new Login();
                lo.setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Login Gagal \n"+ex);
        }
    }
}
