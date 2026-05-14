import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class FoodOrderingApp {

    // Data
    private static final java.util.List<FoodItem> MENU = new java.util.ArrayList<>();
    private static final ArrayList<FoodItem> CART = new ArrayList<>();
    private static User CUSTOMER = null;
    private static int TOTAL_TK = 0;

    public static void main(String[] args) {
        // Build the menu list
        createMenu();
        // Start login window
        SwingUtilities.invokeLater(FoodOrderingApp::showLoginWindow);
    }

    private static void createMenu() {
        MENU.add(new FoodItem("Chicken Burger", 150));
        MENU.add(new FoodItem("Beef Burger", 180));
        MENU.add(new FoodItem("Chicken Sandwich", 120));
        MENU.add(new FoodItem("Beef Sandwich", 140));
        MENU.add(new FoodItem("Beef Samosa", 50));
        MENU.add(new FoodItem("Chicken Samosa", 40));
        MENU.add(new FoodItem("Coca-Cola", 30));
        MENU.add(new FoodItem("Sprite", 30));
        MENU.add(new FoodItem("Shawarma", 200));
    }

    // ===================== LOGIN =====================
    private static void showLoginWindow() {
        JFrame frame = new JFrame("Login - Food Ordering App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 2, 8, 8));

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        frame.add(new JLabel("Username:"));
        frame.add(username);
        frame.add(new JLabel("Password:"));
        frame.add(password);
        frame.add(exitBtn);
        frame.add(loginBtn);

        // Exit
        exitBtn.addActionListener(e -> System.exit(0));

        // Login
        loginBtn.addActionListener(e -> {
            String u = username.getText().trim();
            String p = new String(password.getPassword());
            if (u.equals("admin") && p.equals("123")) {
                // Ask if they want to order now
                int ans = JOptionPane.showConfirmDialog(frame, "Do you want to order now?", "Order",
                        JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    showCustomerForm();
                } else {
                    // Stay on login page
                    JOptionPane.showMessageDialog(frame, "You are still on the login page.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!");
            }
        });

        frame.setVisible(true);
    }

    // ===================== CUSTOMER DETAILS =====================
    private static void showCustomerForm() {
        JFrame frame = new JFrame("Customer Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 2, 8, 8));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton backBtn = new JButton("Back");
        JButton proceedBtn = new JButton("Proceed");

        frame.add(new JLabel("Name:"));
        frame.add(nameField);
        frame.add(new JLabel("Phone:"));
        frame.add(phoneField);
        frame.add(backBtn);
        frame.add(proceedBtn);

        backBtn.addActionListener(e -> {
            frame.dispose();
            showLoginWindow();
        });

        proceedBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both name and phone.");
                return;
            }
            CUSTOMER = new User(name, phone);
            CART.clear();
            TOTAL_TK = 0;
            frame.dispose();
            showMenuWindow();
        });

        frame.setVisible(true);
    }

    // ===================== MENU WINDOW =====================
    private static void showMenuWindow() {
        JFrame frame = new JFrame("Menu - Food Ordering App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 420);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(8, 8));

        // Left: Menu list (no scrollbars, simple JList)
        DefaultListModel<FoodItem> menuModel = new DefaultListModel<>();
        for (FoodItem fi : MENU) menuModel.addElement(fi);
        JList<FoodItem> menuList = new JList<>(menuModel);
        menuList.setVisibleRowCount(10);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Menu"));
        leftPanel.add(menuList, BorderLayout.CENTER);

        // Right: Cart list (no scrollbars)
        DefaultListModel<FoodItem> cartModel = new DefaultListModel<>();
        JList<FoodItem> cartList = new JList<>(cartModel);
        cartList.setVisibleRowCount(10);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Cart"));
        rightPanel.add(cartList, BorderLayout.CENTER);

        // Center container for two columns
        JPanel center = new JPanel(new GridLayout(1, 2, 8, 8));
        center.add(leftPanel);
        center.add(rightPanel);

        // Bottom controls
        JButton addBtn = new JButton("Add to Cart");
        JButton removeBtn = new JButton("Remove from Cart");
        JButton finishBtn = new JButton("Finish Order");
        JButton logoutBtn = new JButton("Logout");
        JLabel totalLabel = new JLabel("Total: Tk 0");

        JPanel bottom = new JPanel();
        bottom.add(addBtn);
        bottom.add(removeBtn);
        bottom.add(finishBtn);
        bottom.add(logoutBtn);
        bottom.add(totalLabel);

        // Add to frame
        frame.add(center, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        // Actions
        addBtn.addActionListener(e -> {
            FoodItem selected = menuList.getSelectedValue();
            if (selected != null) {
                CART.add(selected);
                cartModel.addElement(selected);
                TOTAL_TK += selected.getPrice();
                totalLabel.setText("Total: Tk " + TOTAL_TK);
            }
        });

        removeBtn.addActionListener(e -> {
            FoodItem selected = cartList.getSelectedValue();
            if (selected != null) {
                CART.remove(selected);
                cartModel.removeElement(selected);
                TOTAL_TK -= selected.getPrice();
                if (TOTAL_TK < 0) TOTAL_TK = 0;
                totalLabel.setText("Total: Tk " + TOTAL_TK);
            }
        });

        finishBtn.addActionListener(e -> {
            if (CART.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Your cart is empty.");
                return;
            }
            frame.dispose();
            showReceiptWindow();
        });

        logoutBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                frame.dispose();
                showLoginWindow();
            }
        });

        frame.setVisible(true);
    }

    // ===================== RECEIPT WINDOW =====================
    private static void showReceiptWindow() {
        JFrame frame = new JFrame("Receipt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 420);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(8, 8));

        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("==== RECEIPT ====").append(System.lineSeparator());
        sb.append("Customer: ").append(CUSTOMER.getName()).append(System.lineSeparator());
        sb.append("Phone   : ").append(CUSTOMER.getPhone()).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Items:").append(System.lineSeparator());
        for (FoodItem item : CART) {
            sb.append("- ").append(item.getName()).append(" : Tk ").append(item.getPrice()).append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        sb.append("Total Bill: Tk ").append(TOTAL_TK).append(System.lineSeparator());

        area.setText(sb.toString());

        JButton saveBtn = new JButton("Save Receipt & Logout");

        saveBtn.addActionListener(e -> {
            String file = Receipt.saveReceipt(CUSTOMER, CART, TOTAL_TK);
            if (file != null) {
                JOptionPane.showMessageDialog(frame, "Receipt saved as " + file + "\nLogging out now.");
            } else {
                JOptionPane.showMessageDialog(frame, "Could not save receipt file.");
            }
            frame.dispose();
            // Return to login
            showLoginWindow();
        });

        frame.add(area, BorderLayout.CENTER);
        frame.add(saveBtn, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
