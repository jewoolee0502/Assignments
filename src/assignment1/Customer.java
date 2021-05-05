package assignment1;

/**
 * @author: Jewoo Lee
 * @Student ID: 260910789
 */
public class Customer {

    //field
    private String name; //name of the customer
    private int balance; //balance of the customer in cents
    private Basket basket; //array of products that the customer would like to buy

    //constructor
    public Customer(String name, int initBalance) {
        //use the inputs and create an empty Basket to initialize the corresponding fields
        this.basket = new Basket();
        this.name = name;
        this.balance = initBalance;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance;
    }

    public Basket getBasket() {
        return this.basket;
    }

    public int addFunds(int funds) throws IllegalArgumentException {
        if(funds >= 0) {
            balance = this.balance + funds; //the new balance is initial balance + funds added by the customer
        }
        else { //when fund is negative
            throw new IllegalArgumentException("You can only add funds that are positive.");
        }
        return this.getBalance();
    }

    public void addToBasket(MarketProduct marketProduct) {
        this.getBasket().add(marketProduct);//adding marketProduct to the basket
    }

    public boolean removeFromBasket(MarketProduct marketProduct) {
        return this.getBasket().remove(marketProduct); //removing marketProduct
    }

    public String checkOut() throws IllegalStateException {
        String checkOut; //initializing

        if(getBalance() >= basket.getTotalCost()) { //when the customer has greater or equal balance than the total cost
            balance = this.getBalance() - basket.getTotalCost(); //new balance is original balance - total cost
        }
        else { //when the customer's balance is less than the total cost
            throw new IllegalStateException("You do not have enough balance.");
        }
        checkOut = getBasket().toString(); //receipt
        this.getBasket().clear(); //clearing the basket after the checkout
        return checkOut;
    }

}
