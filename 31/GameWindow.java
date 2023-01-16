/* Class simulates the game "31"
 *
 * Author: Shamiur Rahman Ramim
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;

 public class GameWindow extends JFrame implements ActionListener {

    /* Fields */

    private JButton stockPileButton; // button for when the user wants to draw card from stock pile
    private JButton discardPileButton; // button for when the user wants to draw card from discard pile
    private JButton[] userCardButtons; // the buttons that are visible when the user chooses which card to discard
    private JButton doneWithTurnButton; // turn goes to computer, used as knock button when no exchange occurs
    private JButton nextRoundButton; // resets the round

    private JLabel[] computerCardLabels; // the labels that display the cards of the computer
    private JLabel[] userCardLabels; // the labels that display the cards of the user

    private JPanel statusBar; // tells what computer does and when it is the user's turn, and also the scores and sums

    private Player computer; // the object representing the computer
    private Player user; // the object representing the user

    private ImageIcon backOfCardImage; // the image for the back of a card

    private Color colorOfBoard; // the color the board

    private boolean isDrawingFromStock; // remembers which pile to player wants to draw a card from
    private boolean exchangeHasOccurred; // used as a signal to determine if user knocks or not
    private boolean userIsKnocking; // signal to give computer another turn, and then end the game
    private boolean computerIsKnocking;

    private int userScore; // score of the player
    private int computerScore; // score of the computer

    private Board board;

    /* Methods - Constructor */

    public GameWindow() {

        // following code generates and sets up the actual GUI
        super("31");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // following code sets up the game
        this.backOfCardImage = new ImageIcon("back_of_card.png");
        this.colorOfBoard = Color.decode("#35654d");
        this.setUpRound();

        // scores are initialized here to stop them from being reset to 0 after evey round
        this.userScore = 0;
        this.computerScore = 0;
    }

    /* Methods - Internal */

    // Method sets up the window and all objects that are needed for the round (a singular game of 31) to start
    private void setUpRound() {

        // following code generates most of the objects and variables that can't be seen in window
    	this.board = new Board();
        this.user = new Player(this.board, false);
        this.computer = new Player(this.board, true);
        this.isDrawingFromStock = false; // starting value, means nothing yet
        this.exchangeHasOccurred = false;
        this.userIsKnocking = false;
        this.computerIsKnocking = false;

        // following code generates and add the components to window
        this.generateUserComponents();
        this.generateOtherComponents();
        this.addComponentsToView();
    }

    // Method creates all things that the user will use in a game of 31
    private void generateUserComponents() {

        ImageIcon[] imagesOfCardsOnHand = this.getOnHandCardImages(this.user);
        this.userCardLabels = new JLabel[3];
        this.generatImageLabels(this.userCardLabels, imagesOfCardsOnHand);
        this.makeButtons();
    }

    // Method makes and returns an array of the images of the cards on a user's/ computer's hand
    private ImageIcon[] getOnHandCardImages(Player p) {

        ImageIcon[] imagesOfCardsOnHand = new ImageIcon[3];
        for (int i = 0; i < imagesOfCardsOnHand.length; i++) {

            imagesOfCardsOnHand[i] = p.getImageIconOf(this.board, i);
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
		this.doneWithTurnButton = new JButton("Knock");
		this.doneWithTurnButton.addActionListener(this);

		this.stockPileButton = new JButton(this.backOfCardImage);
		this.stockPileButton.setBackground(this.colorOfBoard);
		this.stockPileButton.addActionListener(this);

		this.discardPileButton = new JButton();
		this.discardPileButton.addActionListener(this);
		this.discardPileButton.setBackground(this.colorOfBoard);
		this.discardPileButton.setVisible(false); // discard pile should not be visible when empty

        // create the button that is only visible when a round has ended
        this.nextRoundButton = new JButton("Next round");
        this.nextRoundButton.addActionListener(this);
        this.nextRoundButton.setVisible(false);

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

        // create and fill the status section
        this.statusBar = new JPanel(new BorderLayout());

        String statusMessage = "Your sum is " + this.user.getSumOfCards();
        this.updateStatusBar(statusMessage);

        // create the image labels for the cards of the computer, because its cards should be hidden from the user
        // the cards will only be displayed as the from the back, till someone knocks
        this.computerCardLabels = new JLabel[3];
        ImageIcon[] backOfCardImageList = {this.backOfCardImage, this.backOfCardImage, this.backOfCardImage};
        this.generatImageLabels(this.computerCardLabels, backOfCardImageList);
    }

    // Method is used to update the status bar so that it has the right scores and has the right status message
    private void updateStatusBar(String statusMessage) {

        // erases the content of the status bar
        this.statusBar.removeAll();

        // re-populate status bar with new scores and a status message
        this.statusBar.add(new JLabel(statusMessage), BorderLayout.NORTH);
        this.statusBar.add(new JLabel("Computer: " + this.computerScore), BorderLayout.WEST);
        this.statusBar.add(new JLabel("User: " + this.userScore), BorderLayout.EAST);
        revalidate();
        repaint();
    }

    // Method adds all components to the container/ view
    private void addComponentsToView() {

		Container contentArea = getContentPane();
		contentArea.setBackground(this.colorOfBoard);

		// add some of the buttons to view
		contentArea.add("West", this.discardPileButton);
		contentArea.add("East", this.stockPileButton);
		contentArea.add("South", this.doneWithTurnButton);
		contentArea.add("North", this.statusBar);

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

    // ---------
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

    	ImageIcon imageOfDiscardedCard = user.getImageIconOf(this.board, indexOfDiscardedCard); // used to display card on discard pile
        if (this.isDrawingFromStock) { // if user clicked to exchange with stock

            this.user.drawCardFromStock(this.board, indexOfDiscardedCard);
        }
        else { // if they didn't

            this.user.drawCardFromDiscard(this.board, indexOfDiscardedCard);

            this.user.getImageIconOf(this.board, indexOfDiscardedCard).getDescription();
        }

        this.alterView(imageOfDiscardedCard); // reload view, with the discarded card on discard pile
    }

    // Method is used to change settings on discard buttons, and reload view
    private void alterView(ImageIcon icon) {

        // code block below is used to get images of the new cards of user, and make discard button visible
        // with the image of it being the last card that was discarded
        this.generateUserComponents();
        this.generateOtherComponents();
    	this.discardPileButton.setVisible(true);
        this.discardPileButton = new JButton(icon);
        this.discardPileButton.addActionListener(this);
        this.discardPileButton.setBackground(this.colorOfBoard);
        getContentPane().removeAll();

        // if stock pile is empty and there is a discard pile, the button of stock pile should be invisible (= no stock pile)
        if (this.board.isStockPileEmpty() && this.stockPileButton.isVisible()) {

        	this.stockPileButton.setVisible(false);
        }

        // reload the view
        this.addComponentsToView();
        revalidate();
        repaint();
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


            this.userScore++;
            this.updateStatusBar("You win this round");
        }
        else if (playerSum == computerSum) {

            this.updateStatusBar("No winner");
        }
        else {

            this.computerScore++;
            this.updateStatusBar("Computer wins this round");
        }

        if (!(this.computerScore == 4 || this.userScore == 4)) {

            this.nextRoundButton.setVisible(true);
            getContentPane().add("South", this.nextRoundButton);
        }
        else {

        	this.updateStatusBar("Game has ended");
        }
    }

    // Method is used to simulate the computer making a move.
    private void initiateComputerMove() {

    	ImageIcon imageOfDiscardedCard = this.computer.makeMove(this.board); // the image of the discarded card or nothing
        if (!(imageOfDiscardedCard == null)) { // if computer is/ should be a drawing card

            this.alterView(imageOfDiscardedCard); // reload view, with the discarded card on discard pile
        }
        else {

        	// System.out.println("hello");
        	this.computerIsKnocking = true;
        	this.updateStatusBar("Computer is knocking");
        }
    }
    
    // Method checks if user clicked any of the card buttons. If so, it discards that card
    private boolean userIsDiscarding(ActionEvent event) {

        boolean userIsDiscarding = false;
        // loop goes through the card buttons to see which card was clicked
        for (int indexOfDiscardedCard = 0; indexOfDiscardedCard < this.userCardButtons.length; indexOfDiscardedCard++) {

            if (event.getSource() == this.userCardButtons[indexOfDiscardedCard]) { // if card is found

                this.discardCard(indexOfDiscardedCard); // card is discarded
                userIsDiscarding = true;

                this.exchangeHasOccurred = true;
                this.alterTurnButton("Done with turn");
                revalidate();
                repaint();
                return userIsDiscarding;
            }
        }

        return userIsDiscarding;
    }
    
    // Method checks if user has tried to change the game somehow, i.e. wanted to go to next round, 
    // or just was done with thier round. If so, then methd changes environment according to 
    // which button was pushed
    private boolean userIsChangingGame(ActionEvent event) {

        boolean userIsChangingGame = false;
        boolean someoneIsKnocking = this.userIsKnocking || this.computerIsKnocking;

        if (event.getSource() == this.doneWithTurnButton && !someoneIsKnocking) { // if user clicked the right button while no one has knocked

        	System.out.println(this.exchangeHasOccurred);
            if (this.exchangeHasOccurred) { // reset the text of the button to "Knock" if user has discarded a card

            	this.alterTurnButton("Knock");
            	System.out.println("hello");
            }
            else { // set the text of the button to show cards, let user know it knocked

                this.updateStatusBar("You are knocking");
                this.alterTurnButton("Show cards");
                this.userIsKnocking = true;
            }

            userIsChangingGame = true;
            this.initiateComputerMove(); // let computer do it's thing 
            this.exchangeHasOccurred = false;
        }
        else if (event.getSource() == this.doneWithTurnButton && someoneIsKnocking) { // if someone is knocking

            this.displayComputersCards(); 
            this.declareWinner();
            userIsChangingGame = true;
        }

        return userIsChangingGame;
    }
    
    private void alterTurnButton(String text) {
    	
    	this.doneWithTurnButton.setText(text);
        revalidate();
        repaint();
    }

    /* Methods - Event Handler */
    
	// Method is used to simulate whatever happens when user has
	// clicked a button.
	@Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == this.stockPileButton) { // if user clicked the stock pile

            if (!this.exchangeHasOccurred && !this.userIsKnocking) { // checks if user hasn't drawn yet and isn't knocking, so user doesn't draw again before computer's turn

                this.isDrawingFromStock = true;
                this.showCardButtons(); // show players cards as buttons for user to choose a card to discard
            }
            else {
            	
            	this.updateStatusBar("User has knocked and can thus not draw a card");
            }
        }
        else if (event.getSource() == this.discardPileButton) { // if user clicked the discard pile

            if (!this.exchangeHasOccurred && !this.userIsKnocking) { // checks if user hasn't drawn yet and isn't knocking, so user doesn't draw again before computer's turn

                this.isDrawingFromStock = false;
                this.showCardButtons(); // show players cards as buttons for user to choose a card to discard
            }
            else {
            	
            	this.updateStatusBar("User has knocked and can thus not draw a card");
            }
        }
        else if (event.getSource() == this.nextRoundButton) { // if user clicked the next round button, environment will reset

    		getContentPane().removeAll();
    		this.setUpRound();
    	}
        else { // if user has pushed any of the other buttons

        	this.userIsDiscarding(event); // checks if user has pushed any card buttons, if yes => discard that card
            this.userIsChangingGame(event); // checks if user has pushed any "game changer" buttons (done with turn, next round), and does something
        }
	}
 }
	    
