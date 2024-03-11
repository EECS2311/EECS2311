package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.List;


import database.DB; 

public class Notification extends JFrame {

//	private JFrame frame = HomeView.getFrame();

    public Notification() {
        initializeUI();
        
 
        displayExpiringItemsNotification();
    }
    public static void main(String[] args) {
        new Notification(); // Run your application
    }
    
    private void initializeUI() {
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void displayExpiringItemsNotification() {
        // Create an instance of our DB class
        DB db = new DB();
        
        List<String> expiringItems = db.getExpiringItems(); // get expiring items 

        if (!expiringItems.isEmpty()) {
            
            StringBuilder message = new StringBuilder("The following items are expiring soon:\n");//message string to display
            for (String item : expiringItems) {
                message.append("- ").append(item).append("\n");
            }
            // Display the message in a JOptionPane dialog
            JOptionPane.showMessageDialog(this, message.toString(), "Expiring Items Alert", JOptionPane.WARNING_MESSAGE);
        } else {
           
            JOptionPane.showMessageDialog(this, "No expiring items found.", "Expiring Items Alert", JOptionPane.INFORMATION_MESSAGE);  // If there are no expiring item
        }
    }

   
}
