import java.util.List;
import java.util.ArrayList;

class CustomerManagement {
    private List<Customer> customers;

    public CustomerManagement() {
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}