public class MainFood {
    public static void main(String[] args) {

        System.out.println("1 --> Using Setter and Getter");
        beef type = new beef();
        type.setType("Curry");
        System.out.println("Beef type is: " + type.getType());

        System.out.println("\n2 --> Using Constructor and Getter");
        beef1 type1 = new beef1("Vuna");
        System.out.println("Beef type is: " + type1.getType());
        
        System.out.println("\n3 --> Using Only Display Method");
        beef2 type2 = new beef2();
        type2.display();

        System.out.println("\n4 --> Using Only Constructor");
        beef3 food3 = new beef3();
        System.out.println(food3);
    }
}

