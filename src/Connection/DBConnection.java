package Connection;

import java.sql.DriverManager;
import java.sql.Connection;


public class DBConnection {
    public static Connection CON;
    public static final String URL = "jdbc:mysql://";
    public static final String DBNAME = "klinik_atma_sehat";
    public static final String PATH = "localhost:3306/"+DBNAME;
    
    public Connection makeConnection(){
        System.out.println("Opening database...");
        try{
            CON = DriverManager.getConnection(URL + PATH, "root", "");
            System.out.println("SUCCESS");
        } catch (Exception e){
            System.out.println("Error opening database");
            System.out.println(e);
        }
        return CON;
    }
    
    public void closeConnection(){
        System.out.println("Closing database...");
        try{
            CON.close();
            System.out.println("Success...");
        }catch(Exception e){
            System.out.println("Error Closing database");
            System.out.println(e);
        }
    }
}
