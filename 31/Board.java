/* Class is used to simulate two a piles of cards, a stock pile and a discard pile.
 * The formatting of the cards are explained below.
 *
 * A card is a string of digits 'sdd' where the first digit, 's', is the suit of the card.
 *
 * 0 : clubs
 * 1 : diamonds
 * 2 : hearts
 * 3 : spades
 *
 * The last two digits, 'dd' denote the card. Where the cards with numbers are the numbers on the cards.
 * The other "special" cards have been given other values.
 *
 * 1 : ace
 * 11 : jester
 * 12 : queen
 * 13 : king
 *
 * This class is used with other classes to simulate a variation of the game "31".
 * 
 * CLARIFICATION: 
 * 
 * (1) declaring all fields (= variables declared outside methods) 
 *     as private prevents them to be changed be changed by anything outside of this class. 
 * (2) this.[variable name] is used to refer to a field of a class.
 *     You are able to refer to a field with only its name, but I like to use
 *     this.[variable name].
 * (3) declaring methods as privates prevents them from being called outside this class
 * (4) this.[method name] is in the same as doing this.[variable name], I prefer
 *     to write this.[method name] instead of [method name]. 
 *
 * ----
 * Author: Shamiur Rahman Ramim
 */

import java.util.Random;
import javax.swing.ImageIcon;
import java.util.Arrays;

public class Board {

    /* Fields */

    private ImageIcon[] cardImages; // the images of all cards
    private String[] stockPile; // the stock pile of cards
    private String[] discardPile; // discard pile of cards

    /* Methods - Constructor */

    public Board() {

        this.getAllCardImages(); // (2)
        this.generateStockPile();
        this.shufflePile(this.stockPile);
        this.discardPile = new String[0];
    }

    /* Internal */

    // Method fills this.cardImages with icons of all the playing-cards in order (3)
    private void getAllCardImages() {

        this.cardImages = new ImageIcon[52];
        this.addCardImages();
    }
    
    // Method adds all card images
    private void addCardImages() {
    	
    	String[] suits = {"clubs", "diamonds", "hearts", "spades"};
    	String[] ranks = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
    	for (int suitIndex = 0; suitIndex < suits.length; suitIndex++) {
    		
    		for (int rankIndex = 0; rankIndex < ranks.length; rankIndex++) {
    			
    			String card = ranks[rankIndex] + "_of_" + suits[suitIndex] + ".png";
    			int index = suitIndex * 13 + rankIndex;
    			this.cardImages[index] = new ImageIcon(card);
    		}
    	}
    }

    // Method creates the string representations of all 52 cards
    private void generateStockPile() {

        this.stockPile = new String[52];
        // loop goes through and fills the empty stock pile with string representations of cards
        for (int suit = 0; suit < 4; suit++) {

        	for (int cardCount = 0; cardCount < 13; cardCount++) {

        		int cardIndex = suit * 13 + cardCount;
        		this.stockPile[cardIndex] = suit + "";
        		if (cardCount < 9) {

        			this.stockPile[cardIndex] += "0";
        		}

        		this.stockPile[cardIndex] += (cardCount + 1);
        	}
        }
    }

    // shuffles a card pile
    private void shufflePile(String[] pile) {

        String temporaryCardHolder = null;
        Random random = new Random();

        // goes through pile, and rearranges some cards by changing the index
        // of a card to another random index.
		for (int i = 0; i < pile.length; i++) {

			int randomIndexToSwap = random.nextInt(pile.length);
			temporaryCardHolder = pile[randomIndexToSwap];
			pile[randomIndexToSwap] = pile[i];
			pile[i] = temporaryCardHolder;
		}
    }

    // Method is used to get the index of a card, index is used to get the card's icon
    private int getIndexOf(String card) {

    	char[] characters = card.toCharArray();
    	int suit = Character.getNumericValue(characters[0]);
    	int cardCount = Character.getNumericValue(characters[1]) * 10  + Character.getNumericValue(characters[2]);
    	int index = suit * 13 + (cardCount - 1);
		return index;
    }

    /* Methods - UI */

    // Method is used when user wants to draw a new card from stock pile and discards a card from hand
    public String drawFromStock(String card) {

        String newCard = this.stockPile[this.stockPile.length - 1]; // gets the card from the top of the stock pile

        // makes new stock pile without the top card
        String[] newStockPile = Arrays.copyOf(this.stockPile, this.stockPile.length - 1);
        this.stockPile = newStockPile;

        // makes a new discard pile with the most recent top card of the stock pile
        String[] newDiscardPile = Arrays.copyOf(this.discardPile, this.discardPile.length + 1);
        newDiscardPile[newDiscardPile.length - 1] = card; // adds the discarded card to the new discard pile
        this.discardPile = newDiscardPile;

        return newCard;
    }

    // Method is used when user wants to draw a new card from discard pile
    public String drawFromDiscard(String card) {

        String newCard = this.discardPile[this.discardPile.length - 1]; // get the top card of discard pile
        this.discardPile[this.discardPile.length - 1] = card; // overwrite top card with argument
        return newCard;
    }

    // Method is used to get the icon of a card
    public ImageIcon getIconOf(String card) {

        int indexOfCard = this.getIndexOf(card); // (4)
        return this.cardImages[indexOfCard];
    }

    // Method is used to get the value of a card
    public int getValueOf(String card) {

		int value = Integer.parseInt(card.substring(1));

		if (value == 1) {

			value = 11;
		}
		else if (value > 10) {

			value = 10;
		}

		return value;
    }

    // Method is used for when the game starts and the player are given cards without having to discard cards
    public String[] drawStartingCards() {

        String[] startingCards = new String[3];
        for (int i = 0; i < startingCards.length; i++) {

            startingCards[i] = this.stockPile[this.stockPile.length - (i + 1)];
        }

        // makes new stock pile without the top 3 cards
        String[] newStockPile = Arrays.copyOf(this.stockPile, this.stockPile.length - 3);
        this.stockPile = newStockPile;

        return startingCards;
    }

    // Method is used to check if discard pile is empty
    public boolean isDiscardPileEmpty() {

        return (this.discardPile.length == 0);
    }

    // Method is used to check if stock pile is empty
    public boolean isStockPileEmpty() {

        return (this.stockPile.length == 0);
    }

    // Method is used by the computer for choosing which pile to draw from card
    public String peekTopOfDiscard() {

        if (!this.isDiscardPileEmpty()) {

            return this.discardPile[this.discardPile.length - 1];
        }
        else {

            return null;
        }
    }
}
