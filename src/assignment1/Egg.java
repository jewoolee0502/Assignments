package assignment1;

/**
 * @author: Jewoo Lee
 * @Student ID: 260910789
 */
public class Egg extends MarketProduct {

    //field
    private int numOfEggs; //number of eggs
    private int priceDozen; //price per dozen of eggs in cents

    //constructor
    public Egg(String productName, int requiredNum, int productPrice) {
        super(productName);
        this.numOfEggs = requiredNum;
        this.priceDozen = productPrice;
    }

    public int getCost() {
        double cost = (double) (this.priceDozen * this.numOfEggs) / 12;
        //multiplying the number first to avoid getting zero from the division by 12
        //also made it double first to make sure it doesn't give me zero even when price * numOfEgg is less than 12
        return (int) cost;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Egg) { //checking if they have the same type
            Egg egg = (Egg) obj;

            if((this.getName().equalsIgnoreCase(egg.getName()))
                    && (this.numOfEggs == egg.numOfEggs)
                    && (this.getCost() == egg.getCost())) { //checking if they have the same name, number of eggs, and cost
                return true;
            }
        }
        return false; //return false if they are not equal in one of the conditions
    }

}
