/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Fudolig
 */
public class Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       //     for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
       //         if ("Nimbus".equals(info.getName())) {
       //             UIManager.setLookAndFeel(info.getClassName());
       //             break;
       // }
        } 
        catch (UnsupportedLookAndFeelException e) {
           // handle exception
        }
        catch (ClassNotFoundException e) {
           // handle exception
        }
        catch (InstantiationException e) {
           // handle exception
        }
        catch (IllegalAccessException e) {
           // handle exception
        }
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
        //MainFrame frame = new MainFrame();
        //frame.setVisible(true);
    }
    
}
