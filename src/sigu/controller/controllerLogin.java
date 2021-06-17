/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigu.controller;

import aplikasisigu.Home;
import aplikasisigu.Login;
import java.io.IOException;
import sigu.model.modelLogin;

/**
 *
 * @author Luky Mulana
 */
public class controllerLogin {
    private modelLogin mL;
    private Login lo;

    public controllerLogin(Login lo) {
        this.lo = lo;
    }
    
    public void loginUser() throws IOException {
        mL = new modelLogin();
        
        mL.setUsername(lo.getTfUsername().getText());
        mL.setPassword(lo.getTfPassword().getText());
        
        mL.login();
    }
}
