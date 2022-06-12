package com.Thanatoslayer6;
import java.util.Scanner;

public class CredentialsLogin {
    private static String email, pwd;
    public String getEmail() {    
        return email;
    }
    public String getPwd() {    
        return pwd;
    }
    public static void setEmail(String email) {
        CredentialsLogin.email = email;
    }
    public static void setPwd(String pwd) {    
        CredentialsLogin.pwd = pwd;
    }

    CredentialsLogin() {
    	Scanner getm = new Scanner(System.in);
    	System.out.print("Enter your Google Email: ");
        setEmail(getm.nextLine());
    	System.out.print("Enter your Password: ");
    	setPwd(getm.nextLine());
        getm.close();
    }
}
