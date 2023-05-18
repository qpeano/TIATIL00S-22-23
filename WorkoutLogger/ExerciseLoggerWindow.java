import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExerciseLoggerWindow extends JFrame implements ActionListener {

    /* FIELDS */

	private JTextField dateInputField; // user will input a date here
	private JButton searchWorkoutButton; // user will search for workout by pressing this
	private JButton makeNewWorkoutButton; // user will make new workout with today's date using this

	private JTextField[] exerciseInfoInputFields; // user will input name, sets, reps, and intensity of an exercise here
	private JButton saveExerciseButton; // user will save exercise to log using this

    private ArrayList<JButton> exercises; // the buttons with the exerise information

    private JButton compareWorkoutsButton; // user will press button to compare workouts

	// internal

	private ExerciseLogger logger; // logs the exercise info for later use
    // private LocalDate dateOfToday; // date of the day
    // private DateTimeFormatter dateFormatter; // formatts the date
    // private final String correctFormat = "yyyy-MM-dd"; // the format

    /* METHODS - constructor */

    /**
     * Constructor
     *
     * @param fileName the name of the file that houses all the workouts
     */
    public ExerciseLoggerWindow(String fileName) {

        super("Exercise Logger");
        this.setSize(500, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setUpApp(fileName);
    }

    /* METHODS - internal */

    /**
     * Methods initializes objects and puts them in view
     *
     */
    public void setUpApp(String fileName) {

        this.makeComponents();
        this.addComponentsToView();

        try {

            this.logger = new ExerciseLogger(fileName);
        }
        catch (IOException exception) {

            JOptionPane.showMessageDialog(ExerciseLoggerWindow.this,
            "A PROBLEM HAS OCCURED WITH DATACOLLECTION:\n" + exception.getMessage() + "\nPLEASE CHECK FILE OR TRY RESTARTING THE APP",
            "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method initializes all starting objects
     *
     */
    private void makeComponents() {

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter correctFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = currentDate.format(correctFormat);

		this.dateInputField = new JTextField(date);
		this.searchWorkoutButton = new JButton("search");
		this.makeNewWorkoutButton = new JButton("new");

		String[] starterText = {"exercise (back-squat)", "sets (2)", "reps (8)", "intensity (79kg)"};
		this.exerciseInfoInputFields = new JTextField[starterText.length];

		for (int i = 0; i < starterText.length; i++) {

			this.exerciseInfoInputFields[i] = new JTextField(starterText[i]);
			this.exerciseInfoInputFields[i].setVisible(false);
		}

		this.saveExerciseButton = new JButton("save");
		this.saveExerciseButton.setVisible(false);

		this.exercises = new ArrayList<>();
		this.compareWorkoutsButton = new JButton("compare");
		this.compareWorkoutsButton.setVisible(false);
    }

    /**
     * Method adds all created components to view
     *
     */
    public void addComponentsToView() {

        Container contentArea = this.getContentPane();

        JPanel northPanel = this.generateNorth();
        JPanel centerPanel = this.generateCenter(new JPanel());

        contentArea.add("North", northPanel);
        contentArea.add("Center", centerPanel);
        this.setContentPane(contentArea);
    }

    /**
	 * Method makes the part of the screen (view) were the
	 * user clicks to create or search for a workout
	 *
	 * @return the panel with all objects
     */
	private JPanel generateNorth() {

        JPanel panel = new JPanel();

		panel.add(this.dateInputField);
		panel.add(this.searchWorkoutButton);
		panel.add(this.makeNewWorkoutButton);

		return panel;
	}

	/*
	 * Method makes the part of view where the exercises are viewed
	 *
     * @param panel a JPanel
	 * @return the panel with all objects
	 */
    private JPanel generateCenter(JPanel panel) {

        JPanel subPanel = new JPanel();
		for (JTextField field: this.exerciseInfoInputFields) {

			subPanel.add(field);
		}

		subPanel.add(saveExerciseButton);
		panel.add(subPanel, BorderLayout.NORTH);

		return panel;
    }

    /* METHODS - event handler */

    @Override
    public void actionPerformed(ActionEvent event) {


    }
}
