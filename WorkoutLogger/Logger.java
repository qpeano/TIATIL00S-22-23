import java.util.ArrayList;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Logger {

    /* FIELDS */

    private DataCollection workouts;
    private String primDate;

    /* METHODS - constructor */

    public Logger(String fileName) throws IOException {

        this.workouts = new DataCollection(fileName);
    }

    /* METHODS - internal */

    /**
     * Method is used to check formatting on a date
     *
     * @param date the date
     */
    private void checkDateFormat(String date) throws Exception {

        Pattern dateFormat = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d"); // format of date
        Matcher matchFormat = dateFormat.matcher(date);

        if (matchFormat.matches()) {

            int year = Integer.parseInt(date.substring(0, 4)); // gets year
            int month = Integer.parseInt(date.substring(5, 7)); // gets month
            int day = Integer.parseInt(date.substring(8)); // gets day

            int daysInMonth = this.getDaysInMonth(year, month);
            if (day < 0 || day > daysInMonth) {

                throw new Exception("SOMETHING IS NOT RIGHT WITH THE NUMBER OF DAYS, THE MONTH, OR THE YEAR");
            }
        }
        else {

            throw new Exception("DATE SHOULD BE FORMATTED yyyy-MM-dd");
        }
    }

    /**
     * Method is used to get the number of days in a month in  a particular year
     *
     * @param year the year
     * @param month the month
     * @return maximum number of days in a month
     */
    private int getDaysInMonth(int year, int month) {

        Calendar mycal = new GregorianCalendar(year, month, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return daysInMonth;
    }

    /**
     * Method is used to check if exercise is formatted correctly
     *
     * @param exerciseInfo
     */
    private void checkExerciseFormat(String exerciseInfo) throws Exception {

        Pattern format = Pattern.compile("[a-zA-Z]+_\\d+_\\d+_\\d+[\\.\\d+]*(kg|sec|min)${1}");
        Matcher matchFormat = format.matcher(exerciseInfo);

        if (!matchFormat.matches()) {

            throw new Exception("EXERCISE INPUT IS NOT FORMATTED CORRECTLY");
        }
    }

    /* METHODS - UI */

    /**
     * Method is used to add a new workout
     *
     * @param date the date of today
     */
    public void addWorkout(String date) throws IOException, Exception {

        this.checkDateFormat(date);
        this.workouts.add(date);
        this.primDate = date;
    }

    /**
     * Method is used to add exercises in an existing workout
     *
     * @param exerciseInfo the information about the exercises (name, sets, reps, intensity)
     */
    public void addExercises(String exerciseInfo) throws IOException, Exception {

        this.checkExerciseFormat(exerciseInfo);
        this.workouts.addTo(this.primDate, exerciseInfo);
    }

    /**
     * Method is used to get a workout using the date
     *
     * @param date the date the workout was logged
     * @return the exercises from a workout
     */
    public ArrayList<String> getWorkout(String date) throws Exception {

        this.checkDateFormat(date);
        ArrayList<String> formattedExercises = this.getFormattedExercises(this.workouts.get(date));
        return formattedExercises;
    }
    
    
    /**
     * Method is used to return formatted exercises
     * 
     * @param exerises the exercises in raw format
     * @return exercises but formatted
     */
    private ArrayList<String> getFormattedExercises(ArrayList<String> exercises) {
			
    	ArrayList<String> formattedExercises = new ArrayList<>();
    	
    	for (String exercise : exercises) {
    		
    		String formattedExercise = exercise.replaceAll("_", " | ");
    		formattedExercises.add(formattedExercise);
    	}
    	
		return formattedExercises;
	}

	/**
     * Method is used to clear exercises of a workout
     *
     * @param date the date the workout was logged
     * @throws IOException if something goes wrong while writing to file
     * @throws Exception if formatting is incorrect
     */
    public void clearWorkout(String date) throws IOException, Exception {

        this.checkDateFormat(date);
        this.workouts.clearDataUnit(date);
    }
    
    /**
     * Method is used to check if a workout exists or not
     * 
     * @param date
     * @return true if workout with date exists, otherwise false
     * @throws Exception if formatting is incorrect
     */
    public boolean hasWorkoutDate(String date) throws Exception {
    	
    	this.checkDateFormat(date);
    	return this.workouts.contains(date);
    }
}
