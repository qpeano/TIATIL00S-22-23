/* Class is used to simulate a player in the game "31".
 *
 * Author: @qpeano
 */

import javax.swing.ImageIcon;

public class Player {

    /* Fields */

    private static Board board; // the board with the decks
    public String[] cardsOnHand; // the cards on hand
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

    // counts up the value of each card on hand
    private double calculateCardSum(String[] cards) {

        double sum = 0;
        for (int i = 0; i < cards.length; i++) {

            sum += Player.board.getValueOf(cards[i]);
        }

        return sum;
    }

    // Method is used to determine if computer should exchange a card with the top card of discard pile or not
    private boolean isDrawingFromDiscard(int indexOfCard) {

        String topCardOfDiscard = Player.board.peekTopOfDiscard(); // takes a look of the top card of discard
        if (topCardOfDiscard != null) { // checks if discard is empty

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
        this.cardsOnHand[indexOfCard] = newCard;

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

        int indexOfDiscardedCard = 0;
        if (!this.isComputer) { // returns null and does nothing if instance isn't a computer

            return null;
        }

        if (this.sumOfAllCardValues <= 26) { // if sum of cards is less than or equal to 26, computer discards least card

            int indexOfLeastCard = this.getIndexOfLeastCard();
            if (this.isDrawingFromDiscard(indexOfLeastCard)) { // if adding top card from discard makes card sum in (26, 32)

                this.drawCardFromDiscard(indexOfLeastCard);
            }
            else { // if adding top card doesn't yeald a desirable sum OR if discard pile is empty

                this.drawCardFromStock(indexOfLeastCard);
            }

            indexOfDiscardedCard = indexOfLeastCard;
        }
        else if (this.sumOfAllCardValues >= 32) { // if sum of cards is greater than or equal to 32, computer discards most card

            int indexOfMostCard = this.getIndexOfMostCard();
            if (this.isDrawingFromDiscard(indexOfMostCard)) { // if adding top card from discard makes card sum in (26, 32)

                this.drawCardFromDiscard(indexOfMostCard);
            }
            else { // if adding top card doesn't yeald a desirable sum OR if discard pile is empty

                this.drawCardFromStock(indexOfMostCard);
            }

            indexOfDiscardedCard = indexOfMostCard;
        }

        // calculates new sum
        this.sumOfAllCardValues = this.calculateCardSum(this.cardsOnHand);

        ImageIcon imageOfDiscardedCard = Player.board.getIconOf(this.cardsOnHand[indexOfDiscardedCard]);
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
}
