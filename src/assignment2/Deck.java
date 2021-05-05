package assignment2;

import java.util.Random;

public class Deck {
 public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
 public static Random gen = new Random();

 public int numOfCards; // contains the total number of cards in the deck
 public Card head; // contains a pointer to the card on the top of the deck

 /* 
  * TODO: Initializes a Deck object using the inputs provided
  */

 public Deck(int numOfCardsPerSuit, int numOfSuits) {
  /**** ADD CODE HERE ****/
//  Joker redJoker = new Joker("red");
//  Joker blackJoker = new Joker("black");
//  this.numOfCards = (numOfCardsPerSuit * numOfSuits) + 2;

  if((numOfSuits < 1 || numOfSuits > suitsInOrder.length) || (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13)) {
     throw new IllegalArgumentException("The inputs are illegal as they are either not a possible number of cards per suits or a number of suits.");
  }

  for(int i = 0; i < numOfSuits; i++) {
     for(int j = 0; j < numOfCardsPerSuit; j++) {
        Card newCard = new PlayingCard(suitsInOrder[i], j + 1);
        addCard(newCard);
     }
  }
  addCard(new Joker("red"));
  addCard(new Joker("black"));
 }

 /* 
  * TODO: Implements a copy constructor for Deck using Card.getCopy().
  * This method runs in O(n), where n is the number of cards in d.
  */
 public Deck(Deck d) {
  /**** ADD CODE HERE ****/
  Card copyCard = d.head;
  for(int i = 0; i < d.numOfCards; i++) {
     addCard(copyCard.getCopy());
     copyCard = copyCard.next;
  }
 }

 /*
  * For testing purposes we need a default constructor.
  */
 public Deck() {}

 /* 
  * TODO: Adds the specified card at the bottom of the deck. This 
  * method runs in $O(1)$. 
  */
 public void addCard(Card c) {
  /**** ADD CODE HERE ****/
  if(head == null) {
    head = c;
    head.prev = c;
    head.next = c;
    this.numOfCards = 1;
  }
  else {
    Card lastCard = head.prev;
    c.next = head;
    head.prev = c;
    lastCard.next = c;
    c.prev = lastCard;
    numOfCards += 1;
  }
 }

 /*
  * TODO: Shuffles the deck using the algorithm described in the pdf. 
  * This method runs in O(n) and uses O(n) space, where n is the total 
  * number of cards in the deck.
  */
 public void shuffle() {
  /**** ADD CODE HERE ****/
  if(head != null) {
     Card[] card = new Card[numOfCards];
     card[0] = head;
     for(int i = 0; i < numOfCards - 1; i++) {
        card[i + 1] = card[i].next;
     }
     for(int i = numOfCards - 1; i >= 1; i--) {
        int j = gen.nextInt(i + 1);
        Card temp = card[i];
        card[i] = card[j];
        card[j] = temp;
     }
     head = card[0];
     for(int i = 0; i < numOfCards; i++) {
        card[i].next = card[(i + 1) % numOfCards];
        card[i].next.prev = card[i];
     }
  }
 }

 /*
  * TODO: Returns a reference to the joker with the specified color in 
  * the deck. This method runs in O(n), where n is the total number of 
  * cards in the deck. 
  */
 public Joker locateJoker(String color) {
  /**** ADD CODE HERE ****/
  Card card = head;
  String jokerName = (color.charAt(0) + "J").toUpperCase();
  for(int i = 0; i < numOfCards; i++) {
     if(card.toString().equals(jokerName)) {
        return (Joker) card;
     }
     card = card.next;
  }
  return null;
 }

 /*
  * TODO: Moved the specified Card, p positions down the deck. You can 
  * assume that the input Card does belong to the deck (hence the deck is
  * not empty). This method runs in O(p).
  */
 public void moveCard(Card c, int p) {
  /**** ADD CODE HERE ****/
  if(p != 0) {
      Card card = c;
      c.prev.next = c.next;
      c.next.prev = c.prev;
      for(int i = 0; i < p; i++) {
          card = card.next;
      }
      c.prev = card;
      card.next.prev = c;
      c.next = card.next;
      card.next = c;
  }
 }

