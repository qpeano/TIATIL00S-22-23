//* Class is used to simulate a player in the game "31".
 *
 * Author: Shamiur Rahman Ramim
 */

 import javax.swing.ImageIcon;

 public class Player {

    /* Fields */

    private String[] cardsOnHand; // the cards that player is currently has
    private boolean isComputer; // tells if a player instance is computer or not
    private double sumOfCardValues; // the sum of cards on hand

    /* Methods - Constructor */

    public Player(Board board, boolean isComputer) {

        this.isComputer = isComputer;
        this.cardsOnHand = board.drawStartingCards();
        this.sumOfCardValues = this.calculateCardSum(board, this.cardsOnHand);
    }

     /* Methods - Internal */

     // Method counts up the value of each card on hand
     // if all cards have same rank, then 30.5 is to be returned
     private double calculateCardSum(Board board, String[] cards) {

        double sum = 0;
        double[] cardValues = new double[cards.length];
        for (int i = 0; i < cards.length; i++) {

            cardValues[i] = board.getValueOf(cards[i]);
            sum += cardValues[i];
        }

        if (this.allCardsHaveSameRank(cards)) {

            return 30.5;
        }
        else {

            return sum;
        }
    }

    // Method checks if all ranks ("cards number") of an array (hand) are the same
    private boolean allCardsHaveSameRank(String[] cardValues) {

        String checker = cardValues[0].substring(1); // removes the first character of cardValue, which is it's suit
        for (int i = 1; i < cardValues.length; i++) {

            if (!checker.equals(cardValues[i].substring(1))) {

                return false;
            }
        }

        return true;
    }

     // Method is used to determine if computer should exchange a card with the top card of discard pile or not
     private boolean isDrawingFromDiscard(Board board, int indexOfCard) {

        // if stock pile is empty, computer should always choose the discard pile
        if (board.isStockPileEmpty() && !board.isDiscardPileEmpty()) {

            return true;
        }

        String topCardOfDiscard = board.peekTopOfDiscard(); // takes a look of the top card of discard
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

            double potentialSumOfCardvalues = this.calculateCardSum(board, potentialCardsOnHand);

            if (potentialSumOfCardvalues > 26 && potentialSumOfCardvalues < 32) {

                return true;
            }
            else {

                return false;
            }
        }

        return false;
    }

    private int getIndexOfLeastCard(Board board) {

        int leastIndex = 0;
        for (int i = 0; i < this.cardsOnHand.length; i++) {

            double valueOfCard = board.getValueOf(this.cardsOnHand[i]);
            double valueOfLeastCard = board.getValueOf(this.cardsOnHand[leastIndex]);
            if (valueOfCard < valueOfLeastCard) {

                leastIndex = i;
            }
        }

        return leastIndex;
    }

    // Method is used by computer to get the index of the card with the highest card value that is on hand
    private int getIndexOfMostCard(Board board) {

        int mostIndex = 0;
        for (int i = 0; i < this.cardsOnHand.length; i++) {

            double valueOfCard = board.getValueOf(this.cardsOnHand[i]);
            double valueOfMostCard = board.getValueOf(this.cardsOnHand[mostIndex]);
            if (valueOfCard > valueOfMostCard) {

                mostIndex = i;
            }
        }

        return mostIndex;
    }

    /* Methods - UI */

     // Method is used when player wants to exchange a card with a new card from stock pile
     public void drawCardFromStock(Board board, int indexOfCard) {

        String newCard = board.drawFromStock(this.cardsOnHand[indexOfCard]);
        this.cardsOnHand[indexOfCard] = newCard;

        // calculates new sum
        this.sumOfCardValues = this.calculateCardSum(board, this.cardsOnHand);
    }

    // Method is used when player wants to exchange a card with a new card from discard pile
    public void drawCardFromDiscard(Board board, int indexOfCard) {

        String newCard = board.drawFromDiscard(this.cardsOnHand[indexOfCard]);
        // System.out.println(this.cardsOnHand[indexOfCard]);
        this.cardsOnHand[indexOfCard] = newCard;
        // System.out.println(newCard);
        // calculates new sum
        this.sumOfCardValues = this.calculateCardSum(board, this.cardsOnHand);
    }

    // Method is used to get the icon of a card with specified index
    public ImageIcon getImageIconOf(Board board, int indexOfCard) {

        ImageIcon cardIcon = board.getIconOf(this.cardsOnHand[indexOfCard]);
        return cardIcon;
    }

    // Method is only used by computer
    public ImageIcon makeMove(Board board) {

        ImageIcon imageOfDiscardedCard = null;

        if (!this.isComputer) { // returns null and does nothing if instance isn't a computer

            return null;
        }

        if (this.sumOfCardValues <= 26) { // if sum of cards is less than or equal to 26, computer discards least card

            int indexOfLeastCard = this.getIndexOfLeastCard(board);
            imageOfDiscardedCard = board.getIconOf(this.cardsOnHand[indexOfLeastCard]);

            if (this.isDrawingFromDiscard(board, indexOfLeastCard)) { // if adding top card from discard makes card sum in (26, 32)

                this.drawCardFromDiscard(board, indexOfLeastCard);
            }
            else { // if adding top card doesn't yield a desirable sum OR if discard pile is empty

                this.drawCardFromStock(board, indexOfLeastCard);
            }
        }
        else if (this.sumOfCardValues >= 32) { // if sum of cards is greater than or equal to 32, computer discards most card

            int indexOfMostCard = this.getIndexOfMostCard(board);
            imageOfDiscardedCard = board.getIconOf(this.cardsOnHand[indexOfMostCard]);

            if (this.isDrawingFromDiscard(board, indexOfMostCard)) { // if adding top card from discard makes card sum in (26, 32)

                this.drawCardFromDiscard(board, indexOfMostCard);
            }
            else { // if adding top card doesn't yield a desirable sum OR if discard pile is empty

                this.drawCardFromStock(board, indexOfMostCard);
            }
        }

        // calculates new sum
        this.sumOfCardValues = this.calculateCardSum(board, this.cardsOnHand);

        return imageOfDiscardedCard;
    }

    // Method should ONLY  be used by computer, tells computer if it should make a move or not
    public boolean isDrawingCard() {

        return !(this.sumOfCardValues > 26 && this.sumOfCardValues < 32);
    }

    // Method is used to get card sum
    public double getSumOfCards() {

        return this.sumOfCardValues;
    }
 }
