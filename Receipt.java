import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Receipt {

    // Saves receipt to a simple .txt file in the project folder
    public static String saveReceipt(User user, ArrayList<FoodItem> cart, int totalTk) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==== FOOD ORDER RECEIPT ====" + System.lineSeparator());
        receipt.append("Customer: ").append(user.getName()).append(System.lineSeparator());
        receipt.append("Phone: ").append(user.getPhone()).append(System.lineSeparator()).append(System.lineSeparator());
        receipt.append("Items:" + System.lineSeparator());
        for (FoodItem item : cart) {
            receipt.append("- ").append(item.getName())
                   .append(" : Tk ").append(item.getPrice())
                   .append(System.lineSeparator());
        }
        receipt.append(System.lineSeparator());
        receipt.append("Total Bill: Tk ").append(totalTk).append(System.lineSeparator());

        // timestamped filename to avoid overwriting
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "receipt_" + timestamp + ".txt";

        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(receipt.toString());
        } catch (IOException e) {
            return null;
        }
        return filename;
    }
}
