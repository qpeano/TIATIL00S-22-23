/* Class is used to make the window of an exercise logger that stores workouts that are logged in files
 *
 * @Shamiur Rahman Ramim [created: 2023-05-20 | last updated: 2023-0521]
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Window extends JFrame implements ActionListener {

	/* FIELDS */

	// visual
	private JLabel loggerLabel; // displays "Workout logger"
	private JTextField dateInputField; // user will input a date here

	private JButton searchButton; // user will search for workout by pressing this
	private JButton newWorkoutButton; // user will make new workout with date using this
	private JButton deleteWorkoutButton; // user will delete workout by pressing this
	private JLabel messageLabel; // displays different things, error messages for example

	private JTextField[] exerciseInputFields; // user will input name, sets, reps, and intensity of an exercise here
	private JButton saveExerciseButton; // user will save exercise to log using this

	private JPanel centerPanel; // the panel that houses all the exercies of a workout
	private ArrayList<JButton> exerciseButtons; // the buttons with the exerise information

	// internal
	private Logger workoutLogger; // logs the exercise info for later use
	private boolean isDisplayingWorkout; // checker so that an exercise can only be saved when a workout is displayed
	private int indexOfClickedExerciseButton; // tracks which button of the exercises that has been clicked

	/* METHODS - constructor */

	/**
	 * Constructor
	 *
	 * @param filePath the file name (path) that will house the workouts
	 */
	public Window(String filePath) {

        // following code makes the actual GUI
        super("Exercise Logger");
        this.setSize(500, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.BLACK);

        // following code sets up window
        this.setUpView(filePath);

        // following code sets up the interal fields
        this.exerciseButtons = new ArrayList<>();
        this.isDisplayingWorkout = false;
        this.indexOfClickedExerciseButton = -1;
	}

	/* METHODS - internal */

	/**
	 * Method sets upp window, instantiates and adds all starting components to view
	 *
	 * @param filePath the file name (path) that will house the workouts
     * @exception IOException if program can't connect to file, or read from it
	 */
	private void setUpView(String filePath) {

        try {

            // following code generates and add the components to window
            this.workoutLogger = new Logger(filePath);
            this.makeComponents();
            this.addComponentsToView();
        }
        catch (IOException exception) {

            JOptionPane.showMessageDialog(Window.this,
            "A PROBLEM HAS OCCURED IN FILE:\n" + exception.getMessage() + "\nPLEASE CHECK FILE AND FILE NAME OR TRY RESTARTING THE APP",
            "ERROR", JOptionPane.ERROR_MESSAGE);
        }
	}

    /**
     * Method is used to generate all starting objects
     *
     */
	private void makeComponents() {

		// instantiate the labels, make the text white
		this.loggerLabel = new JLabel("<html><span style='font-size: 30px' >Workout Logger</span><span style = 'font-size: 8px'> @qpeano</span></html>", SwingConstants.CENTER);
		this.loggerLabel.setForeground(Color.WHITE);
		this.messageLabel = new JLabel("<html><span style='font-size:20px' >input exercise info below, date above and press 'new' to create a workout<br>input date and press 'delete' to remove workout</span></html>", SwingConstants.CENTER);
		this.messageLabel.setForeground(Color.WHITE);

        // instantiate the input field
		this.dateInputField = new JTextField(this.getCurrentDate());
		this.dateInputField.setBackground(Color.WHITE);
		this.dateInputField.setForeground(Color.BLACK);

        // instantiate and "decorate" the search button
        // border and text will change color to red if user hovers mouse above "search" button
		this.searchButton = this.makeButton("search", Color.BLUE);

        // instantiate and "decorate" the "new workout" button
        // border and text will change color to red if user hovers mouse above "new workout" button
		this.newWorkoutButton = this.makeButton("new", Color.GREEN);


        // instantiate and "decorate" the "new workout" button
        // border and text will change color to red if user hovers mouse above "new workout" button
		this.deleteWorkoutButton = this.makeButton("delete", Color.RED);

		// instantiate and fill the exercise input fields
		String[] starterText = {"exercise (back-squat)", "sets (2)", "reps (8)", "intensity (79kg)"};
		this.exerciseInputFields = new JTextField[starterText.length];

		for (int i = 0; i < starterText.length; i++) {

			this.exerciseInputFields[i] = new JTextField(starterText[i], 10);
		}

        // instantiate and "decorate" the "save" button
        // border and text will change color to red if user hovers mouse above "save" button
		this.saveExerciseButton = this.makeButton("save", Color.GREEN);

        // instantiate the section of the window that will house the exercises
		this.centerPanel = new JPanel(new GridLayout(0, 1, 2, 2));
		this.centerPanel.setBackground(Color.BLACK);
	}

    /**
     * Method is used to get the date of current day
     *
     * @return date the date of today
     */
    private String getCurrentDate() {

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

	/**
	 * Method makes and decorates a button with a particular hover color and display text
	 *
	 * @param displayString the string to be displayed on the button
	 * @param color the color of the text and border
	 * @return the created button
	 */
	private JButton makeButton(String displayString, Color color) {

        // instantiate and "decorate" the button
		JButton button = new JButton(displayString);
		button.setBackground(Color.BLACK);
		button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		button.setForeground(Color.WHITE);
		button.addActionListener(this);

        // border and text will change color to red if user hovers mouse above clear button
		button.addMouseListener(new MouseAdapter() {

		    public void mouseEntered(MouseEvent evt) { // when user is hovering

				button.setBorder(BorderFactory.createLineBorder(color, 2));
				button.setForeground(color);
		    }

		    public void mouseExited(MouseEvent evt) { // when user stops

				button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				button.setForeground(Color.WHITE);
		    }
		});

		return button;
	}

	/**
	 * Method adds starting components to view
	 *
	 */
	private void addComponentsToView() {

        // northPanel is north of the window
		JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.BLACK);

        JPanel nNorth = new JPanel(new BorderLayout()); // north of northPanel
        nNorth.setBackground(Color.BLACK);
        JPanel cNorth = new JPanel(new BorderLayout()); // center of northPanel
        cNorth.setBackground(Color.BLACK);
        JPanel sNorth = new JPanel(new BorderLayout()); // south of northPanel
        sNorth.setBackground(Color.BLACK);

        JPanel ncNorth = new JPanel(); // north of center of northPanel
        ncNorth.setBackground(Color.BLACK);
        JPanel scNorth = new JPanel(); // south of center of northPanel
        scNorth.setBackground(Color.BLACK);

        JPanel nsNorth = new JPanel(); // north of south of northPanel
        nsNorth.setBackground(Color.BLACK);
        JPanel ssNorth = new JPanel(); // south of south of northPanel
        ssNorth.setBackground(Color.BLACK);

        // following code adds components to the panels
        nNorth.add(this.loggerLabel, BorderLayout.NORTH);

        // north and south of the center panel
        ncNorth.add(this.dateInputField);
        scNorth.add(this.searchButton);
        scNorth.add(this.newWorkoutButton);
        scNorth.add(this.deleteWorkoutButton);

        // north and south of the south panel
        nsNorth.add(this.messageLabel);
        for (JTextField textField : this.exerciseInputFields) {

        	ssNorth.add(textField);
        }

        ssNorth.add(this.saveExerciseButton);

        // following code adds the subpanels to the bigger panels
        cNorth.add(ncNorth, BorderLayout.NORTH);
        cNorth.add(scNorth, BorderLayout.SOUTH);

        sNorth.add(nsNorth, BorderLayout.NORTH);
        sNorth.add(ssNorth, BorderLayout.SOUTH);

        northPanel.add(nNorth, BorderLayout.NORTH);
        northPanel.add(cNorth, BorderLayout.CENTER);
        northPanel.add(sNorth, BorderLayout.SOUTH);

        // add the bigger panels to view
        Container contentArea = this.getContentPane();
        contentArea.add(northPanel, "North");
        contentArea.add(this.centerPanel);
        this.setContentPane(contentArea);
	}

    /**
     * Method is used to alter the text of the message label
     *
     * @param message the message displayed on the label
     */
	private void makeMessage(String message) {

		String text = "<html><span style='font-size:20px' >" + message + "</span></html>";
		messageLabel.setText(text);
	}

    /**
     * Method is used to display the workout that user has searched for
     *
     * @param date the date of the workout
     */
	private void displaySearchedWorkout(String date) {

		if (this.isDisplayingWorkout) { // checker so that new exercises don't get piled up on the wrong workout

			this.centerPanel.removeAll();
			this.centerPanel.revalidate();
			this.centerPanel.repaint();
			this.exerciseButtons.clear();
		}

		try {

			if (this.workoutLogger.hasWorkoutDate(date)) { // checker if date is actually logged

                // following code gets the exercises of a workout and displays in to view IF there are exercises
				ArrayList<String> exercises = this.workoutLogger.getWorkout(date);
				if (!exercises.isEmpty()) {

					for (String exercise : exercises) {

						JButton exerciseButton = this.makeButton(exercise, Color.YELLOW);
						this.exerciseButtons.add(exerciseButton);
						this.centerPanel.add(exerciseButton);
					}

                    // alters message label
					this.makeMessage("Workout fetched successfully!");
				}
				else { // if there are no exercises in workoy, let user know

					this.makeMessage("Workout fetched successfully, but workout is empty!");
				}
				
				this.loggerLabel.setText("<html><span style='font-size:30px' > Current Workout: " + date + "</span></html>");
			}
			else { // if there is no record of a workout with date, let user know
				
				// for user
				this.makeMessage("There is no record of a workout on " + date);
			}
		}
		catch (Exception exception) { // if something happens, let user know what

			this.makeMessage(exception.getMessage());
		}

        // regenerate the view
		Container contentArea = this.getContentPane();
		this.setContentPane(contentArea);
		this.isDisplayingWorkout = true; // checker is active
	}

    /**
     * Method is used to add a new workout
     *
     * @param date the date of the workout
     */
	private void addNewWorkout(String date) {

		try {

			if (this.workoutLogger.hasWorkoutDate(date)) { // checker so that two workout don't havae the same date

				this.makeMessage("There already exists a workout on " + date);
			}
			else { // if no such workout exists, the following code generates new workout and displays it

				if (!this.isExerciseInputEmpty()) {

					String formattedExercise = this.formatExercise();

					this.workoutLogger.addWorkout(date, formattedExercise);
					this.displaySearchedWorkout(date);

					this.makeMessage("Workout created successfully!");
					this.isDisplayingWorkout = true;
				}
			}
		}
		catch (Exception exception) { // if something goes wrong, let user know

			this.makeMessage(exception.getMessage());
		}
	}

    /**
     * Method is used to check if any of the exercise input fields are empty
     *
     * @return if any of the fields are empty
     */
	private boolean isExerciseInputEmpty() {

        // following code checks if any of the input fields only has whitespace or nothing in it
		Pattern format = Pattern.compile("\\s*");
		Matcher formatMatcher;
		for (JTextField field : this.exerciseInputFields) {

			formatMatcher = format.matcher(field.getText());
			if (formatMatcher.matches()) { // if empty, return true

				return true;
			}
		}

        // if none of them are empty, return false
		return false;
	}

    /**
     * Method is used to format an exercise (using data from exercise input fields) to add to workoutLogger
     *
     * @return formatted exercise to add to workoutLogger
     */
	private String formatExercise() {

		StringBuilder formattedExercise = new StringBuilder(); // string rep

        // following code goes through each field and adds the content and an underscore to string rep
        for (int i = 0; i < this.exerciseInputFields.length; i++) {

			String exerciseInfo = this.exerciseInputFields[i].getText();
			formattedExercise.append(exerciseInfo);

			if (i != this.exerciseInputFields.length - 1) {

				formattedExercise.append("_");
			}
		}

		return formattedExercise.toString();
	}

    /**
     * Method is used to add an exercise to the current workout
     *
     */
	private void addExercise() {

		try {

			if (!this.isExerciseInputEmpty()) { // check so that input fields aren't empty

                // following code gets the content of the input fields and formatts it and then adds the formatted
                // exercise to current workout
				String formattedExercise = this.formatExercise();
				this.workoutLogger.addExercise(formattedExercise);

                // current workout is displayed
				this.displaySearchedWorkout(this.workoutLogger.getCurrentDate());
				this.makeMessage("Added exercise successfully!");
			}
			else { // if input any input field is empty, let user know

				this.makeMessage("You cannot add new exercise without filling in every field below");
			}
		}
		catch (Exception exception) {

			this.makeMessage(exception.getMessage());
		}
	}

    /**
     * Method is used to edit an exercise in current workout
     *
     * @param index the index of the workout that is to be edited
     */
	private void editExercise(int index) {

		try {

			if (!this.isExerciseInputEmpty()) { // if no input field is empty

                // get the exercises that are in current workout
				String formattedExercise = this.formatExercise();
				ArrayList<String> exercises = this.workoutLogger.getCurrentWorkoutRaw();

                // erase every exercise from the workout
				this.workoutLogger.clearCurrentWorkout();

                // following code re-adds exercises to workout, but the exercise with the
                // same index as the argument is replaced with exercise made of data from exercise input fields
				for (int i = 0; i < exercises.size(); i++) {

					if (i == index) {

						this.workoutLogger.addExercise(formattedExercise);
					}
					else {

						this.workoutLogger.addExercise(exercises.get(i));
					}
				}
				
				// display the edited workout
	            this.displaySearchedWorkout(this.workoutLogger.getCurrentDate());
                
	            // let user know it worked
				this.makeMessage("Edit successful!");
			}
			else { // if any input field is empty

                // get the exercises from current workout, delete the exercise with same index as the argument
				ArrayList<String> exercises = this.workoutLogger.getCurrentWorkoutRaw();
				exercises.remove(this.indexOfClickedExerciseButton);

                // erase all exercises from current workout
				this.workoutLogger.clearCurrentWorkout();

                // re-add exercises to current workout
				for (String exercise : exercises) {

					this.workoutLogger.addExercise(exercise);
				}
				
				// display the edited workout
	            this.displaySearchedWorkout(this.workoutLogger.getCurrentDate());
                
	            // let user know
				this.makeMessage("Exercise removed successfully!");
			}

            this.indexOfClickedExerciseButton = -1; // user has not clicked on exercise yet
		}
		catch (Exception exception) {

			this.makeMessage(exception.getMessage());
		}
	}

    /**
     * Method "de-formats" exercise, and adds content to the exercise input fields
     *
     * @param formattedString
     */
	private void addToInputFields(String formattedString) {

		String[] inputStrings = formattedString.replace(" | ", " ").split(" "); // removes formatting
		for (int i = 0; i < inputStrings.length; i++) {

			this.exerciseInputFields[i].setText(inputStrings[i]); // adds to input fields
		}
	}

    /**
     * Method is used to delete a workout
     *
     * @param date the date of the workout
     */
	private void deleteWorkout(String date) {

		try {

			if (this.workoutLogger.hasWorkoutDate(date)) { // checks if workout with specified date even exists

                // removes workout, displays it, (nothing), and lets user know
				this.workoutLogger.removeWorkout(date);
				this.displaySearchedWorkout(date);
				this.makeMessage("Workout deleted");
				this.isDisplayingWorkout = false;
			}
			else { // if it doesn't exist, let user know

				this.makeMessage("Workout does not exist");
			}
		}
		catch (Exception exception) {

			this.makeMessage(exception.getMessage());
		}
	}

    /**
     * Method checks if date is empty or not
     *
     * @param date the date from the date field
     */
    private boolean isDateEmpty(String date) {

        Pattern format = Pattern.compile("\\s*");
        Matcher formatMatcher = format.matcher(date);

        if (formatMatcher.matches()) {

            return true;
        }

        return false;
    }

    /**
     * Method is used to handle all the events
     *
     * @param event the action made by user (clicking buttons)
     */
	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == this.searchButton) { // if user has searched for a workout

			// get what user has written, clears date field
			String date = this.dateInputField.getText();
			this.dateInputField.setText("");
			this.dateInputField.setColumns(10);

            // following code is used to check if the date field is empty, if not, then program searches for workout
            if (!this.isDateEmpty(date)) {

                this.displaySearchedWorkout(date);
            }
		}
		else if (event.getSource() == this.newWorkoutButton) { // if user wants to create new a workout

            // get what user has written, clears date field
			String date = this.dateInputField.getText();
			this.dateInputField.setText("");
			this.dateInputField.setColumns(10);

            // following code is used to check if the date field is empty, if not, then program adds workout
            if (!this.isDateEmpty(date)) {

                this.addNewWorkout(date);
            }
		}
		else if (event.getSource() == this.saveExerciseButton) { // if user wants to save an exercise to workout

			if (this.isDisplayingWorkout) { // checks if a workout is even displayed

				if (this.indexOfClickedExerciseButton != -1) { // if user has clicked on an exercise

					this.editExercise(this.indexOfClickedExerciseButton); // edits that exercise
				}
				else { // else, add new exercise to current workout

					this.addExercise();
				}
			}
		}
		else if (event.getSource() == this.deleteWorkoutButton) { // if user wants to delete a workout

            // get what user has written, clears date field
			String date = this.dateInputField.getText();
			this.dateInputField.setText("");
			this.dateInputField.setColumns(10);

            // following code is used to check if the date field is empty, if not, then program deletes workout
            if (!this.isDateEmpty(date)) {

                this.deleteWorkout(date);
            }
		}
		else { // if none of the normal buttons has been clicked, check if exercise buttons has been clicked

            // goes through all exercise buttons
			for (int i = 0; i < this.exerciseButtons.size(); i++) {

				if (event.getSource() == this.exerciseButtons.get(i)) { // if a button has been found to have been pressed

					this.indexOfClickedExerciseButton = i; // get the index

                    // add its content to exercise input fields, let user know they are in edit mode
					this.addToInputFields(this.exerciseButtons.get(i).getText());
					this.makeMessage("You are in edit mode, leave any of the fields below blank and press save to remove exercise");
				}
			}
		}
	}
}
