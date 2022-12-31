import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements ActionListener {

    /* Fields */

    private JButton stockPileButton; // button for when the user wants to draw card from stock pile
    private JButton discardPileButton; // button for when the user wants to draw card from discard pile
    private JButton[] userCardButtons; // the buttons that are visible when the user chooses which card to discard
    private JButton doneWithTurnButton; // turn goes to computer, used as knock button when no no exchange occurs

    private JLabel[] computerCardLabels; // the labels that display the cards of the computer
    private JLabel[] userCardLabels; // the labels that display the cards of the user
    private JLabel statusMessage; // tells what computer does and when it is the user's turn

    private Player computer; // the object representing the computer
    private Player user; // the object representing the user

    private ImageIcon backOfCardImage; // the image for the back of a card

    private Color colorOfBoard; // the color the board

    private boolean isDrawingFromStock; // remembers which pile to draw a card from
    private boolean exchangeHasOccurred; // used as a signal to determine if user knocks or not

    private double userScore; // score of user
    private double computerScore; // score of computer
    private boolean isUserKnocking;

    /* Constructor */
    public GameWindow() {

        super("31");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.backOfCardImage = new ImageIcon("back_of_card.png");
        this.colorOfBoard = Color.decode("#35654d");
        this.setUpRound();
    }

    /* Internal */

    // Method sets up the window and all objects that are needed for the round (a singular game of 31) to start
    private void setUpRound() {

        this.user = new Player(false);
        this.computer = new Player(true);
        this.isDrawingFromStock = false; // starting value, means nothing yet
        this.exchangeHasOccurred = false;
        this.userScore = 0;
        this.computerScore = 0;
        this.isUserKnocking = false;

        this.generatePlayerComponents();
        this.generateOtherComponents();
        this.addAllComponentsToView();
    }

    // Method creates all things that the user will use in a game of 31
    private void generatePlayerComponents() {

        ImageIcon[] imagesOfCardsOnHand = this.getOnHandCardImages(this.user);
        this.userCardLabels = new JLabel[3];
        this.generatImageLabels(this.userCardLabels, imagesOfCardsOnHand);
        this.makeButtons();
    }

    // Method makes and returns an array of the images of the cards on a user's/ computer's hand
    private ImageIcon[] getOnHandCardImages(Player p) {

        ImageIcon[] imagesOfCardsOnHand = new ImageIcon[3];
        for (int i = 0; i < imagesOfCardsOnHand.length; i++) {

            imagesOfCardsOnHand[i] = p.getImageIconOf(i);
        }

        return imagesOfCardsOnHand;
    }

    // Method sets a list of labels to be a list of only labels with images
    private void generatImageLabels(JLabel[] cardLabels, ImageIcon[] cardImages) {

        for (int i = 0; i < cardLabels.length; i++) {

            cardLabels[i] = new JLabel(cardImages[i]);
        }
    }

    // Method makes all the buttons
    private void makeButtons() {

        // create and set up the buttons that are visible most of the time
		this.doneWithTurnButton = new JButton("done");
		this.doneWithTurnButton.addActionListener(this);

		this.stockPileButton = new JButton(this.backOfCardImage);
		this.stockPileButton.setBackground(this.colorOfBoard);
		this.stockPileButton.addActionListener(this);

		this.discardPileButton = new JButton();
		this.discardPileButton.addActionListener(this);
		this.discardPileButton.setBackground(this.colorOfBoard);
		this.discardPileButton.setVisible(false); // discard pile should not be visible when empty

        // create the buttons that are visible when user chooses which card to discard
        ImageIcon[] imagesOfCardsOnHand = this.getOnHandCardImages(this.user);
        this.userCardButtons = new JButton[3];
        for (int i = 0; i < this.userCardButtons.length; i++) {

            this.userCardButtons[i] = new JButton(imagesOfCardsOnHand[i]);
            this.userCardButtons[i].addActionListener(this);
            this.userCardButtons[i].setBackground(this.colorOfBoard);
            this.userCardButtons[i].setVisible(false); // card buttons should only be visible when discarding a card
        }
    }

    // Method makes all the components that the user can't interact with
    private void generateOtherComponents() {

        // create the status message
        this.statusMessage = new JLabel("Your sum is: " + this.user.getSumOfCards());

        // create the image labels for the cards of the computer, because its cards should be hidden from the user
        // the cards will only be displayed as the from the back, till someone knocks
        this.computerCardLabels = new JLabel[3];
        ImageIcon[] backOfCardImageList = {this.backOfCardImage, this.backOfCardImage, this.backOfCardImage};
        this.generatImageLabels(this.computerCardLabels, backOfCardImageList);
    }

    // Method adds all components to the container/ view
    private void addAllComponentsToView() {

		Container contentArea = getContentPane();
		contentArea.setBackground(this.colorOfBoard);

		// add some of the buttons to view
		contentArea.add("West", this.discardPileButton);
		contentArea.add("East", this.stockPileButton);
		contentArea.add("South", this.doneWithTurnButton);

		// make section for status message, and add it to view
		JPanel messageSection = new JPanel();
		messageSection.add(this.statusMessage);
		contentArea.add("North", messageSection);

        // add all card labels to view, and set the view
        this.addCardLabels(contentArea);
        setContentPane(contentArea);
    }

	// Method is used to add the card labels (and buttons) to the window.
	private void addCardLabels(Container container) {

		// the entire section for the card labels to be put in
		JPanel cardSection = new JPanel(new BorderLayout());
		cardSection.setBackground(this.colorOfBoard);

		// space for the computer's cards (labels)
		JPanel computerSubsection = new JPanel();
		computerSubsection.setBackground(this.colorOfBoard);

		// add the card labels to window
		for (int i = 0; i < this.computerCardLabels.length; i++) {

			computerSubsection.add(this.computerCardLabels[i]);
		}

		// space for the user's cards (labels)
		JPanel playerSubsection = new JPanel();
		playerSubsection.setBackground(this.colorOfBoard);

		// add the card labels to window
		for (int i = 0; i < this.userCardLabels.length; i++) {

			playerSubsection.add(this.userCardLabels[i]);
			playerSubsection.add(this.userCardButtons[i]);
		}

		// add subsections for computer and for user to entire section
		// add  entire section to window
		cardSection.add(computerSubsection, BorderLayout.NORTH);
		cardSection.add(playerSubsection, BorderLayout.SOUTH);
		container.add("Center", cardSection);
	}

    // Method makes the pile buttons invisible and "turns the card labels into card buttons"
	private void showCardButtons() {

        // the pile buttons and turn button should not be visible, so that the user doesn't
        // accidentally click them, resulting in weird behavior
		this.stockPileButton.setVisible(false);
		this.discardPileButton.setVisible(false);
        this.doneWithTurnButton.setVisible(false);

        // the card buttons should be visible, and the card labels - invisible.
		for (int i = 0; i < this.userCardLabels.length; i++) {

			this.userCardLabels[i].setVisible(false);
			this.userCardButtons[i].setVisible(true);
		}
	}

    // Method is used to exchange a card with either stock pile or discard pile
    private void discardCard(int indexOfDiscardedCard) {

    	ImageIcon imageOfDiscardedCard = user.getImageIconOf(indexOfDiscardedCard); // used to display card on discard pile
        if (this.isDrawingFromStock) { // if user clicked to exchange with stock

            this.user.drawCardFromStock(indexOfDiscardedCard);
        }
        else { // if they didn't

            this.user.drawCardFromDiscard(indexOfDiscardedCard);

            this.user.getImageIconOf(indexOfDiscardedCard).getDescription();
        }

        this.alterView(imageOfDiscardedCard); // reload view, with the discarded card on discard pile
    }

    // Method is used to change settings on discard buttons, and reload view
    private void alterView(ImageIcon icon) {

        // code block below is used to get images of the new cards of user, and make discard button visible
        // with the image of it being the last card that was discarded
        this.generatePlayerComponents();
        this.generateOtherComponents();
    	this.discardPileButton.setVisible(true);
        this.discardPileButton = new JButton(icon);
        this.discardPileButton.addActionListener(this);
        this.discardPileButton.setBackground(this.colorOfBoard);
        getContentPane().removeAll();

        // if stock pile is empty and there is a discard pile, the button of stock pile should be invisible (= no stock pile)
        if (this.user.isStockPileEmpty() && this.stockPileButton.isVisible()) {

        	this.stockPileButton.setVisible(false);
        }

        // reload the view
        this.addAllComponentsToView();
    }

    // Method displays the computers cards, as well as making buttons invisible
    private void displayComputersCards() {

    	this.stockPileButton.setVisible(false);
    	this.discardPileButton.setVisible(false);
        this.doneWithTurnButton.setVisible(false);

        ImageIcon[] imagesOfCardsOnHand = this.getOnHandCardImages(this.computer);
        for (int i = 0; i < this.computerCardLabels.length; i++) {

        	this.computerCardLabels[i].setIcon(imagesOfCardsOnHand[i]);
        }
    }

    // Method compares the card sum of user and computer, and declares a winner
    private void declareWinner() {

    	double playerSum = this.user.getSumOfCards();
    	double computerSum = this.computer.getSumOfCards();

        if (playerSum > computerSum && playerSum <= 31) { // the sum should always be less than or equal to 31

            this.statusMessage.setText("You win this round");
            this.userScore++;
        }
        else if (playerSum == computerSum) {

            this.statusMessage.setText("404 no winner found for this round");
        }
        else {

            this.statusMessage.setText("Computer wins this round");
            this.computerScore++;
        }
    }

    // Method is used to simulate the computer making a move.
    private void initiateComputerMove() {

        if (this.computer.isDrawingCard()) { // if computer isn't already knocking

            ImageIcon imageOfDiscardedCard = this.computer.makeMove(); // the image of the discarded card
            this.alterView(imageOfDiscardedCard); // reload view, with the discarded card on discard pile
        }
        else { // if it is, then display that it is knocking
        	
        	System.out.println("hello world");
            this.statusMessage.setText("Computer is knocking, it's your turn");
        }
    }

    private void act(ActionEvent event) {

        if (event.getSource() == this.stockPileButton) { // if user clicked the stock pile

            if (!this.exchangeHasOccurred) { // checks if user hasn't drawn yet

                this.isDrawingFromStock = true;
                this.showCardButtons(); // show players cards as buttons for user to choose a card to discard
            }
        }
        else if (event.getSource() == this.discardPileButton) { // if user clicked the discard pile

            if (!this.exchangeHasOccurred) { // checks if user hasn't drawn yet

                this.isDrawingFromStock = false;
                this.showCardButtons(); // show players cards as buttons for user to choose a card to discard
            }
        }
        else {

            // loop goes through the card buttons to see which card was clicked
            for (int indexOfDiscardedCard = 0; indexOfDiscardedCard < this.userCardButtons.length; indexOfDiscardedCard++) {

                // if card is found
                if (event.getSource() == this.userCardButtons[indexOfDiscardedCard]) {

                    this.discardCard(indexOfDiscardedCard); // card is discarded
                    break;
                }
            }

            this.exchangeHasOccurred = true; // a card exchange has thus occurred
        }
    }

    /* Event Handler */

	// method is used to simulate whatever happens when user has
	// clicked a button.
	@Override
    public void actionPerformed(ActionEvent event) {

        if (this.isUserKnocking) {

            this.initiateComputerMove();
        }
        else if (!this.computer.isDrawingCard()) {

            this.act(event);
        }
        else {

            if (event.getSource() == this.doneWithTurnButton) { // if user is done with turn
            	
            	this.initiateComputerMove();
                if (!this.exchangeHasOccurred) {

                    this.isUserKnocking = true;
                    this.doneWithTurnButton.setText("show cards");
                }
                
                this.exchangeHasOccurred = false;
            }
            else {

                this.act(event);
            }
            return;
        }
        
        this.displayComputersCards();
        this.declareWinner();
    }
}
