package assignment1;

/**
 * @author: Jewoo Lee
 * @Student ID: 260910789
 */
public class Basket {

    //field
    private MarketProduct[] marketProducts; //an array list of market products

    //constructor
    public Basket() {
        this.marketProducts = new MarketProduct[] {}; //initialized the array with an empty array
    }

    public MarketProduct[] getProducts() {
        MarketProduct[] marketProductsCopy = this.marketProducts.clone(); //shallow copy of the array
        return marketProductsCopy;
    }

    public void add(MarketProduct marketProduct) {
        MarketProduct[] newMarketProd = new MarketProduct[this.marketProducts.length + 1]; //create a new array with one length longer

        for(int i = 0; i < marketProducts.length; i++) {
            newMarketProd[i] = this.marketProducts[i]; //copying all the elements in the new array
        }
        newMarketProd[this.marketProducts.length] = marketProduct; //setting the last element to be marketProduct
        this.marketProducts = newMarketProd;
    }

    public boolean remove(MarketProduct marketProduct) {
        int k = marketProducts.length - 1; //setting k to be the length of the new array after an element is removed
        int i = 0; //initializing an integer i outside so I can also use it in the if statement and while loop
        int index = 0; //initializing int index
        boolean removed = false; //initializing remove to be false

        if(marketProducts.length > 0) { //making sure that the array is not empty
            while(i < marketProducts.length) { //goes through each element in the array
                if(marketProducts[i].equals(marketProduct)) { //checks which element is wanting to be deleted
                    index = i; //storing which elements in which index wants to be deleted
                    removed = true;
                    break;
                }
                i++;
            }
            if(removed == true) {
                MarketProduct[] newMarketProducts = new MarketProduct[k]; //creating new array with one less length
                for(int j = 0; j < index; j++) { //copying all the element until index, which is the element that will be removed
                    newMarketProducts[j] = this.marketProducts[j];
                }
                for(int n = index; n < k; n++) { //starts copying all the element again after the element that will be removed
                    newMarketProducts[n] = this.marketProducts[n + 1];
                }
                this.marketProducts = newMarketProducts; //setting the old array = new array
            }
            return removed;
        }
        return false; //removed is always false here
    }

    public void clear() {
        this.marketProducts = new MarketProduct[] {}; //clearing the array
    }

    public int getNumOfProducts() {
        return this.marketProducts.length; //length of the array = number of products(elements)
    }

    public int getSubTotal() {
        //returns the cost of all the products in the basket
        int subTotal = 0; //initializing subTotal

        for(int i = 0; i < this.marketProducts.length; i++) {
            subTotal = subTotal + this.marketProducts[i].getCost(); //subTotal = adding all the individual product's cost
        }
        return subTotal; //cost before taxes added
    }

    public int getTotalTax() {
        double tax = 0.15; //Quebec tax rate = 15%
        double totalTax = 0.0; //cost of the total tax
        int jamSubTotCost = 0; //sum of all the cost for jam as they are the only product where tax applies
        //return tax amount to be paid based on the product in this basket

        for(int i = 0; i < this.marketProducts.length; i++) {
            if(this.marketProducts[i] instanceof Jam) { //goes through all the element in the array and checks which element has the type Jam
                jamSubTotCost += this.marketProducts[i].getCost(); //add costs of all the jam
            }
            totalTax = jamSubTotCost * tax; //since only jam is taxed
        }
        return (int) totalTax;
    }

    public int getTotalCost() {
        //returns the total cost of the products in the basket
        int totalCost = this.getSubTotal() + this.getTotalTax(); //total cost in cents
        return totalCost;
    }

    private String centToDollar(int money) { //converting cents to dollar AND from int to String
        int dollar = money / 100; //represents the dollar part of the money
        int cents = money % 100; //represents the decimals(cents) of the money

        if(money <= 0) { //when the money is zero or negative
            return "-";
        }
        else if(cents < 10) { //making sure that the decimals are in the right place
            return (dollar + ".0" + cents); //concatenating strings
        }
        else { //cents > 0
            return (dollar + "." + cents); //concatenating strings, and just '.' in between dollar and cents
        }
    }

    public String toString() {
        String product = ""; //initializing
        String receipt;

        for(int i = 0; i < this.marketProducts.length; i++) {
            product += this.marketProducts[i].getName() + "\t"
                    + centToDollar(this.marketProducts[i].getCost()) + "\n";
            //product is a list of all the names and costs of each products added to the basket
        }
        receipt = product + "\n" + "Subtotal" + "\t" + centToDollar(getSubTotal()) + "\n"
                + "Total Tax" + "\t" + centToDollar(getTotalTax()) + "\n\n"
                + "Total Cost" + "\t" + centToDollar(getTotalCost());
        //receipt contains product and list of Subtotal, Total tax, and Total cost
        return receipt;
    }

}
