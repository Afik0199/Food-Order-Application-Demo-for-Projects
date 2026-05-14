public class FoodItem {
    private String name;
    private int price; // price in Tk

    public FoodItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        // What will be shown in the JList
        return name + " - Tk " + price;
    }
}
