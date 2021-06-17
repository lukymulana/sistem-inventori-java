/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplikasisigu;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import sigu.controller.controllerBarang;
import sigu.controller.controllerBarangKeluar;
import sigu.controller.controllerBarangMasuk;
import sigu.controller.controllerKategori;
import sigu.controller.controllerSupplier;
import sigu.controller.controllerUser;
import sigu.db.koneksiDatabase;
import sigu.view.viewBarang;
import sigu.view.viewBarangKeluar;
import sigu.view.viewBarangMasuk;
import sigu.view.viewKategori;
import sigu.view.viewLaporanStokBarang;
import sigu.view.viewListDataBarangKeluar;
import sigu.view.viewListDataBarangMasuk;
import sigu.view.viewSupplier;
import sigu.view.viewUser;

/**
 *
 * @author Kakang Rizal
 */
public class Home extends javax.swing.JFrame implements Runnable{

    /**
     * Creates new form Home
     */
    //deklarasi controller
    private final controllerBarang cB;
    private final controllerSupplier cS;
    private final controllerKategori cK;
    private final controllerBarangMasuk cBM;
    private final controllerBarangKeluar cBK;
    private final controllerUser cU;
    
    //deklarasi view
    private final viewSupplier vS;
    private final viewBarang vB; 
    private final viewKategori vK;
    private final viewBarangMasuk vBM;
    private final viewListDataBarangMasuk vLDBM;
    private final viewBarangKeluar vBK;
    private final viewListDataBarangKeluar vLDBK;
    private final viewUser vU;
    private final viewLaporanStokBarang vLSB;
    
    Date date = new Date();
    
    // DIGITAL WATCH
    
    Thread t=null;  
    int hours=0, minutes=0, seconds=0;  
    String timeString = "";  

    public void DigitalWatch(){  
        t = new Thread(this);  
            t.start();  
    }  

     public void run() {  
          try {  
             while (true) {  

                Calendar cal = Calendar.getInstance();  
                hours = cal.get( Calendar.HOUR_OF_DAY );  
                if ( hours > 24 ) hours -= 24;  
                minutes = cal.get( Calendar.MINUTE );  
                seconds = cal.get( Calendar.SECOND );  

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
                Date date = cal.getTime();  
                timeString = formatter.format( date );  

                lbWaktu.setText(timeString);

                t.sleep( 1000 );  // interval given in milliseconds  
             }  
          }  
          catch (Exception e) { }  
     }
    
    public Home() throws IOException, SQLException {
        initComponents();
        
        //pembuatan objek dari controller
        cB = new controllerBarang(this);
        cS = new controllerSupplier(this);
        cK = new controllerKategori(this);
        cBM = new controllerBarangMasuk(this);
        cBK = new controllerBarangKeluar(this);
        cU = new controllerUser(this);
        
        //pembuatan objek dari view
        vS = new viewSupplier(this);
        vB = new viewBarang(this);
        vK = new viewKategori(this);
        vBM = new viewBarangMasuk(this);
        vLDBM = new viewListDataBarangMasuk(this);
        vBK = new viewBarangKeluar(this);
        vLDBK = new viewListDataBarangKeluar(this);
        vU = new viewUser(this);
        vLSB = new viewLaporanStokBarang(this);
        
        //Form Barang
        vB.tampilDataBarang();
        vB.autoKodeBarang();
        //vB.dataKategoriToComboBox();
        //vB.dataSupplierToComboBox();
        tfKodeKategoriBarang.setVisible(false);
        tfKodeSupplierBarang.setVisible(false);
        cB.kontrolButton();
        
        //Form Barang Masuk
        vBM.tampilDataBarangMasuk();
        tfKodeSupplierBarangMasuk.setVisible(false);
        cBM.kontrolButton();
        dateTanggalBarangMasuk.setDate(date);
        
        //Form Barang Keluar
        vBK.tampilDataBarangKeluar();
        vBK.autoNoFakturBarangKeluar();
        tfKodeSupplierBarangKeluar.setVisible(false);
        cBK.kontrolButton();
        dateTanggalBarangKeluar.setDate(date);
 
        //Form Supplier
        vS.tampilDataSupplier();
        vS.autoKodeSupplier();
        cS.kontrolButton();
        
        //Form Kategori
        vK.tampilDataKategori();    
        vK.autoKodeKategori();
        cK.kontrolButton();
        
        //Form User
        vU.tampilDataUser();
        vU.autoKodeUser();
        cU.kontrolButton();
        
        //index
        index();
        
        //tanggalwaktu
        tanggalwaktu();
        
        DigitalWatch();
    }

    
    
    
    public void index() throws IOException, SQLException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String bulan = dateFormat.format(date);
        
        String query1 = "SELECT sum(jumlah) as jumlah from barang_masuk where date(tanggal_masuk) < date('"+bulan+"')";
        Statement stat1 = (Statement) koneksiDatabase.getKoneksi().createStatement();
        ResultSet arraymasuk = stat1.executeQuery(query1);
        int in = 0;
        if (arraymasuk.next()) {
            if (arraymasuk.getString("jumlah") == null) {
                in = 0;
            } else {
                in  = Integer.parseInt(arraymasuk.getString("jumlah"));
            }
        } else in = 0;  
        
        String query2 = "SELECT sum(jumlah) as jumlah from barang_keluar where date(tanggal_keluar) < date('"+bulan+"')";
        Statement stat2 = (Statement) koneksiDatabase.getKoneksi().createStatement();
        ResultSet arraykeluar = stat2.executeQuery(query2);
        int out = 0;
        if (arraykeluar.next()) {
            if (arraykeluar.getString("jumlah") == null) {
                out = 0;
            } else {
                out  = Integer.parseInt(arraykeluar.getString("jumlah"));
            }
        } else out = 0;
        
        String query3 = "SELECT sum(jumlah) as jumlah from barang_masuk where month(tanggal_masuk) = month('"+bulan+"')";
        Statement stat3 = (Statement) koneksiDatabase.getKoneksi().createStatement();
        ResultSet masuk = stat3.executeQuery(query3);
        int barang_masuk = 0;
        if (masuk.next()) {
            if (masuk.getString("jumlah") == null){
                barang_masuk = 0;
            } else {
                barang_masuk = Integer.parseInt(masuk.getString("jumlah"));
            }
        } else barang_masuk = 0;
        
        String query4 = "SELECT sum(jumlah) as jumlah from barang_keluar where month(tanggal_keluar) = month('"+bulan+"')";
        Statement stat4 = (Statement) koneksiDatabase.getKoneksi().createStatement();
        ResultSet keluar = stat4.executeQuery(query4);
        int barang_keluar = 0;
        if (keluar.next()) {
            if (keluar.getString("jumlah") == null){
                barang_keluar = 0;
            } else {
                barang_keluar = Integer.parseInt(keluar.getString("jumlah"));
            }
        } else barang_keluar = 0;
        
        int kapasitas_full = 5000;
        int stok_awal = in - out;
        float bagian = (stok_awal + barang_masuk) - barang_keluar;
        float persentase = (bagian / kapasitas_full) * 100;
        