 /*
  * TODO: Performs a triple cut on the deck using the two input cards. You 
  * can assume that the input cards belong to the deck and the first one is 
  * nearest to the top of the deck. This method runs in O(1)
  */
 public void tripleCut(Card firstCard, Card secondCard) {
  /**** ADD CODE HERE ****/
  Card temp;
  if(secondCard != head.prev) {
      temp = secondCard.next;
  }
  else {
      temp = firstCard;
  }
  if(firstCard != head) {
      Card firstFront = head;
      Card firstLast = firstCard.prev;
      firstFront.prev.next = firstCard;
      firstCard.prev = firstFront.prev;
      secondCard.next = firstFront;
      firstFront.prev = secondCard;
      firstLast.next = temp;
      temp.prev = firstLast;
  }
  head = temp;
 }

 /*
  * TODO: Performs a count cut on the deck. Note that if the value of the 
  * bottom card is equal to a multiple of the number of cards in the deck, 
  * then the method should not do anything. This method runs in O(n).
  */
 public void countCut() {
  /**** ADD CODE HERE ****/
  Card card = head;
  int value = head.prev.getValue() % numOfCards;
  if(value == 0 || value == numOfCards - 1) {
      return;
  }

  for(int i = 0; i < value - 1; i++) {
      card = card.next;
  }
  Card temp1 = card.next;
  Card temp2 = head.prev.prev;
  card.next = head.prev;
  head.prev.prev = card;
  temp1.prev = head.prev;
  head.prev.next = temp1;
  temp2.next = head;
  head.prev = temp2;
  head = temp1;
 }

 /*
  * TODO: Returns the card that can be found by looking at the value of the 
  * card on the top of the deck, and counting down that many cards. If the 
  * card found is a Joker, then the method returns null, otherwise it returns
  * the Card found. This method runs in O(n).
  */
 public Card lookUpCard() {
  /**** ADD CODE HERE ****/
  int value = head.getValue();
  Card card = head;
  for(int i = 0; i < value; i++) {
      card = card.next;
  }
  if(!(card.toString().equals("RJ")) && !(card.toString().equals("BJ"))) {
      return card;
  }
  return null;
 }

 /*
  * TODO: Uses the Solitaire algorithm to generate one value for the keystream 
  * using this deck. This method runs in O(n).
  */
 public int generateNextKeystreamValue() {
  /**** ADD CODE HERE ****/
  Joker RJ = locateJoker("red");
  Joker BJ = locateJoker("black");
  while(true) {
      moveCard(RJ, 1);
      moveCard(BJ, 2);
      Card card = head;
      while(true) {
          if(card == RJ) {
              tripleCut(RJ, BJ);
              break;
          }
          else if(card == BJ) {
              tripleCut(BJ, RJ);
              break;
          }
          card = card.next;
      }
      countCut();
      Card result = lookUpCard();
      if(result != null) {
          return result.getValue();
      }
  }
  //return 0;
 }


 public abstract class Card { 
  public Card next;
  public Card prev;

  public abstract Card getCopy();
  public abstract int getValue();

 }

 public class PlayingCard extends Card {
  public String suit;
  public int rank;

  public PlayingCard(String s, int r) {
   this.suit = s.toLowerCase();
   this.rank = r;
  }

  public String toString() {
   String info = "";
   if (this.rank == 1) {
    //info += "Ace";
    info += "A";
   } else if (this.rank > 10) {
    String[] cards = {"Jack", "Queen", "King"};
    //info += cards[this.rank - 11];
    info += cards[this.rank - 11].charAt(0);
   } else {
    info += this.rank;
   }
   //info += " of " + this.suit;
   info = (info + this.suit.charAt(0)).toUpperCase();
   return info;
  }

  public PlayingCard getCopy() {
   return new PlayingCard(this.suit, this.rank);   
  }

  public int getValue() {
   int i;
   for (i = 0; i < suitsInOrder.length; i++) {
    if (this.suit.equals(suitsInOrder[i]))
     break;
   }

   return this.rank + 13*i;
  }

 }

 public class Joker extends Card{
  public String redOrBlack;

  public Joker(String c) {
   if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black")) 
    throw new IllegalArgumentException("Jokers can only be red or black"); 

   this.redOrBlack = c.toLowerCase();
  }

  public String toString() {
   //return this.redOrBlack + " Joker";
   return (this.redOrBlack.charAt(0) + "J").toUpperCase();
  }

  public Joker getCopy() {
   return new Joker(this.redOrBlack);
  }

  public int getValue() {
   return numOfCards - 1;
  }

  public String getColor() {
   return this.redOrBlack;
  }
 }

}
