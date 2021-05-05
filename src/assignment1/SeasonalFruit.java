package assignment1;

/**
 * @author: Jewoo Lee
 * @Student ID: 260910789
 */
public class SeasonalFruit extends Fruit {

    //constructor
    public SeasonalFruit(String productName, double weight, int price) {
        //uses the inputs to create a fruit
        super(productName, weight, price);
    }

    public int getCost() {
        double discount = 0.15; //15% discount on seasonal fruit
        double cost = (super.getCost() * (1 - discount)); //cost = cost of the fruits * 0.85
        return (int) cost;
    }
}
