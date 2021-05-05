package assignment1;

/**
 * @author: Jewoo Lee
 * @Student ID: 260910789
 */
public class Jam extends MarketProduct {

    //field
    private int numOfJars; //number of Jars
    private int priceJar; //price per jar in cents

    //constructor
    public Jam(String productName, int numOfJars, int priceJar) {
        super(productName);
        this.numOfJars = numOfJars;
        this.priceJar = priceJar;
    }

    public int getCost() {
        int cost = (this.numOfJars * this.priceJar); //cost = price per number of jar * number of jars
        return cost;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Jam) { //checking if they have the same type
            Jam jam = (Jam) obj;

            if((this.getName().equalsIgnoreCase(jam.getName()))
                    && (this.numOfJars == jam.numOfJars)
                    && (this.getCost() == jam.getCost())) { //checking if they have the same name, number of jars, and cost
                return true;
            }
        }
        return false; //return false if they are not equal in one of the conditions
    }

}
