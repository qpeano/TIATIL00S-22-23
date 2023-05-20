
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
	private JButton newWorkoutButton; // user will make new workout with today's date using this
	private JLabel messageLabel; // displays different things, error messages for example
	
	private JTextField[] exerciseInputFields; // user will input name, sets, reps, and intensity of an exercise here
	private JButton saveExerciseButton; // user will save exercise to log using this
	
	private JPanel centerPanel; // the panel that houses all the exercies of a workout
	private ArrayList<JButton> exerciseButtons; // the buttons with the exerise information
	
	// internal
	private Logger workoutLogger; // logs the exercise info for later use
	private boolean isDisplayingWorkout; // checker so that an exercise can only be saved when a workout is displayed
	private int indexOfClickedExerciseButton;
	
	/* METHODS - constructor */
	
	/**
	 * Constructor
	 * 
	 * @param filePath the file name (path) that will house the workouts
	 */
	public Window(String filePath) {
		
        super("Exercise Logger");
        this.setSize(500, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.BLACK);
        
        this.exerciseButtons = new ArrayList<>();
        this.isDisplayingWorkout = false;
        this.indexOfClickedExerciseButton = -1;
        this.setUpView(filePath);
	}
	
	/* METHODS - internal */
	
	/**
	 * Method sets upp window, instantiates and adds all starting components to view
	 * 
	 * @param filePath the file name (path) that will house the workouts
	 */
	private void setUpView(String filePath) {
		
        try {

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
		this.messageLabel = new JLabel("<html><span style='font-size:20px' >input exercise info below, date above and press 'new' to create a workout</span></html>", SwingConstants.CENTER);
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
		this.newWorkoutButton = this.makeButton("new", Color.BLUE);
		
		// instantiate and fill the exercise input fields
		String[] starterText = {"exercise (back-squat)", "sets (2)", "reps (8)", "intensity (79kg)"};
		this.exerciseInputFields = new JTextField[starterText.length];
		
		for (int i = 0; i < starterText.length; i++) {

			this.exerciseInputFields[i] = new JTextField(starterText[i], 10);
		}
		
        // instantiate and "decorate" the "save" button
        // border and text will change color to red if user hovers mouse above "save" button
		this.saveExerciseButton = this.makeButton("save", Color.BLUE);
		
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
	 * @param displayString
	 * @param color
	 * @return the button
	 */
	private JButton makeButton(String displayString, Color color) {
		
        // instantiate and "decorate" the clear (all tasks) button
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

		    public void mouseExited(MouseEvent evt) { // when user stps

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
        
        nNorth.add(this.loggerLabel, BorderLayout.NORTH);
        
        ncNorth.add(this.dateInputField);
        scNorth.add(this.searchButton);
        scNorth.add(this.newWorkoutButton);
        
        nsNorth.add(this.messageLabel);
        
        for (JTextField textField : this.exerciseInputFields) {
        	
        	ssNorth.add(textField);
        }
        
        ssNorth.add(this.saveExerciseButton);
        
        cNorth.add(ncNorth, BorderLayout.NORTH);
        cNorth.add(scNorth, BorderLayout.SOUTH);
        
        sNorth.add(nsNorth, BorderLayout.NORTH);
        sNorth.add(ssNorth, BorderLayout.SOUTH);
        
        northPanel.add(nNorth, BorderLayout.NORTH);
        northPanel.add(cNorth, BorderLayout.CENTER);
        northPanel.add(sNorth, BorderLayout.SOUTH);
        
        Container contentArea = this.getContentPane();
        contentArea.add(northPanel, "North");
        contentArea.add(this.centerPanel);
        this.setContentPane(contentArea);
	}
	
	private void makeMessage(String message) {
		
		String text = "<html><span style='font-size:20px' >" + message + "</span></html>";
		messageLabel.setText(text);
	}
	
    /**
     * Method is used to display the workout that user has searched for
     *
     * @param date the date
     */
	private void displaySearchedWorkout(String date) {
	
		if (this.isDisplayingWorkout) {
			
			this.centerPanel.removeAll();
			this.centerPanel.revalidate();
			this.centerPanel.repaint();
			this.exerciseButtons.clear();
		}
		
		try {
			
			if (this.workoutLogger.hasWorkoutDate(date)) {
				
				ArrayList<String> exercises = this.workoutLogger.getWorkout(date);
				if (!exercises.isEmpty()) {
					
					for (String exercise : exercises) {
						
						JButton exerciseButton = this.makeButton(exercise, Color.YELLOW);
						this.exerciseButtons.add(exerciseButton);
						this.centerPanel.add(exerciseButton);
					}
					
					this.makeMessage("Workout fetched successfully!");
				}
				else {
					
					this.makeMessage("Workout fetched successfully, but workout is empty!");
				}
			}
			else {
					
				this.makeMessage("There is no record of a workout on " + date);
			}	
		}
		catch (Exception exception) {
				
			this.makeMessage(exception.getMessage());
		}
		
		Container contentArea = this.getContentPane();
		this.setContentPane(contentArea);
		this.isDisplayingWorkout = true;
	}
	
	private void addNewWorkout(String date) {
		
		try {
			
			if (this.workoutLogger.hasWorkoutDate(date)) {
				
				this.makeMessage("There already exists a workout on " + date);
			} 
			else {
				
				if (!this.isExerciseInputEmpty()) {
					
					String formattedExercise = this.formatExercise();
					
					this.workoutLogger.addWorkout(date, formattedExercise);
					this.displaySearchedWorkout(date);	
					
					this.makeMessage("Workout created successfully!");
					this.isDisplayingWorkout = true;
				}
			}
		}
		catch (Exception exception) {
			
			this.makeMessage(exception.getMessage());
		}
	}


	private boolean isExerciseInputEmpty() {
		
		Pattern format = Pattern.compile("\\s*");
		Matcher formatMatcher;
		for (JTextField field : this.exerciseInputFields) {
			
			formatMatcher = format.matcher(field.getText());
			if (formatMatcher.matches()) {
				
				return true;
			}
		}
		
		return false;
	}
	
	private String formatExercise() {
		
		StringBuilder formattedExercise = new StringBuilder();
		for (int i = 0; i < this.exerciseInputFields.length; i++) {
			
			String exerciseInfo = this.exerciseInputFields[i].getText();
			formattedExercise.append(exerciseInfo);
			
			if (i != this.exerciseInputFields.length - 1) {
				
				formattedExercise.append("_");
			}
		}
		
		System.out.println(formattedExercise.toString());
		return formattedExercise.toString();
	}
	
	private void addExercise() {
		
		try {
			
			if (!this.isExerciseInputEmpty()) {
				
				String formattedExercise = this.formatExercise();
				this.workoutLogger.addExercise(formattedExercise);
				
				this.displaySearchedWorkout(this.workoutLogger.getCurrentDate());
				this.makeMessage("Added exercise successfully!");
			}
			else {
				
				this.makeMessage("You cannot add new exercise without filling in every field below");
			}
		}
		catch (Exception exception) {
			
			this.makeMessage(exception.getMessage());
		}
	}

	private void editExercise(int index) {

		try {
				
			if (!this.isExerciseInputEmpty()) {
					
				String formattedExercise = this.formatExercise();
				
				ArrayList<String> exercises = this.workoutLogger.getCurrentWorkoutRaw();
				
				this.workoutLogger.clearCurrentWorkout();
				
				for (int i = 0; i < exercises.size(); i++) {
					
					if (i == this.indexOfClickedExerciseButton) {
						
						this.workoutLogger.addExercise(formattedExercise);
					}
					else {
						
						this.workoutLogger.addExercise(exercises.get(i));
					}
				}
				
				this.displaySearchedWorkout(this.workoutLogger.getCurrentDate());
				this.makeMessage("Edit successful!");
				this.indexOfClickedExerciseButton = -1;
			}
			else {
				
				this.makeMessage("You cannot edit without filling in every field below");
			}
		}
		catch (Exception exception) {
			
			this.makeMessage(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private void addToInputFields(String formattedString) {
		
		String[] inputStrings = formattedString.replace(" | ", " ").split(" ");
		for (int i = 0; i < inputStrings.length; i++) {
			
			this.exerciseInputFields[i].setText(inputStrings[i]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == this.searchButton) {
			
			// get what user has written
			String date = this.dateInputField.getText();
			this.dateInputField.setText("");
			this.dateInputField.setColumns(10);
			
			Pattern format = Pattern.compile("\\s*"); 
			Matcher formatMatcher = format.matcher(date); 
			
			if (!formatMatcher.matches()) {
				
				this.displaySearchedWorkout(date);
			}
		}
		else if (event.getSource() == this.newWorkoutButton) {
			
			String date = this.dateInputField.getText();
			this.dateInputField.setText("");
			this.dateInputField.setColumns(10);
			
			Pattern format = Pattern.compile("\\s*"); 
			Matcher formatMatcher = format.matcher(date); 
			
			if (!formatMatcher.matches()) {
				
				this.addNewWorkout(date);
			}
		}
		else if (event.getSource() == this.saveExerciseButton) {
			
			if (this.isDisplayingWorkout) {
				
				if (this.indexOfClickedExerciseButton != -1) {
					
					this.editExercise(this.indexOfClickedExerciseButton);
				}
				else {
					
					this.addExercise();
				}
			}
		}
		else {
			
			for (int i = 0; i < this.exerciseButtons.size(); i++) {
				
				if (event.getSource() == this.exerciseButtons.get(i)) {
					
					this.indexOfClickedExerciseButton = i;
					this.addToInputFields(this.exerciseButtons.get(i).getText());
					this.makeMessage("You are in edit mode");
				}
			}
		}
	}
}
