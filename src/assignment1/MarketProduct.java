package assignment1;

/**
 * @author: Jewoo Lee
 * @Student ID: 260910789
 */
abstract public class MarketProduct {

    //field
    private String name;

    //constructor
    public MarketProduct(String productName) {
        this.name = productName;
    }

    public final String getName() {
        return this.name;
    }

    public abstract int getCost();

    public abstract boolean equals(Object obj);

}
