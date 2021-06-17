/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Luky Mulana
 */
public class koneksiDatabase {
    private static Connection conn;
    private static Properties properti = new Properties();
    
    public static Connection getKoneksi() throws IOException {
        if (conn == null) {
            try {
                properti.load(new FileInputStream("D:\\Luky Mulana\\Sekolah\\Kuliah\\Semester 5\\Pemograman Berbasis Dekstop\\SIGU_REV04\\src\\sigu\\db\\konfigurasiDatabase.properties"));
                conn = DriverManager.getConnection(properti.getProperty("jdbc.url"), properti.getProperty("jdbc.username"), properti.getProperty("jdbc.password"));
            } catch (SQLException ex) {
                System.err.println("error mengambil file "+ex);
                System.err.println("error membaca file "+ex);
            }
        }
        return conn;
    }
    
    public static void main(String[] args) throws IOException  {
        if (getKoneksi().equals(conn)) {
            System.out.println("Sukses Terkoneksi");
        }
    }
}
