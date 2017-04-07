/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Fudolig
 */
public class MyConnection {
    private static final String conn_string = "jdbc:mysql://102c:3306/fudolig_it321la";
    public MyConnection(){        
    }
    public Connection getConnection(){
        Connection con = null;
        
        
        try{
            con = DriverManager.getConnection(conn_string,"fudolig_it321la","");
            //System.out.println("Connected");
            //Statement stmt = 
        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
}
