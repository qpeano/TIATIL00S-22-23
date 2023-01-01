/* Class is used to simulate a player in the game "31".
 *
 * Author: @qpeano
 */

 import javax.swing.ImageIcon;

 public class Player {

     /* Fields */

     private static Board board; // the board with the decks
     private String[] cardsOnHand; // the cards on hand //change!!!
     private boolean isComputer; // tells if a player is computer or not;
     private double sumOfAllCardValues; // self-explanatory

     /* Constructor */

     public Player(boolean isComputer) {

         Player.board = new Board();
         this.isComputer = isComputer;
         this.cardsOnHand = Player.board.drawStartingCards();
         this.sumOfAllCardValues = this.calculateCardSum(this.cardsOnHand);
     }

     /* Internal */

     // Method counts up the value of each card on hand
     // if all cards have same rank, then 30.5 is to be returned
     private double calculateCardSum(String[] cards) {
    	 
    	 double sum = 0;
    	 double[] cardValues = new double[cards.length];
    	 for (int i = 0; i < cards.length; i++) {
    		 
    		 cardValues[i] = Player.board.getValueOf(cards[i]);
    		 sum += cardValues[i];
    	 }
    	 
    	 if (this.allCardsHaveSameRank(cardValues)) {
    		 
    		 return 30.5;
    	 }
    	 else {
    		 
    		 return sum;
    	 }
     }
     
     // Method checks if all values (cards) of an array (hand) are the same 
     private boolean allCardsHaveSameRank(double[] cardValues) {
    	 
    	 double checker = cardValues[0];
    	 for (int i = 1; i < cardValues.length; i++) {
    		 
    		 if (!(checker == cardValues[i])) {
    			 
    			 return false;
    		 }
    	 }
    	 
    	 return true;
     }

     // Method is used to determine if computer should exchange a card with the top card of discard pile or not
     private boolean isDrawingFromDiscard(int indexOfCard) {

         // if stock pile is empty, computer should always choose the discard pile
         if (Player.board.isStockPileEmpty() && !Player.board.isDiscardPileEmpty()) {

             return true;
         }

         String topCardOfDiscard = Player.board.peekTopOfDiscard(); // takes a look of the top card of discard
         // System.out.println(topCardOfDiscard);

         if (topCardOfDiscard != null) {

             // code below checks if exchanging a certain card with the top card of discard will result in
             // the computers card sum being in the integer range of (26, 32), which will let it knock next round.
             String[] potentialCardsOnHand = new String[3];
             for (int i = 0; i < potentialCardsOnHand.length; i++) {

                 if (i == indexOfCard) {

                     potentialCardsOnHand[i] = topCardOfDiscard;
                 }
                 else {

                     potentialCardsOnHand[i] = this.cardsOnHand[i];
                 }
             }

             double potentialSumOfCardvalues = this.calculateCardSum(potentialCardsOnHand);

             if (potentialSumOfCardvalues > 26 && potentialSumOfCardvalues < 32) {

                 return true;
             }
             else {

                 return false;
             }
         }

         return false;
     }

     // Method is used by computer to get the index of the card with the lowest card value that is on hand
     private int getIndexOfLeastCard() {

         int leastIndex = 0;
         for (int i = 0; i < this.cardsOnHand.length; i++) {

             double valueOfCard = Player.board.getValueOf(this.cardsOnHand[i]);
             double valueOfLeastCard = Player.board.getValueOf(this.cardsOnHand[leastIndex]);
             if (valueOfCard < valueOfLeastCard) {

                 leastIndex = i;
             }
         }

         return leastIndex;
     }

     // Method is used by computer to get the index of the card with the highest card value that is on hand
     private int getIndexOfMostCard() {

         int mostIndex = 0;
         for (int i = 0; i < this.cardsOnHand.length; i++) {

             double valueOfCard = Player.board.getValueOf(this.cardsOnHand[i]);
             double valueOfMostCard = Player.board.getValueOf(this.cardsOnHand[mostIndex]);
             if (valueOfCard > valueOfMostCard) {

                 mostIndex = i;
             }
         }

         return mostIndex;
     }

     /* USER INTERFACE */

     // Method is used when player wants to exchange a card with a new card from stock pile
     public void drawCardFromStock(int indexOfCard) {

         String newCard = Player.board.drawFromStock(this.cardsOnHand[indexOfCard]);
         this.cardsOnHand[indexOfCard] = newCard;

         // calculates new sum
         this.sumOfAllCardValues = this.calculateCardSum(this.cardsOnHand);
     }

     // Method is used when player wants to exchange a card with a new card from discard pile
     public void drawCardFromDiscard(int indexOfCard) {

         String newCard = Player.board.drawFromDiscard(this.cardsOnHand[indexOfCard]);
         // System.out.println(this.cardsOnHand[indexOfCard]);
         this.cardsOnHand[indexOfCard] = newCard;
         // System.out.println(newCard);
         // calculates new sum
         this.sumOfAllCardValues = this.calculateCardSum(this.cardsOnHand);
     }

     // Method is used to get the icon of a card with specified index
     public ImageIcon getImageIconOf(int indexOfCard) {

         ImageIcon cardIcon = Player.board.getIconOf(this.cardsOnHand[indexOfCard]);
         return cardIcon;
     }

     // Method is only used by computer
     public ImageIcon makeMove() {

         ImageIcon imageOfDiscardedCard = null;

         if (!this.isComputer) { // returns null and does nothing if instance isn't a computer

             return null;
         }

         if (this.sumOfAllCardValues <= 26) { // if sum of cards is less than or equal to 26, computer discards least card

             int indexOfLeastCard = this.getIndexOfLeastCard();
             imageOfDiscardedCard = Player.board.getIconOf(this.cardsOnHand[indexOfLeastCard]);

             if (this.isDrawingFromDiscard(indexOfLeastCard)) { // if adding top card from discard makes card sum in (26, 32)

                 this.drawCardFromDiscard(indexOfLeastCard);
             }
             else { // if adding top card doesn't yield a desirable sum OR if discard pile is empty

                 this.drawCardFromStock(indexOfLeastCard);
             }
         }
         else if (this.sumOfAllCardValues >= 32) { // if sum of cards is greater than or equal to 32, computer discards most card

             int indexOfMostCard = this.getIndexOfMostCard();
             imageOfDiscardedCard = Player.board.getIconOf(this.cardsOnHand[indexOfMostCard]);

             if (this.isDrawingFromDiscard(indexOfMostCard)) { // if adding top card from discard makes card sum in (26, 32)

                 this.drawCardFromDiscard(indexOfMostCard);
             }
             else { // if adding top card doesn't yield a desirable sum OR if discard pile is empty

                 this.drawCardFromStock(indexOfMostCard);
             }
         }

         // calculates new sum
         this.sumOfAllCardValues = this.calculateCardSum(this.cardsOnHand);

         return imageOfDiscardedCard;
     }

     // Method should ONLY  be used by computer, tells computer if it should make a move or not
     public boolean isDrawingCard() {

         return !(this.sumOfAllCardValues > 26 && this.sumOfAllCardValues < 32);
     }

     // Method is used to get card sum
     public double getSumOfCards() {

         return this.sumOfAllCardValues;
     }

     // Method is used to check if stock pile is empty
     public boolean isStockPileEmpty() {

         return Player.board.isStockPileEmpty();
     }
 }
