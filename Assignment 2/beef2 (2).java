// Using Getter & Constructor
public class beef1 {
    private final String type;

    public beef1(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void display() {
        System.out.println("Favorite food: " + type);
    }
}