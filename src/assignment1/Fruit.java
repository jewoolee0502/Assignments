package assignment1;

/**
 * @author: Jewoo Lee
 * @Student ID: 260910789
 */
public class Fruit extends MarketProduct {

    //field
    private double weight; //weight in kg
    private int price; //price per kg in cents

    //constructor
    public Fruit(String productName, double weight, int price) {
        super(productName);
        this.weight = weight;
        this.price = price;
    }

    public int getCost() {
        int cost = (int) (this.weight * this.price); //weight is double, so have to cast it to be an int
        return cost; //cost = weight in kg * price per kg
    }

    public boolean equals(Object obj) {
        if(obj instanceof Fruit) { //checking if they have the same type
            Fruit fruit = (Fruit) obj;

            if((this.getName().equalsIgnoreCase(fruit.getName()))
                    && (this.weight == fruit.weight) //not checking if the price per kg is the same because of seasonal fruit
                    && (this.getCost() == fruit.getCost())) { //checking if they have the same name, weight, and cost
                return true;
            }
        }
        return false; //return false if they are not equal in one of the conditions
    }

}
