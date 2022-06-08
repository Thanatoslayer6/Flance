import java.util.Scanner;

public class Credentials {
    private static String email, pwd;
    public String getEmail() {    
        return email;
    }
    public String getPwd() {    
        return pwd;
    }
    public static void setEmail(String email) {
        Credentials.email = email;
    }
    public static void setPwd(String pwd) {    
        Credentials.pwd = pwd;
    }

    Credentials() {
    	Scanner getm = new Scanner(System.in);
    	System.out.print("Enter your Google Email: ");
        setEmail(getm.nextLine());
    	System.out.print("Enter your Password: ");
    	setPwd(getm.nextLine());
        getm.close();
    }
}
