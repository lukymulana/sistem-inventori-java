/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.controller;

import aplikasisigu.Home;
import java.io.IOException;
import java.sql.SQLException;
import sigu.model.modelUser;

/**
 *
 * @author Kakang Rizal
 */
public class controllerUser {
    private modelUser mU;
    private Home ho;

    public controllerUser(Home ho) {
        this.ho = ho;
    }
    
    public void bersihUser() {
        ho.getTfKodeUser().setText("");
        ho.getTfUsernameUser().setText("");
        ho.getTfPasswordUser().setText("");
        ho.getTfNamaUser().setText("");
        ho.getCbLevelUser().setSelectedIndex(0);
        ho.getTfUsernameUser().setEnabled(true);
    }
    
    public void kontrolButton() {
        ho.getBtnTambahDaftarUser().setEnabled(true);
        ho.getBtnHapusDaftarUser().setEnabled(false);
        ho.getBtnEditDaftarUser().setEnabled(false);
        ho.getBtnBatalDaftarUser().setEnabled(true);
    }
    
    public void kontrolButtonDua() {
        ho.getBtnTambahDaftarUser().setEnabled(false);
        ho.getBtnHapusDaftarUser().setEnabled(true);
        ho.getBtnEditDaftarUser().setEnabled(true);
        ho.getBtnBatalDaftarUser().setEnabled(true);
    }
    
    public void simpanDataUser() throws IOException, SQLException{
        mU = new modelUser();
        
        mU.setKodeUserModel(ho.getTfKodeUser().getText());
        mU.setUsernameUserModel(ho.getTfUsernameUser().getText());
        mU.setPasswordUserModel(ho.getTfPasswordUser().getText());
        mU.setNamaUserModel(ho.getTfNamaUser().getText());
        mU.setLevelUserModel(ho.getCbLevelUser().getSelectedItem().toString());
        
        mU.simpanDataUser();
        bersihUser();
        kontrolButton();
    }
    
    public void ubahDataUser() throws IOException{
        mU = new modelUser();
        
        mU.setKodeUserModel(ho.getTfKodeUser().getText());
        mU.setUsernameUserModel(ho.getTfUsernameUser().getText());
        mU.setPasswordUserModel(ho.getTfPasswordUser().getText());
        mU.setNamaUserModel(ho.getTfNamaUser().getText());
        mU.setLevelUserModel(ho.getCbLevelUser().getSelectedItem().toString());
        
        mU.ubahDataUser();
        bersihUser();
        kontrolButton();
    }
    
    public void hapusDataUser() throws IOException{
        mU = new modelUser();
        
        mU.setKodeUserModel(ho.getTfKodeUser().getText());
        
        mU.hapusDataUser();
        bersihUser();
        kontrolButton();
    }
}