        lblBarangMasukIndex.setText(String.valueOf(barang_masuk));
        lblBarangKeluarIndex.setText(String.valueOf(barang_keluar));
        lblKapasitasIndex.setText(String.valueOf(persentase) + " %");
    }
    
    
    public void tanggalwaktu(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");   
        String tanggal = dateFormat.format(date);
        lbTanggal.setText(tanggal);
    }

    public JLabel getLbNamaAkun() {
        return lbNamaAkun;
    }

    public JPanel getBtnHome() {
        return btnHome;
    }

    public JPanel getBtnUser() {
        return btnUser;
    }

    public JLabel getLblUserMenu() {
        return lblUserMenu;
    }

    public JPanel getBtnKategori() {
        return btnKategori;
    }

    public JPanel getBtnSupplier() {
        return btnSupplier;
    }

    public JLabel getLblKategori() {
        return lblKategori;
    }

    public JLabel getLblSupplier() {
        return lblSupplier;
    }

    public JLabel getLblSession() {
        return lblSession;
    }

    public JLabel getLblUserID() {
        return lblUserID;
    }

    public JComboBox<String> getCbKategoriBarang() {
        return cbKategoriBarang;
    }

    public JComboBox<String> getCbLevelUser() {
        return cbLevelUser;
    }

    public JComboBox<String> getCbSupplierBarang() {
        return cbSupplierBarang;
    }
    
    public JDateChooser getDateTanggalBarangKeluar() {
        return dateTanggalBarangKeluar;
    }

    public JDateChooser getDateTanggalBarangMasuk() {
        return dateTanggalBarangMasuk;
    }

    public JTextArea getTaAlamatSupplier() {
        return taAlamatSupplier;
    }

    public JTextField getTfNamaSupplierBarangMasuk() {
        return tfNamaSupplierBarangMasuk;
    }

    public JTextField getTfJumlahBarangKeluar() {
        return tfJumlahBarangKeluar;
    }

    public JTextField getTfJumlahBarangMasuk() {
        return tfJumlahBarangMasuk;
    }

    public JTextField getTfKodeBarang() {
        return tfKodeBarang;
    }

    public JTextField getTfKodeBarangKategori() {
        return tfKodeBarangKategori;
    }

    public JTextField getTfKodeSupplier() {
        return tfKodeSupplier;
    }

    public JTextField getTfKodeUser() {
        return tfKodeUser;
    }

    public JTextField getTfNamaBarang() {
        return tfNamaBarang;
    }

    public JTextField getTfNamaBarangKeluar() {
        return tfNamaBarangKeluar;
    }

    public JTextField getTfNamaBarangMasuk() {
        return tfNamaBarangMasuk;
    }

    public JTextField getTfNamaKategori() {
        return tfNamaKategori;
    }

    public JTextField getTfNamaSupplier() {
        return tfNamaSupplier;
    }

    public JTextField getTfNamaUser() {
        return tfNamaUser;
    }

    public JTextField getTfNoFakturBarangKeluar() {
        return tfNoFakturBarangKeluar;
    }

    public JTextField getTfNoFakturBarangMasuk() {
        return tfNoFakturBarangMasuk;
    }

    public JTextField getTfNoRak() {
        return tfNoRak;
    }

    public JTextField getTfNoTelpSupplier() {
        return tfNoTelpSupplier;
    }

    public JTextField getTfSatuanBarang() {
        return tfSatuanBarang;
    }

    public JTextField getTfStokBarang() {
        return tfStokBarang;
    }

    public JTextField getTfSupplierBarangKeluar() {
        return tfSupplierBarangKeluar;
    }

    public JTextField getTfSupplierBarangMasuk() {
        return tfKodeSupplierBarangMasuk;
    }

    public JTextField getTfUsernameUser() {
        return tfUsernameUser;
    }

    public JTextField getTfPasswordUser() {
        return tfPasswordUser;
    }

    public JButton getBtnBatalBarangKeluar() {
        return btnBatalBarangKeluar;
    }

    public JButton getBtnBatalBarangMasuk() {
        return btnBatalBarangMasuk;
    }

    public JButton getBtnBatalDaftarUser() {
        return btnBatalDaftarUser;
    }

    public JButton getBtnBatalDataBarang() {
        return btnBatalDataBarang;
    }

    public JButton getBtnBatalKategori() {
        return btnBatalKategori;
    }

    public JButton getBtnBatalSupplier() {
        return btnBatalSupplier;
    }

    public JButton getBtnEditDaftarUser() {
        return btnEditDaftarUser;
    }

    public JButton getBtnEditDataBarang() {
        return btnEditDataBarang;
    }

    public JButton getBtnEditKategori() {
        return btnEditKategori;
    }

    public JButton getBtnEditSupplier() {
        return btnEditSupplier;
    }

    public JButton getBtnHapusBarangKeluar() {
        return btnHapusBarangKeluar;
    }

    public JButton getBtnHapusBarangMasuk() {
        return btnHapusBarangMasuk;
    }

    public JButton getBtnHapusDaftarUser() {
        return btnHapusDaftarUser;
    }

    public JButton getBtnHapusDataBarang() {
        return btnHapusDataBarang;
    }

    public JButton getBtnHapusKategori() {
        return btnHapusKategori;
    }

    public JButton getBtnHapusSupplier() {
        return btnHapusSupplier;
    }

    public JButton getBtnTambahBarangKeluar() {
        return btnTambahBarangKeluar;
    }

    public JButton getBtnTambahBarangMasuk() {
        return btnTambahBarangMasuk;
    }

    public JButton getBtnTambahDaftarUser() {
        return btnTambahDaftarUser;
    }

    public JButton getBtnTambahDataBarang() {
        return btnTambahDataBarang;
    }

    public JButton getBtnTambahKategori() {
        return btnTambahKategori;
    }

    public JButton getBtnTambahSupplier() {
        return btnTambahSupplier;
    }

    public JTable getTabelDataSupplier() {
        return tabelDataSupplier;
    }

    public JTable getTabelDataBarang() {
        return tabelDataBarang;
    }

    public JTable getTabelBarangKeluar() {
        return tabelBarangKeluar;
    }

    public JTable getTabelBarangMasuk() {
        return tabelBarangMasuk;
    }

    public JTable getTabelDaftarUser() {
        return tabelDaftarUser;
    }
    
    public JTable getTabelDataKategori() {
        return tabelDataKategori;
    }

    public JTextField getTfKodeKategoriBarang() {
        return tfKodeKategoriBarang;
    }

    public JTextField getTfKodeSupplierBarang() {
        return tfKodeSupplierBarang;
    }

    public JButton getBtnCariKodeBarangMasuk() {
        return btnCariKodeBarangMasuk;
    }

    public JComboBox<String> getCbKondisiBarangMasuk() {
        return cbKondisiBarangMasuk;
    }

    public JTextField getTfKodeBarangMasuk() {
        return tfKodeBarangMasuk;
    }

    public JTextField getTfKodeSupplierBarangMasuk() {
        return tfKodeSupplierBarangMasuk;
    }

    public JButton getBtnCariKodeBarangKeluar() {
        return btnCariKodeBarangKeluar;
    }

    public JComboBox<String> getCbKondisiBarangKeluar() {
        return cbKondisiBarangKeluar;
    }

    public JTextArea getTaKeteranganBarangKeluar() {
        return taKeteranganBarangKeluar;
    }

    public JTextField getTfKodeBarangKeluar() {
        return tfKodeBarangKeluar;
    }

    public JTextField getTfKodeSupplierBarangKeluar() {
        return tfKodeSupplierBarangKeluar;
    }

    public JDateChooser getDatePeriodeAkhir() {
        return datePeriodeAkhir;
    }

    public JDateChooser getDatePeriodeAwal() {
        return datePeriodeAwal;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        logo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblSession = new javax.swing.JLabel();
        lblUserID = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnClose = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnMin = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        menu = new javax.swing.JPanel();
        btnHome = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBarang = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnSupplier = new javax.swing.JPanel();
        lblSupplier = new javax.swing.JLabel();
        btnUser = new javax.swing.JPanel();
        lblUserMenu = new javax.swing.JLabel();
        btnLaporan = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        mainMenu = new javax.swing.JPanel();
        homeMenu = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lbNamaAkun = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblBarangMasukIndex = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblBarangKeluarIndex = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        lblKapasitasIndex = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lbWaktu = new javax.swing.JLabel();
        lbTanggal = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        supplierMenu = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        tfKodeSupplier = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        tfNamaSupplier = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        taAlamatSupplier = new javax.swing.JTextArea();
        jLabel38 = new javax.swing.JLabel();
        tfNoTelpSupplier = new javax.swing.JTextField();
        btnTambahSupplier = new javax.swing.JButton();
        btnHapusSupplier = new javax.swing.JButton();
        btnEditSupplier = new javax.swing.JButton();
        btnBatalSupplier = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tabelDataSupplier = new javax.swing.JTable();
        barangMenu = new javax.swing.JPanel();
        btnDataBarang = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnBarangMasuk = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnBarangKeluar = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btnKategori = new javax.swing.JPanel();
        lblKategori = new javax.swing.JLabel();
        userMenu = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnTambahDaftarUser = new javax.swing.JButton();
        btnHapusDaftarUser = new javax.swing.JButton();
        btnEditDaftarUser = new javax.swing.JButton();
        jLabel79 = new javax.swing.JLabel();
        tfKodeUser = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        tfNamaUser = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        tfUsernameUser = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        cbLevelUser = new javax.swing.JComboBox<>();
        btnBatalDaftarUser = new javax.swing.JButton();
        jLabel83 = new javax.swing.JLabel();
        tfPasswordUser = new javax.swing.JPasswordField();
        jScrollPane14 = new javax.swing.JScrollPane();
        tabelDaftarUser = new javax.swing.JTable();
        laporanMenu = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        datePeriodeAwal = new com.toedter.calendar.JDateChooser();
        btnCekStok = new javax.swing.JButton();
        jLabel72 = new javax.swing.JLabel();
        datePeriodeAkhir = new com.toedter.calendar.JDateChooser();
        jLabel47 = new javax.swing.JLabel();
        dataBarangMenu = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        tfKodeBarang = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        tfNamaBarang = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        cbKategoriBarang = new javax.swing.JComboBox<>();
        jLabel45 = new javax.swing.JLabel();
        cbSupplierBarang = new javax.swing.JComboBox<>();
        jLabel50 = new javax.swing.JLabel();
        tfStokBarang = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        tfSatuanBarang = new javax.swing.JTextField();
        btnTambahDataBarang = new javax.swing.JButton();
        btnHapusDataBarang = new javax.swing.JButton();
        btnEditDataBarang = new javax.swing.JButton();
        btnBatalDataBarang = new javax.swing.JButton();
        tfKodeKategoriBarang = new javax.swing.JTextField();
        tfKodeSupplierBarang = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        tabelDataBarang = new javax.swing.JTable();
        barangMasukMenu = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        tfNoFakturBarangMasuk = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        dateTanggalBarangMasuk = new com.toedter.calendar.JDateChooser();
        jLabel54 = new javax.swing.JLabel();
        tfNamaBarangMasuk = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        tfJumlahBarangMasuk = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        tfKodeSupplierBarangMasuk = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        btnTambahBarangMasuk = new javax.swing.JButton();
        btnHapusBarangMasuk = new javax.swing.JButton();
        btnBatalBarangMasuk = new javax.swing.JButton();
        cbKondisiBarangMasuk = new javax.swing.JComboBox<>();
        tfKodeBarangMasuk = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        btnCariKodeBarangMasuk = new javax.swing.JButton();
        tfNamaSupplierBarangMasuk = new javax.swing.JTextField();
        btnCetakBarangMasuk = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        tabelBarangMasuk = new javax.swing.JTable();
        barangKeluarMenu = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        tfNoFakturBarangKeluar = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        dateTanggalBarangKeluar = new com.toedter.calendar.JDateChooser();
        jLabel64 = new javax.swing.JLabel();
        tfNamaBarangKeluar = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        tfJumlahBarangKeluar = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        tfSupplierBarangKeluar = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        btnTambahBarangKeluar = new javax.swing.JButton();
        btnHapusBarangKeluar = new javax.swing.JButton();
        btnBatalBarangKeluar = new javax.swing.JButton();
        cbKondisiBarangKeluar = new javax.swing.JComboBox<>();
        jLabel69 = new javax.swing.JLabel();
        tfKodeBarangKeluar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        taKeteranganBarangKeluar = new javax.swing.JTextArea();
        btnCariKodeBarangKeluar = new javax.swing.JButton();
        tfKodeSupplierBarangKeluar = new javax.swing.JTextField();
        btnCetakBarangKeluar = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        tabelBarangKeluar = new javax.swing.JTable();
        kategoriMenu = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        tfKodeBarangKategori = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        tfNamaKategori = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        tfNoRak = new javax.swing.JTextField();
        btnTambahKategori = new javax.swing.JButton();
        btnHapusKategori = new javax.swing.JButton();
        btnEditKategori = new javax.swing.JButton();
        btnBatalKategori = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        tabelDataKategori = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        header.setBackground(new java.awt.Color(55, 78, 78));
        header.setPreferredSize(new java.awt.Dimension(800, 50));
        header.setLayout(new java.awt.BorderLayout());

        logo.setBackground(new java.awt.Color(55, 78, 78));
        logo.setPreferredSize(new java.awt.Dimension(200, 50));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(254, 193, 47));
        jLabel3.setText("SIGU");

        lblSession.setText("Session");

        lblUserID.setText("UserID");

        javax.swing.GroupLayout logoLayout = new javax.swing.GroupLayout(logo);
        logo.setLayout(logoLayout);
        logoLayout.setHorizontalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(lblSession)
                .addGap(18, 18, 18)
                .addComponent(lblUserID)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        logoLayout.setVerticalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSession)
                    .addComponent(lblUserID))
                .addGap(26, 26, 26))
            .addGroup(logoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        header.add(logo, java.awt.BorderLayout.LINE_START);

        jPanel7.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnClose.setBackground(new java.awt.Color(55, 78, 78));
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new java.awt.Dimension(50, 50));
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCloseMouseExited(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/icons8_delete_32px.png"))); // NOI18N

        javax.swing.GroupLayout btnCloseLayout = new javax.swing.GroupLayout(btnClose);
        btnClose.setLayout(btnCloseLayout);
        btnCloseLayout.setHorizontalGroup(
            btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnCloseLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnCloseLayout.setVerticalGroup(
            btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnCloseLayout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addGap(0, 9, Short.MAX_VALUE)))
        );

        jPanel7.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, -1, -1));

        btnMin.setBackground(new java.awt.Color(55, 78, 78));
        btnMin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMin.setPreferredSize(new java.awt.Dimension(50, 50));
        btnMin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMinMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinMouseExited(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(254, 193, 47));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("-");

        javax.swing.GroupLayout btnMinLayout = new javax.swing.GroupLayout(btnMin);
        btnMin.setLayout(btnMinLayout);
        btnMinLayout.setHorizontalGroup(
            btnMinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnMinLayout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        btnMinLayout.setVerticalGroup(
            btnMinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnMinLayout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel7.add(btnMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        header.add(jPanel7, java.awt.BorderLayout.LINE_END);

        getContentPane().add(header, java.awt.BorderLayout.PAGE_START);

        menu.setBackground(new java.awt.Color(55, 78, 78));
        menu.setPreferredSize(new java.awt.Dimension(50, 450));
        menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnHome.setBackground(new java.awt.Color(55, 78, 78));
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.setPreferredSize(new java.awt.Dimension(50, 50));
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHomeMouseExited(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/icons8_warehouse_32px_1.png"))); // NOI18N

        javax.swing.GroupLayout btnHomeLayout = new javax.swing.GroupLayout(btnHome);
        btnHome.setLayout(btnHomeLayout);
        btnHomeLayout.setHorizontalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnHomeLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnHomeLayout.setVerticalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnHomeLayout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGap(0, 9, Short.MAX_VALUE)))
        );

        menu.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btnBarang.setBackground(new java.awt.Color(55, 78, 78));
        btnBarang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBarang.setPreferredSize(new java.awt.Dimension(50, 50));
        btnBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBarangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBarangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBarangMouseExited(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/icons8_product_32px.png"))); // NOI18N

        javax.swing.GroupLayout btnBarangLayout = new javax.swing.GroupLayout(btnBarang);
        btnBarang.setLayout(btnBarangLayout);
        btnBarangLayout.setHorizontalGroup(
            btnBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnBarangLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnBarangLayout.setVerticalGroup(
            btnBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnBarangLayout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 9, Short.MAX_VALUE)))
        );

        menu.add(btnBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        btnSupplier.setBackground(new java.awt.Color(55, 78, 78));
        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.setPreferredSize(new java.awt.Dimension(50, 50));
        btnSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSupplierMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSupplierMouseExited(evt);
            }
        });

        lblSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/icons8_supplier_32px_1.png"))); // NOI18N

        javax.swing.GroupLayout btnSupplierLayout = new javax.swing.GroupLayout(btnSupplier);
        btnSupplier.setLayout(btnSupplierLayout);
        btnSupplierLayout.setHorizontalGroup(
            btnSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnSupplierLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblSupplier)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnSupplierLayout.setVerticalGroup(
            btnSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnSupplierLayout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addComponent(lblSupplier)
                    .addGap(0, 9, Short.MAX_VALUE)))
        );

        menu.add(btnSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, -1, -1));

        btnUser.setBackground(new java.awt.Color(55, 78, 78));
        btnUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUser.setPreferredSize(new java.awt.Dimension(50, 50));
        btnUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUserMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUserMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUserMouseExited(evt);
            }
        });

        lblUserMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/icons8_contacts_32px_1.png"))); // NOI18N

        javax.swing.GroupLayout btnUserLayout = new javax.swing.GroupLayout(btnUser);
        btnUser.setLayout(btnUserLayout);
        btnUserLayout.setHorizontalGroup(
            btnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnUserLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblUserMenu)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnUserLayout.setVerticalGroup(
            btnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnUserLayout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addComponent(lblUserMenu)
                    .addGap(0, 9, Short.MAX_VALUE)))
        );

        menu.add(btnUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, -1, -1));

        btnLaporan.setBackground(new java.awt.Color(55, 78, 78));
        btnLaporan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLaporan.setPreferredSize(new java.awt.Dimension(50, 50));
        btnLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLaporanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLaporanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLaporanMouseExited(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/icons8_print_32px_1.png"))); // NOI18N

        javax.swing.GroupLayout btnLaporanLayout = new javax.swing.GroupLayout(btnLaporan);
        btnLaporan.setLayout(btnLaporanLayout);
        btnLaporanLayout.setHorizontalGroup(
            btnLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnLaporanLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnLaporanLayout.setVerticalGroup(
            btnLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnLaporanLayout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addGap(0, 9, Short.MAX_VALUE)))
        );

        menu.add(btnLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, -1, -1));

        btnLogout.setBackground(new java.awt.Color(55, 78, 78));
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new java.awt.Dimension(50, 50));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoutMouseExited(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/icons8_logout_rounded_left_32px.png"))); // NOI18N

        javax.swing.GroupLayout btnLogoutLayout = new javax.swing.GroupLayout(btnLogout);
        btnLogout.setLayout(btnLogoutLayout);
        btnLogoutLayout.setHorizontalGroup(
            btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnLogoutLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel11)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnLogoutLayout.setVerticalGroup(
            btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnLogoutLayout.createSequentialGroup()
                    .addGap(0, 9, Short.MAX_VALUE)
                    .addComponent(jLabel11)
                    .addGap(0, 9, Short.MAX_VALUE)))
        );

        menu.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, -1));

        getContentPane().add(menu, java.awt.BorderLayout.LINE_START);

        mainMenu.setBackground(new java.awt.Color(182, 205, 205));
        mainMenu.setLayout(new java.awt.CardLayout());

        homeMenu.setBackground(new java.awt.Color(182, 205, 205));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(55, 78, 78));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Hi,");

        lbNamaAkun.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbNamaAkun.setForeground(new java.awt.Color(55, 78, 78));
        lbNamaAkun.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbNamaAkun.setText("Nama User");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(55, 78, 78));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("SELAMAT DATANG DI");

        jPanel8.setBackground(new java.awt.Color(182, 205, 205));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(55, 78, 78)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(55, 78, 78));
        jLabel14.setText("BARANG MASUK (PERBULAN)");
        jPanel8.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        lblBarangMasukIndex.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        lblBarangMasukIndex.setForeground(new java.awt.Color(55, 78, 78));
        lblBarangMasukIndex.setText("0");
        jPanel8.add(lblBarangMasukIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/btnRekap.png"))); // NOI18N
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, -1, -1));

        jPanel9.setBackground(new java.awt.Color(182, 205, 205));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(55, 78, 78)));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(55, 78, 78));
        jLabel15.setText("BARANG KELUAR (PERBULAN)");
        jPanel9.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        lblBarangKeluarIndex.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        lblBarangKeluarIndex.setForeground(new java.awt.Color(55, 78, 78));
        lblBarangKeluarIndex.setText("0");
        jPanel9.add(lblBarangKeluarIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/btnRekap.png"))); // NOI18N
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, -1, -1));

        jPanel10.setBackground(new java.awt.Color(182, 205, 205));
        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(55, 78, 78)));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(55, 78, 78));
        jLabel18.setText("KAPASITAS GUDANG");
        jPanel10.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        lblKapasitasIndex.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        lblKapasitasIndex.setForeground(new java.awt.Color(55, 78, 78));
        lblKapasitasIndex.setText("0");
        jPanel10.add(lblKapasitasIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/btnRekap.png"))); // NOI18N
        jPanel10.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, -1, -1));

        lbWaktu.setBackground(new java.awt.Color(55, 78, 78));
        lbWaktu.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbWaktu.setForeground(new java.awt.Color(55, 78, 78));
        lbWaktu.setText("WAKTU");

        lbTanggal.setBackground(new java.awt.Color(55, 78, 78));
        lbTanggal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lbTanggal.setForeground(new java.awt.Color(55, 78, 78));
        lbTanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTanggal.setText("TANGGAL");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(55, 78, 78));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("SISTEM INFORMASI GUDANG");

        javax.swing.GroupLayout homeMenuLayout = new javax.swing.GroupLayout(homeMenu);
        homeMenu.setLayout(homeMenuLayout);
        homeMenuLayout.setHorizontalGroup(
            homeMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeMenuLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(homeMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, homeMenuLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))
                    .addGroup(homeMenuLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbNamaAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeMenuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbWaktu)
                .addGap(80, 80, 80))
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(homeMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        homeMenuLayout.setVerticalGroup(
            homeMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeMenuLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(homeMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(lbNamaAkun))
                    .addComponent(lbTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbWaktu)
                .addGap(24, 24, 24)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addGap(69, 69, 69)
                .addGroup(homeMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(homeMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        mainMenu.add(homeMenu, "card9");

        supplierMenu.setBackground(new java.awt.Color(182, 205, 205));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(55, 78, 78));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("DATA SUPPLIER");

        jPanel1.setBackground(new java.awt.Color(214, 212, 175));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(55, 78, 78), 1, true));

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(55, 78, 78));
        jLabel35.setText("Kode Supplier");

        tfKodeSupplier.setEnabled(false);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(55, 78, 78));
        jLabel36.setText("Nama Supplier");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(55, 78, 78));
        jLabel37.setText("Alamat");

        taAlamatSupplier.setColumns(20);
        taAlamatSupplier.setRows(5);
        jScrollPane7.setViewportView(taAlamatSupplier);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(55, 78, 78));
        jLabel38.setText("No Telp");

        btnTambahSupplier.setBackground(new java.awt.Color(55, 78, 78));
        btnTambahSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahSupplier.setText("TAMBAH");
        btnTambahSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahSupplierActionPerformed(evt);
            }
        });

        btnHapusSupplier.setBackground(new java.awt.Color(55, 78, 78));
        btnHapusSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusSupplier.setText("HAPUS");
        btnHapusSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusSupplierActionPerformed(evt);
            }
        });

        btnEditSupplier.setBackground(new java.awt.Color(55, 78, 78));
        btnEditSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEditSupplier.setText("EDIT");
        btnEditSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSupplierActionPerformed(evt);
            }
        });

        btnBatalSupplier.setBackground(new java.awt.Color(55, 78, 78));
        btnBatalSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBatalSupplier.setText("BATAL");
        btnBatalSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel35))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfNamaSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                                    .addComponent(tfKodeSupplier)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel38))
                                .addGap(78, 78, 78)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfNoTelpSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTambahSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnEditSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnBatalSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(tfKodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(tfNamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel38))
                    .addComponent(tfNoTelpSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahSupplier)
                    .addComponent(btnHapusSupplier)
                    .addComponent(btnEditSupplier)
                    .addComponent(btnBatalSupplier))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        tabelDataSupplier.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelDataSupplier.setMaximumSize(new java.awt.Dimension(2147483647, 323));
        tabelDataSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataSupplierMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tabelDataSupplier);

        javax.swing.GroupLayout supplierMenuLayout = new javax.swing.GroupLayout(supplierMenu);
        supplierMenu.setLayout(supplierMenuLayout);
        supplierMenuLayout.setHorizontalGroup(
            supplierMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(supplierMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(supplierMenuLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)))
                .addContainerGap())
        );
        supplierMenuLayout.setVerticalGroup(
            supplierMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addGap(18, 18, 18)
                .addGroup(supplierMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        mainMenu.add(supplierMenu, "card2");

        barangMenu.setBackground(new java.awt.Color(182, 205, 205));

        btnDataBarang.setBackground(new java.awt.Color(182, 205, 205));
        btnDataBarang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDataBarang.setPreferredSize(new java.awt.Dimension(267, 116));
        btnDataBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDataBarangMouseClicked(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/btnDataBarang.png"))); // NOI18N

        javax.swing.GroupLayout btnDataBarangLayout = new javax.swing.GroupLayout(btnDataBarang);
        btnDataBarang.setLayout(btnDataBarangLayout);
        btnDataBarangLayout.setHorizontalGroup(
            btnDataBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnDataBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnDataBarangLayout.createSequentialGroup()
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        btnDataBarangLayout.setVerticalGroup(
            btnDataBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnDataBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnDataBarangLayout.createSequentialGroup()
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        btnBarangMasuk.setBackground(new java.awt.Color(182, 205, 205));
        btnBarangMasuk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBarangMasuk.setPreferredSize(new java.awt.Dimension(267, 116));
        btnBarangMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBarangMasukMouseClicked(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/btnBarangMasuk.png"))); // NOI18N
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout btnBarangMasukLayout = new javax.swing.GroupLayout(btnBarangMasuk);
        btnBarangMasuk.setLayout(btnBarangMasukLayout);
        btnBarangMasukLayout.setHorizontalGroup(
            btnBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnBarangMasukLayout.createSequentialGroup()
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        btnBarangMasukLayout.setVerticalGroup(
            btnBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnBarangMasukLayout.createSequentialGroup()
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        btnBarangKeluar.setBackground(new java.awt.Color(182, 205, 205));
        btnBarangKeluar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBarangKeluar.setPreferredSize(new java.awt.Dimension(267, 116));
        btnBarangKeluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBarangKeluarMouseClicked(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/btnBarangKeluar.png"))); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout btnBarangKeluarLayout = new javax.swing.GroupLayout(btnBarangKeluar);
        btnBarangKeluar.setLayout(btnBarangKeluarLayout);
        btnBarangKeluarLayout.setHorizontalGroup(
            btnBarangKeluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnBarangKeluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnBarangKeluarLayout.createSequentialGroup()
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        btnBarangKeluarLayout.setVerticalGroup(
            btnBarangKeluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnBarangKeluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnBarangKeluarLayout.createSequentialGroup()
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        btnKategori.setBackground(new java.awt.Color(182, 205, 205));
        btnKategori.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKategori.setPreferredSize(new java.awt.Dimension(267, 116));
        btnKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKategoriMouseClicked(evt);
            }
        });

        lblKategori.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasisigu/image/btnKategori.png"))); // NOI18N
        lblKategori.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout btnKategoriLayout = new javax.swing.GroupLayout(btnKategori);
        btnKategori.setLayout(btnKategoriLayout);
        btnKategoriLayout.setHorizontalGroup(
            btnKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnKategoriLayout.createSequentialGroup()
                    .addComponent(lblKategori, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        btnKategoriLayout.setVerticalGroup(
            btnKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(btnKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnKategoriLayout.createSequentialGroup()
                    .addComponent(lblKategori, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout barangMenuLayout = new javax.swing.GroupLayout(barangMenu);
        barangMenu.setLayout(barangMenuLayout);
        barangMenuLayout.setHorizontalGroup(
            barangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barangMenuLayout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addGroup(barangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(115, 115, 115)
                .addGroup(barangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(296, Short.MAX_VALUE))
        );
        barangMenuLayout.setVerticalGroup(
            barangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barangMenuLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(barangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(barangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        mainMenu.add(barangMenu, "card8");

        userMenu.setBackground(new java.awt.Color(182, 205, 205));

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(55, 78, 78));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("DAFTAR USER");

        jPanel6.setBackground(new java.awt.Color(214, 212, 175));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(55, 78, 78), 1, true));

        btnTambahDaftarUser.setBackground(new java.awt.Color(55, 78, 78));
        btnTambahDaftarUser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahDaftarUser.setText("TAMBAH");
        btnTambahDaftarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahDaftarUserActionPerformed(evt);
            }
        });

        btnHapusDaftarUser.setBackground(new java.awt.Color(55, 78, 78));
        btnHapusDaftarUser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusDaftarUser.setText("HAPUS");
        btnHapusDaftarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusDaftarUserActionPerformed(evt);
            }
        });

        btnEditDaftarUser.setBackground(new java.awt.Color(55, 78, 78));
        btnEditDaftarUser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEditDaftarUser.setText("EDIT");
        btnEditDaftarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditDaftarUserActionPerformed(evt);
            }
        });

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(55, 78, 78));
        jLabel79.setText("Kode User");

        tfKodeUser.setEnabled(false);

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(55, 78, 78));
        jLabel80.setText("Nama");

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(55, 78, 78));
        jLabel81.setText("Username");

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(55, 78, 78));
        jLabel82.setText("Level");

        cbLevelUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Data --", "admin", "user" }));

        btnBatalDaftarUser.setBackground(new java.awt.Color(55, 78, 78));
        btnBatalDaftarUser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBatalDaftarUser.setText("BATAL");
        btnBatalDaftarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalDaftarUserActionPerformed(evt);
            }
        });

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(55, 78, 78));
        jLabel83.setText("Password");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(jLabel80)
                    .addComponent(jLabel81)
                    .addComponent(jLabel82)
                    .addComponent(jLabel83))
                .addGap(55, 55, 55)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tfUsernameUser, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfNamaUser, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfKodeUser, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbLevelUser, 0, 150, Short.MAX_VALUE)
                    .addComponent(tfPasswordUser))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnTambahDaftarUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusDaftarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEditDaftarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBatalDaftarUser, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(tfKodeUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(tfNamaUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(tfUsernameUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(tfPasswordUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(cbLevelUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahDaftarUser)
                    .addComponent(btnHapusDaftarUser)
                    .addComponent(btnEditDaftarUser)
                    .addComponent(btnBatalDaftarUser))
                .addGap(44, 44, 44))
        );

        tabelDaftarUser.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelDaftarUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarUserMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tabelDaftarUser);

        javax.swing.GroupLayout userMenuLayout = new javax.swing.GroupLayout(userMenu);
        userMenu.setLayout(userMenuLayout);
        userMenuLayout.setHorizontalGroup(
            userMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(userMenuLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)))
                .addContainerGap())
        );
        userMenuLayout.setVerticalGroup(
            userMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addGap(18, 18, 18)
                .addGroup(userMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        mainMenu.add(userMenu, "card7");

        laporanMenu.setBackground(new java.awt.Color(182, 205, 205));

        jPanel14.setBackground(new java.awt.Color(214, 212, 175));
        jPanel14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(55, 78, 78), 1, true));

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(55, 78, 78));
        jLabel71.setText("Periode Awal");

        btnCekStok.setBackground(new java.awt.Color(55, 78, 78));
        btnCekStok.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCekStok.setText("CEK STOK");
        btnCekStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCekStokActionPerformed(evt);
            }
        });

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(55, 78, 78));
        jLabel72.setText("Periode Akhir");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCekStok, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel71)
                            .addComponent(jLabel72))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(datePeriodeAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(datePeriodeAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(59, 59, 59))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel71)
                    .addComponent(datePeriodeAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72)
                    .addComponent(datePeriodeAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(btnCekStok)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(55, 78, 78));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("INFORMASI STOK BARANG");

        javax.swing.GroupLayout laporanMenuLayout = new javax.swing.GroupLayout(laporanMenu);
        laporanMenu.setLayout(laporanMenuLayout);
        laporanMenuLayout.setHorizontalGroup(
            laporanMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, laporanMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 1151, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(laporanMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(laporanMenuLayout.createSequentialGroup()
                    .addGap(379, 379, 379)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(380, Short.MAX_VALUE)))
        );
        laporanMenuLayout.setVerticalGroup(
            laporanMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(laporanMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addContainerGap(384, Short.MAX_VALUE))
            .addGroup(laporanMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(laporanMenuLayout.createSequentialGroup()
                    .addGap(75, 75, 75)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(149, Short.MAX_VALUE)))
        );

        mainMenu.add(laporanMenu, "card10");

        dataBarangMenu.setBackground(new java.awt.Color(182, 205, 205));
        dataBarangMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataBarangMenuMouseClicked(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(55, 78, 78));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("DATA BARANG");

        jPanel2.setBackground(new java.awt.Color(214, 212, 175));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(55, 78, 78), 1, true));

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(55, 78, 78));
        jLabel42.setText("Kode Barang");

        tfKodeBarang.setEnabled(false);

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(55, 78, 78));
        jLabel43.setText("Nama Barang");

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(55, 78, 78));
        jLabel44.setText("Kategori");

        cbKategoriBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Data --" }));
        cbKategoriBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbKategoriBarangMouseClicked(evt);
            }
        });
        cbKategoriBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKategoriBarangActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(55, 78, 78));
        jLabel45.setText("Supplier");

        cbSupplierBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Data --" }));
        cbSupplierBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSupplierBarangActionPerformed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(55, 78, 78));
        jLabel50.setText("Stok");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(55, 78, 78));
        jLabel51.setText("Satuan");

        btnTambahDataBarang.setBackground(new java.awt.Color(55, 78, 78));
        btnTambahDataBarang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahDataBarang.setText("TAMBAH");
        btnTambahDataBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahDataBarangActionPerformed(evt);
            }
        });

        btnHapusDataBarang.setBackground(new java.awt.Color(55, 78, 78));
        btnHapusDataBarang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusDataBarang.setText("HAPUS");
        btnHapusDataBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusDataBarangActionPerformed(evt);
            }
        });

        btnEditDataBarang.setBackground(new java.awt.Color(55, 78, 78));
        btnEditDataBarang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEditDataBarang.setText("EDIT");
        btnEditDataBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditDataBarangActionPerformed(evt);
            }
        });

        btnBatalDataBarang.setBackground(new java.awt.Color(55, 78, 78));
        btnBatalDataBarang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBatalDataBarang.setText("BATAL");
        btnBatalDataBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalDataBarangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel43)
                            .addComponent(jLabel44)
                            .addComponent(jLabel45)
                            .addComponent(jLabel50)
                            .addComponent(jLabel51))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfSatuanBarang)
                            .addComponent(tfStokBarang)
                            .addComponent(tfNamaBarang)
                            .addComponent(tfKodeBarang)
                            .addComponent(cbKategoriBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbSupplierBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfKodeKategoriBarang, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                            .addComponent(tfKodeSupplierBarang))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTambahDataBarang)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusDataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditDataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(btnBatalDataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(tfKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(tfNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(cbKategoriBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfKodeKategoriBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(cbSupplierBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfKodeSupplierBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(tfStokBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(tfSatuanBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahDataBarang)
                    .addComponent(btnHapusDataBarang)
                    .addComponent(btnEditDataBarang)
                    .addComponent(btnBatalDataBarang))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        tabelDataBarang.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelDataBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataBarangMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tabelDataBarang);

        javax.swing.GroupLayout dataBarangMenuLayout = new javax.swing.GroupLayout(dataBarangMenu);
        dataBarangMenu.setLayout(dataBarangMenuLayout);
        dataBarangMenuLayout.setHorizontalGroup(
            dataBarangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataBarangMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataBarangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dataBarangMenuLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dataBarangMenuLayout.setVerticalGroup(
            dataBarangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataBarangMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addGroup(dataBarangMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        mainMenu.add(dataBarangMenu, "card3");

        barangMasukMenu.setBackground(new java.awt.Color(182, 205, 205));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(55, 78, 78));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("BARANG MASUK");

        jPanel3.setBackground(new java.awt.Color(214, 212, 175));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(55, 78, 78), 1, true));

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(55, 78, 78));
        jLabel52.setText("No Faktur");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(55, 78, 78));
        jLabel53.setText("Tanggal Masuk");

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(55, 78, 78));
        jLabel54.setText("Nama Barang");

        tfNamaBarangMasuk.setEnabled(false);

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(55, 78, 78));
        jLabel55.setText("Jumlah");

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(55, 78, 78));
        jLabel56.setText("Supplier");

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(55, 78, 78));
        jLabel57.setText("Kondisi");

        btnTambahBarangMasuk.setBackground(new java.awt.Color(55, 78, 78));
        btnTambahBarangMasuk.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahBarangMasuk.setText("TAMBAH");
        btnTambahBarangMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBarangMasukActionPerformed(evt);
            }
        });

        btnHapusBarangMasuk.setBackground(new java.awt.Color(55, 78, 78));
        btnHapusBarangMasuk.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusBarangMasuk.setText("HAPUS");
        btnHapusBarangMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusBarangMasukActionPerformed(evt);
            }
        });

        btnBatalBarangMasuk.setBackground(new java.awt.Color(55, 78, 78));
        btnBatalBarangMasuk.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBatalBarangMasuk.setText("BATAL");
        btnBatalBarangMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalBarangMasukActionPerformed(evt);
            }
        });

        cbKondisiBarangMasuk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Kondisi Barang --", "OK", "NG" }));

        tfKodeBarangMasuk.setEnabled(false);

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(55, 78, 78));
        jLabel58.setText("Kode Barang");

        btnCariKodeBarangMasuk.setText("...");
        btnCariKodeBarangMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKodeBarangMasukActionPerformed(evt);
            }
        });

        tfNamaSupplierBarangMasuk.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel52)
                                            .addComponent(jLabel53)
                                            .addComponent(jLabel55)
                                            .addComponent(jLabel56)
                                            .addComponent(jLabel57))
                                        .addGap(55, 55, 55)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(cbKondisiBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING, 0, 164, Short.MAX_VALUE)
                                            .addComponent(tfJumlahBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfKodeBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfNamaBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfNoFakturBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dateTanggalBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(tfNamaSupplierBarangMasuk)))
                                    .addComponent(jLabel58))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfKodeSupplierBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCariKodeBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(btnTambahBarangMasuk)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBatalBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(tfKodeBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariKodeBarangMasuk))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(tfNamaBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel52)
                            .addComponent(tfNoFakturBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel53))
                    .addComponent(dateTanggalBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(tfJumlahBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(tfKodeSupplierBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfNamaSupplierBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(cbKondisiBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahBarangMasuk)
                    .addComponent(btnHapusBarangMasuk)
                    .addComponent(btnBatalBarangMasuk))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        btnCetakBarangMasuk.setBackground(new java.awt.Color(55, 78, 78));
        btnCetakBarangMasuk.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCetakBarangMasuk.setText("CETAK");
        btnCetakBarangMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakBarangMasukActionPerformed(evt);
            }
        });

        tabelBarangMasuk.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelBarangMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelBarangMasukMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tabelBarangMasuk);

        javax.swing.GroupLayout barangMasukMenuLayout = new javax.swing.GroupLayout(barangMasukMenu);
        barangMasukMenu.setLayout(barangMasukMenuLayout);
        barangMasukMenuLayout.setHorizontalGroup(
            barangMasukMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barangMasukMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(barangMasukMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(barangMasukMenuLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barangMasukMenuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCetakBarangMasuk)))
                .addContainerGap())
        );
        barangMasukMenuLayout.setVerticalGroup(
            barangMasukMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barangMasukMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(barangMasukMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCetakBarangMasuk)
                .addContainerGap())
        );

        mainMenu.add(barangMasukMenu, "card4");

        barangKeluarMenu.setBackground(new java.awt.Color(182, 205, 205));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(55, 78, 78));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("BARANG KELUAR");

        jPanel4.setBackground(new java.awt.Color(214, 212, 175));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(55, 78, 78), 1, true));

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(55, 78, 78));
        jLabel62.setText("No Faktur");

        tfNoFakturBarangKeluar.setEnabled(false);

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(55, 78, 78));
        jLabel63.setText("Tanggal Keluar");

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(55, 78, 78));
        jLabel64.setText("Nama Barang");

        tfNamaBarangKeluar.setEnabled(false);

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(55, 78, 78));
        jLabel65.setText("Jumlah");

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(55, 78, 78));
        jLabel66.setText("Supplier");

        tfSupplierBarangKeluar.setEnabled(false);

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(55, 78, 78));
        jLabel67.setText("Keterangan");

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(55, 78, 78));
        jLabel68.setText("Kondisi");

        btnTambahBarangKeluar.setBackground(new java.awt.Color(55, 78, 78));
        btnTambahBarangKeluar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahBarangKeluar.setText("TAMBAH");
        btnTambahBarangKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBarangKeluarActionPerformed(evt);
            }
        });

        btnHapusBarangKeluar.setBackground(new java.awt.Color(55, 78, 78));
        btnHapusBarangKeluar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusBarangKeluar.setText("HAPUS");
        btnHapusBarangKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusBarangKeluarActionPerformed(evt);
            }
        });

        btnBatalBarangKeluar.setBackground(new java.awt.Color(55, 78, 78));
        btnBatalBarangKeluar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBatalBarangKeluar.setText("BATAL");
        btnBatalBarangKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalBarangKeluarActionPerformed(evt);
            }
        });

        cbKondisiBarangKeluar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Kondisi Barang --", "OK", "NG" }));

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(55, 78, 78));
        jLabel69.setText("Kode Barang");

        tfKodeBarangKeluar.setEnabled(false);

        taKeteranganBarangKeluar.setColumns(20);
        taKeteranganBarangKeluar.setRows(5);
        jScrollPane1.setViewportView(taKeteranganBarangKeluar);

        btnCariKodeBarangKeluar.setText("...");
        btnCariKodeBarangKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKodeBarangKeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel65)
                                    .addComponent(jLabel66)
                                    .addComponent(jLabel67))
                                .addGap(74, 74, 74))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel68)
                                .addGap(101, 101, 101)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfSupplierBarangKeluar)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(tfJumlahBarangKeluar)
                            .addComponent(cbKondisiBarangKeluar, 0, 154, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfKodeSupplierBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63)
                            .addComponent(jLabel64)
                            .addComponent(jLabel69))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(tfKodeBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCariKodeBarangKeluar))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tfNamaBarangKeluar)
                                .addComponent(tfNoFakturBarangKeluar)
                                .addComponent(dateTanggalBarangKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(btnTambahBarangKeluar)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBatalBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(tfKodeBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariKodeBarangKeluar))
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(tfNamaBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel62)
                            .addComponent(tfNoFakturBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel63))
                    .addComponent(dateTanggalBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(tfJumlahBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(tfSupplierBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfKodeSupplierBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(cbKondisiBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahBarangKeluar)
                    .addComponent(btnHapusBarangKeluar)
                    .addComponent(btnBatalBarangKeluar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCetakBarangKeluar.setBackground(new java.awt.Color(55, 78, 78));
        btnCetakBarangKeluar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCetakBarangKeluar.setText("CETAK");
        btnCetakBarangKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakBarangKeluarActionPerformed(evt);
            }
        });

        tabelBarangKeluar.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelBarangKeluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelBarangKeluarMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tabelBarangKeluar);

        javax.swing.GroupLayout barangKeluarMenuLayout = new javax.swing.GroupLayout(barangKeluarMenu);
        barangKeluarMenu.setLayout(barangKeluarMenuLayout);
        barangKeluarMenuLayout.setHorizontalGroup(
            barangKeluarMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barangKeluarMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(barangKeluarMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barangKeluarMenuLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barangKeluarMenuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCetakBarangKeluar)))
                .addContainerGap())
        );
        barangKeluarMenuLayout.setVerticalGroup(
            barangKeluarMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barangKeluarMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addGap(18, 18, 18)
                .addGroup(barangKeluarMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCetakBarangKeluar)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        mainMenu.add(barangKeluarMenu, "card5");

        kategoriMenu.setBackground(new java.awt.Color(182, 205, 205));

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(55, 78, 78));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("DATA KATEGORI");

        jPanel5.setBackground(new java.awt.Color(214, 212, 175));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(55, 78, 78), 1, true));

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(55, 78, 78));
        jLabel73.setText("Kode Kategori");

        tfKodeBarangKategori.setEnabled(false);

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(55, 78, 78));
        jLabel74.setText("Nama Kategori");

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(55, 78, 78));
        jLabel75.setText("No Rak");

        btnTambahKategori.setBackground(new java.awt.Color(55, 78, 78));
        btnTambahKategori.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambahKategori.setText("TAMBAH");
        btnTambahKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahKategoriActionPerformed(evt);
            }
        });

        btnHapusKategori.setBackground(new java.awt.Color(55, 78, 78));
        btnHapusKategori.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusKategori.setText("HAPUS");
        btnHapusKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusKategoriActionPerformed(evt);
            }
        });

        btnEditKategori.setBackground(new java.awt.Color(55, 78, 78));
        btnEditKategori.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEditKategori.setText("EDIT");
        btnEditKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditKategoriActionPerformed(evt);
            }
        });

        btnBatalKategori.setBackground(new java.awt.Color(55, 78, 78));
        btnBatalKategori.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBatalKategori.setText("BATAL");
        btnBatalKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalKategoriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(jLabel74)
                            .addComponent(jLabel75))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfKodeBarangKategori)
                            .addComponent(tfNamaKategori)
                            .addComponent(tfNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTambahKategori)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBatalKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(tfKodeBarangKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(tfNamaKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(tfNoRak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahKategori)
                    .addComponent(btnHapusKategori)
                    .addComponent(btnEditKategori)
                    .addComponent(btnBatalKategori))
                .addGap(49, 49, 49))
        );

        tabelDataKategori.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelDataKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataKategoriMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tabelDataKategori);

        javax.swing.GroupLayout kategoriMenuLayout = new javax.swing.GroupLayout(kategoriMenu);
        kategoriMenu.setLayout(kategoriMenuLayout);
        kategoriMenuLayout.setHorizontalGroup(
            kategoriMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kategoriMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kategoriMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(kategoriMenuLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)))
                .addContainerGap())
        );
        kategoriMenuLayout.setVerticalGroup(
            kategoriMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kategoriMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addGap(18, 18, 18)
                .addGroup(kategoriMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        mainMenu.add(kategoriMenu, "card6");

        getContentPane().add(mainMenu, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        btnHome.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseExited
        btnHome.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnHomeMouseExited

    private void btnSupplierMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseEntered
        btnSupplier.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnSupplierMouseEntered

    private void btnSupplierMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseExited
        btnSupplier.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnSupplierMouseExited

    private void btnBarangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBarangMouseEntered
        btnBarang.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnBarangMouseEntered

    private void btnBarangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBarangMouseExited
        btnBarang.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnBarangMouseExited

    private void btnUserMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserMouseEntered
        btnUser.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnUserMouseEntered

    private void btnUserMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserMouseExited
        btnUser.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnUserMouseExited

    private void btnLaporanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLaporanMouseEntered
        btnLaporan.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnLaporanMouseEntered

    private void btnLaporanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLaporanMouseExited
        btnLaporan.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnLaporanMouseExited

    private void btnCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseEntered
        btnClose.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnCloseMouseEntered

    private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseExited
        btnClose.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnCloseMouseExited

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        mainMenu.add(homeMenu);
        mainMenu.repaint();
        mainMenu.revalidate();
        
        try {
            index();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBarangMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        mainMenu.add(barangMenu);
        mainMenu.repaint();
        mainMenu.revalidate();
    }//GEN-LAST:event_btnBarangMouseClicked

    private void btnSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        if (lblSession.getText().equals("admin")) {
            mainMenu.add(supplierMenu);
            mainMenu.repaint();
            mainMenu.revalidate();
        } else {
            JOptionPane.showMessageDialog(null, "ANDA BUKAN ADMIN", "Message", JOptionPane.ERROR_MESSAGE);
            mainMenu.add(homeMenu);
            mainMenu.repaint();
            mainMenu.revalidate();
        }
        
    }//GEN-LAST:event_btnSupplierMouseClicked

    private void btnUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        if (lblSession.getText().equals("admin")) {
            mainMenu.add(userMenu);
            mainMenu.repaint();
            mainMenu.revalidate();
        } else {
            JOptionPane.showMessageDialog(null, "ANDA BUKAN ADMIN", "Message", JOptionPane.ERROR_MESSAGE);
            mainMenu.add(homeMenu);
            mainMenu.repaint();
            mainMenu.revalidate();
        }
        
    }//GEN-LAST:event_btnUserMouseClicked

    private void btnLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLaporanMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        mainMenu.add(laporanMenu);
        mainMenu.repaint();
        mainMenu.revalidate();
    }//GEN-LAST:event_btnLaporanMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        System.exit(0);
    }//GEN-LAST:event_btnCloseMouseClicked
    
    private void ambilDataCbKategoriBarang() throws IOException {
        cbKategoriBarang.removeAllItems();
        cbKategoriBarang.addItem("-- Pilih Data --");
        cbKategoriBarang.setSelectedIndex(0);
        vB.dataKategoriToComboBox();
    }
    //public String selectedValue;
    private void ambilDataCbSupplierBarang() throws IOException {
        cbSupplierBarang.removeAllItems();
        cbSupplierBarang.addItem("-- Pilih Data --");
        cbSupplierBarang.setSelectedIndex(0);
        vB.dataSupplierToComboBox();
    }
    
    private void btnDataBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDataBarangMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        mainMenu.add(dataBarangMenu);
        mainMenu.repaint();
        mainMenu.revalidate();
        
        try {
            vB.tampilDataBarang();
            vB.autoKodeBarang();                                                            
            ambilDataCbKategoriBarang();
            ambilDataCbSupplierBarang();   
            
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDataBarangMouseClicked

    private void btnBarangMasukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBarangMasukMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        mainMenu.add(barangMasukMenu);
        mainMenu.repaint();
        mainMenu.revalidate();
    }//GEN-LAST:event_btnBarangMasukMouseClicked

    private void btnBarangKeluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBarangKeluarMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        mainMenu.add(barangKeluarMenu);
        mainMenu.repaint();
        mainMenu.revalidate();
    }//GEN-LAST:event_btnBarangKeluarMouseClicked

    private void btnKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKategoriMouseClicked
        //remove panel
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();
        
        //add panel
        if (lblSession.getText().equals("admin")) {
            mainMenu.add(kategoriMenu);
            mainMenu.repaint();
            mainMenu.revalidate();
        } else {
            JOptionPane.showMessageDialog(null, "ANDA BUKAN ADMIN", "Message", JOptionPane.ERROR_MESSAGE);
            mainMenu.add(barangMenu);
            mainMenu.repaint();
            mainMenu.revalidate();
        }
    }//GEN-LAST:event_btnKategoriMouseClicked

    private void btnTambahDataBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDataBarangActionPerformed
        // TODO add your handling code here:
        if (tfNamaBarang.getText().equals("") || cbKategoriBarang.getSelectedIndex() == 0 || 
                cbSupplierBarang.getSelectedIndex() == 0 || tfStokBarang.getText().equals("") || 
                tfSatuanBarang.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Harap Isi Semua Data !!!", "Message" , JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                cB.simpanDataBarang();
                vB.tampilDataBarang();
                cB.kontrolButton();
                cB.bersihBarang();
                vB.autoKodeBarang();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnTambahDataBarangActionPerformed

    private void btnTambahSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahSupplierActionPerformed
       // TODO add your handling code here:
            if (tfNamaSupplier.getText().equals("") || taAlamatSupplier.getText().equals("") || tfNoTelpSupplier.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Harap Isi Semua Data !!!", "Message" , JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    cS.simpanDataSupplier();
                    vS.tampilDataSupplier();
                    vS.autoKodeSupplier();
                    cS.kontrolButton();
                    cS.bersihSupplier();
                } catch (IOException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }//GEN-LAST:event_btnTambahSupplierActionPerformed

    private void btnBatalSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalSupplierActionPerformed
        // TODO add your handling code here:
        cS.kontrolButton();
        cS.bersihSupplier();
        try {
            vS.autoKodeSupplier();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBatalSupplierActionPerformed

    private void tabelDataSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataSupplierMouseClicked
        // TODO add your handling code here:
        vS.ambilDataSupplier();
        cS.kontrolButtonDua();
    }//GEN-LAST:event_tabelDataSupplierMouseClicked

    private void btnEditSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSupplierActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Mengedit Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
            try {
                cS.ubahDataSupplier();
                vS.tampilDataSupplier();
                cS.kontrolButton();
                cS.bersihSupplier();
                vS.autoKodeSupplier();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnEditSupplierActionPerformed

    private void btnHapusSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusSupplierActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Menghapus Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
            cS.hapusDataSupplier();
            try {
                vS.tampilDataSupplier();
                cS.kontrolButton();
                cS.bersihSupplier();
                vS.autoKodeSupplier();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnHapusSupplierActionPerformed

    private void tabelDataKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataKategoriMouseClicked
        // TODO add your handling code here:
        vK.ambilDataKategori();
        cK.kontrolButtonDua();
    }//GEN-LAST:event_tabelDataKategoriMouseClicked

    private void btnTambahKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahKategoriActionPerformed
        // TODO add your handling code here:
        if (tfNamaKategori.getText().equals("") || tfNoRak.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Harap Isi Semua Data !!!", "Message" , JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                cK.simpanDataKategori();
                vK.tampilDataKategori();
                cK.kontrolButton();
                cK.bersihKategori();
                vK.autoKodeKategori();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnTambahKategoriActionPerformed

    private void btnBatalKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalKategoriActionPerformed
        // TODO add your handling code here:
        cK.kontrolButton();
        cK.bersihKategori();
        try {
            vK.autoKodeKategori();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBatalKategoriActionPerformed

    private void btnEditKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditKategoriActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Mengedit Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
            try {
                cK.ubahDataKategori();
                vK.tampilDataKategori();
                cK.kontrolButton();
                cK.bersihKategori();
                vK.autoKodeKategori();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnEditKategoriActionPerformed

    private void btnHapusKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusKategoriActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Menghapus Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
            cK.hapusDataKategori();
            try {
                vK.tampilDataKategori();
                cK.kontrolButton();
                cK.bersihKategori();
                vK.autoKodeKategori();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnHapusKategoriActionPerformed

    private void tabelDataBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataBarangMouseClicked
        // TODO add your handling code here:
        vB.ambilDataBarang();
        cB.kontrolButtonDua();
    }//GEN-LAST:event_tabelDataBarangMouseClicked
    
    private void cbKategoriBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKategoriBarangActionPerformed
        // TODO add your handling code here: 
        String selectedValue = (String) cbKategoriBarang.getSelectedItem();

        String sql = "SELECT kode_kategori from kategori where nama_kategori='"+selectedValue+"'";

        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);

            if (res.next()) {
                tfKodeKategoriBarang.setText(res.getString("kode_kategori"));
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbKategoriBarangActionPerformed

    private void cbSupplierBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSupplierBarangActionPerformed
        // TODO add your handling code here:
        String selectedValue = (String) cbSupplierBarang.getSelectedItem();
        
        String sql = "SELECT kode_supplier from supplier where nama_supplier='"+selectedValue+"'";
        
        try {
            Statement stat = (Statement) koneksiDatabase.getKoneksi().createStatement();
            ResultSet res = stat.executeQuery(sql);
            
            if (res.next()) {
                tfKodeSupplierBarang.setText(res.getString("kode_supplier"));
            }
            
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_cbSupplierBarangActionPerformed

    private void btnHapusDataBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusDataBarangActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Menghapus Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
            try {
                cB.hapusDataBarang();
                vB.tampilDataBarang();
                cB.kontrolButton();
                cB.bersihBarang();
                vB.autoKodeBarang();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnHapusDataBarangActionPerformed

    private void btnEditDataBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDataBarangActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Mengedit Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
            try {
                cB.ubahDataBarang();
                vB.tampilDataBarang();
                cB.kontrolButton();
                cB.bersihBarang();
                vB.autoKodeBarang();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnEditDataBarangActionPerformed

    private void btnBatalDataBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalDataBarangActionPerformed
        try {
            // TODO add your handling code here:
            vB.tampilDataBarang();
            cB.kontrolButton();
            cB.bersihBarang();
            vB.autoKodeBarang();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBatalDataBarangActionPerformed

    private void tabelBarangMasukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelBarangMasukMouseClicked
        try {
            // TODO add your handling code here:
            vBM.ambilDataBarangMasuk();
            cBM.kontrolButtonDua();
            tfNoFakturBarangMasuk.setEnabled(false);
            dateTanggalBarangMasuk.setEnabled(false);
            tfJumlahBarangMasuk.setEnabled(false);
            cbKondisiBarangMasuk.setEnabled(false);
            btnCariKodeBarangMasuk.setEnabled(false);
        } catch (ParseException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tabelBarangMasukMouseClicked

    private void btnCariKodeBarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKodeBarangMasukActionPerformed
        // TODO add your handling code here:
        try {
            vLDBM.setVisible(true);
            vLDBM.tampilDataListBarang("");
            //this.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCariKodeBarangMasukActionPerformed

    private void btnBatalBarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalBarangMasukActionPerformed
        try {
            // TODO add your handling code here:
            cBM.bersihBarangMasuk();
            cBM.kontrolButton();
            vBM.tampilDataBarangMasuk();
            tfNoFakturBarangMasuk.setEnabled(true);
            dateTanggalBarangMasuk.setEnabled(true);
            tfJumlahBarangMasuk.setEnabled(true);
            cbKondisiBarangMasuk.setEnabled(true);
            btnCariKodeBarangMasuk.setEnabled(true);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBatalBarangMasukActionPerformed

    private void btnTambahBarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBarangMasukActionPerformed
        // TODO add your handling code here:
        if (tfNoFakturBarangMasuk.getText().equals("") || dateTanggalBarangMasuk.getDate().equals("") || 
                tfJumlahBarangMasuk.getText().equals("") || cbKondisiBarangMasuk.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Harap Isi Semua Data !!!", "Message" , JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                cBM.simpanDataBarangMasuk();
                vBM.tampilDataBarangMasuk();
            } catch (IOException | ParseException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnTambahBarangMasukActionPerformed

    private void btnCariKodeBarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKodeBarangKeluarActionPerformed
        // TODO add your handling code here:
        try {
            vLDBK.setVisible(true);
            vLDBK.tampilDataListBarang("");
            //this.setEnabled(false);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCariKodeBarangKeluarActionPerformed

    private void btnBatalBarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalBarangKeluarActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            cBK.bersihBarangKeluar();
            cBK.kontrolButton();
            vBK.autoNoFakturBarangKeluar();
            vBK.tampilDataBarangKeluar();
            dateTanggalBarangKeluar.setEnabled(true);
            tfJumlahBarangKeluar.setEnabled(true);
            cbKondisiBarangKeluar.setEnabled(true);
            taKeteranganBarangKeluar.setEnabled(true);
            btnCariKodeBarangKeluar.setEnabled(true);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBatalBarangKeluarActionPerformed

    private void tabelBarangKeluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelBarangKeluarMouseClicked
        // TODO add your handling code here:
        try {
            vBK.ambilDataBarangKeluar();
            cBK.kontrolButtonDua();            
            dateTanggalBarangKeluar.setEnabled(false);
            tfJumlahBarangKeluar.setEnabled(false);
            cbKondisiBarangKeluar.setEnabled(false);
            taKeteranganBarangKeluar.setEnabled(false);
            btnCariKodeBarangKeluar.setEnabled(false);
        } catch (ParseException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tabelBarangKeluarMouseClicked

    private void btnTambahBarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBarangKeluarActionPerformed
        // TODO add your handling code here:
        if (dateTanggalBarangKeluar.getDate().equals("") || tfJumlahBarangKeluar.getText().equals("") || 
                taKeteranganBarangKeluar.getText().equals("") || cbKondisiBarangKeluar.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Harap Isi Semua Data !!!", "Message" , JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                cBK.simpanDataBarangKeluar();
                vBK.tampilDataBarangKeluar();
                vBK.autoNoFakturBarangKeluar();
            } catch (IOException | SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnTambahBarangKeluarActionPerformed

    private void btnHapusBarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusBarangMasukActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Menghapus Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
           try {
            cBM.hapusDataBarangMasuk();
            cBM.kontrolButton();
            cBM.bersihBarangMasuk();
            tfNoFakturBarangMasuk.setEnabled(true);
            dateTanggalBarangMasuk.setEnabled(true);
            tfJumlahBarangMasuk.setEnabled(true);
            cbKondisiBarangMasuk.setEnabled(true);
            btnCariKodeBarangMasuk.setEnabled(true);
            vBM.tampilDataBarangMasuk();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }//GEN-LAST:event_btnHapusBarangMasukActionPerformed

    private void btnHapusBarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusBarangKeluarActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Menghapus Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
          try {            
            cBK.hapusDataBarangKeluar();
            cBK.kontrolButton();
            cBK.bersihBarangKeluar();
            dateTanggalBarangKeluar.setEnabled(true);
            tfJumlahBarangKeluar.setEnabled(true);
            cbKondisiBarangKeluar.setEnabled(true);
            taKeteranganBarangKeluar.setEnabled(true);
            btnCariKodeBarangKeluar.setEnabled(true);
            vBK.tampilDataBarangKeluar();
            vBK.autoNoFakturBarangKeluar();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
    }//GEN-LAST:event_btnHapusBarangKeluarActionPerformed

    private void btnTambahDaftarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDaftarUserActionPerformed
        // TODO add your handling code here:
        if (tfNamaUser.getText().equals("") || tfUsernameUser.getText().equals("") || tfPasswordUser.getText().equals("") || cbLevelUser.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Harap Isi Semua Data !!!", "Message" , JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                cU.simpanDataUser();
                vU.tampilDataUser();
                vU.autoKodeUser();
            } catch (IOException | SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnTambahDaftarUserActionPerformed

    private void btnHapusDaftarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusDaftarUserActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Menghapus Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
           try {
            cU.hapusDataUser();
            cU.kontrolButton();
            cU.bersihUser();
            vU.tampilDataUser();
            vU.autoKodeUser();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }//GEN-LAST:event_btnHapusDaftarUserActionPerformed

    private void btnEditDaftarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDaftarUserActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Mengedit Data", "Message", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
           try {
                cU.ubahDataUser();
                vU.tampilDataUser();
                cU.kontrolButton();
                cU.bersihUser();
                vU.autoKodeUser();
                tfUsernameUser.setEnabled(true);
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }//GEN-LAST:event_btnEditDaftarUserActionPerformed

    private void btnBatalDaftarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalDaftarUserActionPerformed
        // TODO add your handling code here:
        try {
            vU.tampilDataUser();
            cU.kontrolButton();
            cU.bersihUser();
            vU.autoKodeUser();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBatalDaftarUserActionPerformed

    private void tabelDaftarUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarUserMouseClicked
        // TODO add your handling code here:
        vU.ambilDataUser();
        cU.kontrolButtonDua();
    }//GEN-LAST:event_tabelDaftarUserMouseClicked

    private void btnCetakBarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakBarangMasukActionPerformed
        // TODO add your handling code here:
        try {
            tabelBarangMasuk.print(JTable.PrintMode.FIT_WIDTH, new MessageFormat("BARANG MASUK"), null);
        } catch (PrinterException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE,null,ex);
        }
    }//GEN-LAST:event_btnCetakBarangMasukActionPerformed

    private void btnCekStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCekStokActionPerformed
        
            // TODO add your handling code here:
            if (getDatePeriodeAwal().getDate() == null || getDatePeriodeAkhir().getDate() == null) {
                JOptionPane.showMessageDialog(null, "Harap Isi Semua Data !!!", "Message", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    Date pAwal = getDatePeriodeAwal().getDate();
                    Date pAkhir = getDatePeriodeAkhir().getDate();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String periodeAwal = dateFormat.format(pAwal);
                    String periodeAkhir = dateFormat.format(pAkhir);
                    DateFormat dateFormatTampil = new SimpleDateFormat("dd MMMM yyyy");
                    String periodeAwalTampil = dateFormatTampil.format(pAwal);
                    String periodeAkhirTampil = dateFormatTampil.format(pAkhir);
                    vLSB.getLblPeriodeAwalView().setText(periodeAwalTampil);
                    vLSB.getLblPeriodeAkhirView().setText(periodeAkhirTampil);
                    vLSB.setPeriodeAwal(periodeAwal);
                    vLSB.setPeriodeAkhir(periodeAkhir);
                    vLSB.tampilLaporanStokBarang();
                    vLSB.setVisible(true);
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }//GEN-LAST:event_btnCekStokActionPerformed

    private void btnCetakBarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakBarangKeluarActionPerformed
        // TODO add your handling code here:
        try {
            tabelBarangKeluar.print(JTable.PrintMode.FIT_WIDTH, new MessageFormat("BARANG KELUAR"), null);
        } catch (PrinterException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE,null,ex);
        }
    }//GEN-LAST:event_btnCetakBarangKeluarActionPerformed

    private void btnMinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseClicked
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_btnMinMouseClicked

    private void btnMinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseEntered
        btnMin.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnMinMouseEntered

    private void btnMinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseExited
        btnMin.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnMinMouseExited

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Logout", "Konfirmasi Logout", 
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi == 0) {
            Login lo = new Login();
            lo.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        btnLogout.setBackground(new Color(30,53,53));
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        btnLogout.setBackground(new Color(55,78,78));
    }//GEN-LAST:event_btnLogoutMouseExited

    private void dataBarangMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataBarangMenuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dataBarangMenuMouseClicked

    private void cbKategoriBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbKategoriBarangMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cbKategoriBarangMouseClicked

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    new Home().setVisible(true);
//                } catch (IOException ex) {
//                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barangKeluarMenu;
    private javax.swing.JPanel barangMasukMenu;
    private javax.swing.JPanel barangMenu;
    private javax.swing.JPanel btnBarang;
    private javax.swing.JPanel btnBarangKeluar;
    private javax.swing.JPanel btnBarangMasuk;
    private javax.swing.JButton btnBatalBarangKeluar;
    private javax.swing.JButton btnBatalBarangMasuk;
    private javax.swing.JButton btnBatalDaftarUser;
    private javax.swing.JButton btnBatalDataBarang;
    private javax.swing.JButton btnBatalKategori;
    private javax.swing.JButton btnBatalSupplier;
    private javax.swing.JButton btnCariKodeBarangKeluar;
    private javax.swing.JButton btnCariKodeBarangMasuk;
    private javax.swing.JButton btnCekStok;
    private javax.swing.JButton btnCetakBarangKeluar;
    private javax.swing.JButton btnCetakBarangMasuk;
    private javax.swing.JPanel btnClose;
    private javax.swing.JPanel btnDataBarang;
    private javax.swing.JButton btnEditDaftarUser;
    private javax.swing.JButton btnEditDataBarang;
    private javax.swing.JButton btnEditKategori;
    private javax.swing.JButton btnEditSupplier;
    private javax.swing.JButton btnHapusBarangKeluar;
    private javax.swing.JButton btnHapusBarangMasuk;
    private javax.swing.JButton btnHapusDaftarUser;
    private javax.swing.JButton btnHapusDataBarang;
    private javax.swing.JButton btnHapusKategori;
    private javax.swing.JButton btnHapusSupplier;
    private javax.swing.JPanel btnHome;
    private javax.swing.JPanel btnKategori;
    private javax.swing.JPanel btnLaporan;
    private javax.swing.JPanel btnLogout;
    private javax.swing.JPanel btnMin;
    private javax.swing.JPanel btnSupplier;
    private javax.swing.JButton btnTambahBarangKeluar;
    private javax.swing.JButton btnTambahBarangMasuk;
    private javax.swing.JButton btnTambahDaftarUser;
    private javax.swing.JButton btnTambahDataBarang;
    private javax.swing.JButton btnTambahKategori;
    private javax.swing.JButton btnTambahSupplier;
    private javax.swing.JPanel btnUser;
    private javax.swing.JComboBox<String> cbKategoriBarang;
    private javax.swing.JComboBox<String> cbKondisiBarangKeluar;
    private javax.swing.JComboBox<String> cbKondisiBarangMasuk;
    private javax.swing.JComboBox<String> cbLevelUser;
    private javax.swing.JComboBox<String> cbSupplierBarang;
    private javax.swing.JPanel dataBarangMenu;
    private com.toedter.calendar.JDateChooser datePeriodeAkhir;
    private com.toedter.calendar.JDateChooser datePeriodeAwal;
    private com.toedter.calendar.JDateChooser dateTanggalBarangKeluar;
    private com.toedter.calendar.JDateChooser dateTanggalBarangMasuk;
    private javax.swing.JPanel header;
    private javax.swing.JPanel homeMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPanel kategoriMenu;
    private javax.swing.JPanel laporanMenu;
    private javax.swing.JLabel lbNamaAkun;
    private javax.swing.JLabel lbTanggal;
    private javax.swing.JLabel lbWaktu;
    private javax.swing.JLabel lblBarangKeluarIndex;
    private javax.swing.JLabel lblBarangMasukIndex;
    private javax.swing.JLabel lblKapasitasIndex;
    private javax.swing.JLabel lblKategori;
    private javax.swing.JLabel lblSession;
    private javax.swing.JLabel lblSupplier;
    private javax.swing.JLabel lblUserID;
    private javax.swing.JLabel lblUserMenu;
    private javax.swing.JPanel logo;
    private javax.swing.JPanel mainMenu;
    private javax.swing.JPanel menu;
    private javax.swing.JPanel supplierMenu;
    private javax.swing.JTextArea taAlamatSupplier;
    private javax.swing.JTextArea taKeteranganBarangKeluar;
    private javax.swing.JTable tabelBarangKeluar;
    private javax.swing.JTable tabelBarangMasuk;
    private javax.swing.JTable tabelDaftarUser;
    private javax.swing.JTable tabelDataBarang;
    private javax.swing.JTable tabelDataKategori;
    private javax.swing.JTable tabelDataSupplier;
    private javax.swing.JTextField tfJumlahBarangKeluar;
    private javax.swing.JTextField tfJumlahBarangMasuk;
    private javax.swing.JTextField tfKodeBarang;
    private javax.swing.JTextField tfKodeBarangKategori;
    private javax.swing.JTextField tfKodeBarangKeluar;
    private javax.swing.JTextField tfKodeBarangMasuk;
    private javax.swing.JTextField tfKodeKategoriBarang;
    private javax.swing.JTextField tfKodeSupplier;
    private javax.swing.JTextField tfKodeSupplierBarang;
    private javax.swing.JTextField tfKodeSupplierBarangKeluar;
    private javax.swing.JTextField tfKodeSupplierBarangMasuk;
    private javax.swing.JTextField tfKodeUser;
    private javax.swing.JTextField tfNamaBarang;
    private javax.swing.JTextField tfNamaBarangKeluar;
    private javax.swing.JTextField tfNamaBarangMasuk;
    private javax.swing.JTextField tfNamaKategori;
    private javax.swing.JTextField tfNamaSupplier;
    private javax.swing.JTextField tfNamaSupplierBarangMasuk;
    private javax.swing.JTextField tfNamaUser;
    private javax.swing.JTextField tfNoFakturBarangKeluar;
    private javax.swing.JTextField tfNoFakturBarangMasuk;
    private javax.swing.JTextField tfNoRak;
    private javax.swing.JTextField tfNoTelpSupplier;
    private javax.swing.JPasswordField tfPasswordUser;
    private javax.swing.JTextField tfSatuanBarang;
    private javax.swing.JTextField tfStokBarang;
    private javax.swing.JTextField tfSupplierBarangKeluar;
    private javax.swing.JTextField tfUsernameUser;
    private javax.swing.JPanel userMenu;
    // End of variables declaration//GEN-END:variables
}
