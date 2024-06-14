
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Sales {
    private Inventory inventory;

    public Sales(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public double calculateRevenue() {
        double revenue = 0;
        for (MyBook book : inventory.getBookList()) {
            revenue += (book.priceProperty().get() * book.quantityProperty().get());
        }
        return revenue;
    }
}