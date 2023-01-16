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
        this.addClubs(0);
        this.addDiamonds(13);
        this.addHearts(26);
        this.addSpades(39);
    }

    // Method adds all cards that are clubs to this.cardImages
	private void addClubs(int startIndex) {

		this.cardImages[startIndex++] = new ImageIcon("ace_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("2_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("3_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("4_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("5_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("6_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("7_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("8_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("9_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("10_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("jack_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("queen_of_clubs.png");
		this.cardImages[startIndex++] = new ImageIcon("king_of_clubs.png");
	}

    // Method adds all diamonds that are clubs to this.cardImages
	private void addDiamonds(int startIndex) {

		this.cardImages[startIndex++] = new ImageIcon("ace_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("2_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("3_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("4_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("5_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("6_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("7_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("8_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("9_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("10_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("jack_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("queen_of_diamonds.png");
		this.cardImages[startIndex++] = new ImageIcon("king_of_diamonds.png");
	}

    // Method adds all cards that are hearts to this.cardImages
	private void addHearts(int startIndex) {

		this.cardImages[startIndex++] = new ImageIcon("ace_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("2_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("3_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("4_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("5_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("6_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("7_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("8_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("9_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("10_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("jack_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("queen_of_hearts.png");
		this.cardImages[startIndex++] = new ImageIcon("king_of_hearts.png");
	}

    // Method adds all cards that are spades to this.cardImages
	private void addSpades(int startIndex) {

		this.cardImages[startIndex++] = new ImageIcon("ace_of_spades2.png");
		this.cardImages[startIndex++] = new ImageIcon("2_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("3_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("4_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("5_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("6_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("7_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("8_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("9_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("10_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("jack_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("queen_of_spades.png");
		this.cardImages[startIndex++] = new ImageIcon("king_of_spades.png");
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
