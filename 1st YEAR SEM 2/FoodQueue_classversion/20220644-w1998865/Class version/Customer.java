import java.io.Serializable;
public class Customer implements Serializable {
    private final String firstName;
    private final String lastName;
    private final int burgersRequired;

    public Customer(String firstName, String lastName, int burgersRequired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.burgersRequired = burgersRequired;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getBurgersRequired() {
        return burgersRequired;
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + ", Burgers Required: " + burgersRequired;
    }
}