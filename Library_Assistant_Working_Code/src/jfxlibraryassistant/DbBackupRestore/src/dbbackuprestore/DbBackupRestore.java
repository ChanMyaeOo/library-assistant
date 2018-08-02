/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbbackuprestore;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author ayman
 */
public class DbBackupRestore {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            
            MainFrame mainFrame = new MainFrame();
            mainFrame.setTitle("Backup&&Restore");
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setResizable(false);
            mainFrame.setVisible(true);
            
        } catch (Exception e) {
        }
        
    }
    
}
