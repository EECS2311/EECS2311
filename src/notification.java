

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;






// this is for notification not fully integrated into the pantry 
class FoodItem {
    String name;
    LocalDate expirationDate;

    public FoodItem(String name, LocalDate expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public boolean isExpiringSoon() {
        // i set the timer to be 3 days before the actual expiration date 
        return expirationDate.isBefore(LocalDate.now().plusDays(7));
    }
}

class Pantry {
    List<FoodItem> foodItems;

    public Pantry() {
        this.foodItems = new ArrayList<>();
    }

    public void addFoodItem(FoodItem item) {
        foodItems.add(item);
    }

    public List<FoodItem> getExpiringFoodItems() {
        List<FoodItem> expiringItems = new ArrayList<>();
        for (FoodItem item : foodItems) {
            if (item.isExpiringSoon()) {
                expiringItems.add(item);
            }
        }
        return expiringItems;
    }
}

public class Notification {
    private Pantry pantry;

    public Notification() {
        pantry = new Pantry();
        initializeGUI();
    }

    private void initializeGUI() {
        JFrame frame = new JFrame("Pantry Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton openPantryButton = new JButton("Open Pantry");
        openPantryButton.setBounds(50, 20, 200, 25);
        openPantryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<FoodItem> expiringItems = pantry.getExpiringFoodItems();
                StringBuilder message = new StringBuilder();
                if (!expiringItems.isEmpty()) {
                    for (FoodItem item : expiringItems) {
                        message.append(item.name).append(" is expiring soon!\n");
                    }
                } else {
                    message.append("No items are expiring soon.");
                }
                JOptionPane.showMessageDialog(null, message.toString());
            }
        });

        panel.add(openPantryButton);

        // Simulate adding some food items to the pantry
        pantry.addFoodItem(new FoodItem("Milk", LocalDate.now().plusDays(50)));
        pantry.addFoodItem(new FoodItem("Bread", LocalDate.now().plusDays(5)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Notification();
            }
        });
    }
}

