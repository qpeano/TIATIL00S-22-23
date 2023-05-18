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
    private LocalDate dateOfToday; // date of the day
    private DateTimeFormatter dateFormatter; // formatts the date
    private final String correctFormat = "yyyy-MM-dd"; // the format

    /* METHODS - constructor */

    
}
