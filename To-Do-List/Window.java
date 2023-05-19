/* This class is used with FileHandler to simulate a ToDo-list that stores the tasks between executions
 *
 * @qpeano [created: 2023-05-19 | last updated: 2023-05-19]
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Window extends JFrame implements ActionListener {

    /* FIELDS */

    // visual
    private JLabel toDoListLabel; // label displaying the name
	private JLabel tasksLabel; // label displaying "Tasks"
	private JTextField newTaskField; // where user inputs new task
	private JButton saveTaskButton; // saves task to file
	private JButton clearTasksButton; // clears all tasks

    private ArrayList<JButton> tasks; // the individual tasks
	private JPanel centerPanel; // houses all the tasks

    // internal
    private FileHandler fileHandler; // handles IO to and from file

    /* METHODS - constructor */

    /**
     * Constructor
     *
     * @param filePath the name (path) of the file that will store the tasks
     */
    public Window(String filePath) {

        this.setTitle("To-Do List");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.BLACK);

        this.tasks = new ArrayList<>();
        this.setUpView(filePath);
    }

    /* METHODS - internal */

    /**
     * Method instantiates all staring objects and puts them to view
     *
     * @param fileName the name (path) of the file that will store the tasks
     */
    private void setUpView(String filePath) {

        try {

            this.fileHandler = new FileHandler(filePath);
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
		this.toDoListLabel = new JLabel("<html><span style='font-size:30px' >To-Do List</span><span> @qpeano</span></html>", SwingConstants.CENTER);
		this.toDoListLabel.setForeground(Color.WHITE);

		this.tasksLabel = new JLabel("<html><span style='font-size:20px' >Tasks</span></html>", SwingConstants.CENTER);
		this.tasksLabel.setForeground(Color.WHITE);

        // instantiate the input field
		this.newTaskField = new JTextField(10);
		this.newTaskField.setBackground(Color.WHITE);
		this.newTaskField.setForeground(Color.BLACK);

        // instantiate and "decorate" the save button
		this.saveTaskButton = new JButton("save");
		this.saveTaskButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		this.saveTaskButton.setForeground(Color.WHITE);
		this.saveTaskButton.setBackground(Color.BLACK);
		this.saveTaskButton.addActionListener(this);

        // border and text will change color to green if user hovers mouse above save button
		this.saveTaskButton.addMouseListener(new MouseAdapter() {

		    public void mouseEntered(java.awt.event.MouseEvent evt) { // when user is hovering

				saveTaskButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
				saveTaskButton.setForeground(Color.GREEN);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) { // when user stops

				saveTaskButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				saveTaskButton.setForeground(Color.WHITE);
		    }
		});

        // instantiate and "decorate" the clear (all tasks) button
		this.clearTasksButton = new JButton("clear");
		this.clearTasksButton.setBackground(Color.WHITE);
		this.clearTasksButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		this.clearTasksButton.setForeground(Color.BLACK);
		this.clearTasksButton.addActionListener(this);

        // border and text will change color to red if user hovers mouse above clear button
		this.clearTasksButton.addMouseListener(new MouseAdapter() {

		    public void mouseEntered(java.awt.event.MouseEvent evt) { // when user is hovering

				clearTasksButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				clearTasksButton.setForeground(Color.RED);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) { // when user stps

				clearTasksButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				clearTasksButton.setForeground(Color.BLACK);
		    }
		});

        // instantiate the section of the window that will house the tasks
		this.centerPanel = new JPanel(new GridLayout(0, 1, 10, 2));
		this.centerPanel.setBackground(Color.BLACK);
        this.fillCenter(); // fills section with the stored tasks, if there exists any
    }

    /**
     * Method fills the centerPanel with the tasks that are stored in FileHandler
     *
     */
    private void fillCenter() {

        ArrayList<String> taskList = this.fileHandler.getAllLines();
        for (String task : taskList) {

            this.centerPanel.add(this.makeTaskButton(task));
        }
    }

    /**
     * Makes a button for a task
     *
     * @param task the string representation of the task
     */
    private JButton makeTaskButton(String task) {

        // instantiate and "decorate" the new task button
        JButton newTask = new JButton(task);
        newTask.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        newTask.setForeground(Color.WHITE);
        newTask.setBackground(Color.BLACK);
        newTask.addActionListener(this);
        this.tasks.add(newTask);

        // backgroud  will change color to white and text will change color to black if user hovers mouse above task button
        newTask.addMouseListener(new MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) { // when user is hovering

                newTask.setForeground(Color.BLACK);
                newTask.setBackground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) { // when user stops

                newTask.setForeground(Color.WHITE);
                newTask.setBackground(Color.BLACK);
            }
        });

        return newTask;
    }

    /**
     * Method adds starting components to view
     *
     */
    private void addComponentsToView() {

        // northPanel is north of the window
		JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.BLACK);
		// subNorth is the north of northPanel and houses the panel below
        JPanel subNorth = new JPanel(new BorderLayout());
		subNorth.setBackground(Color.BLACK);
		// subsubNort the north of subNorth and houses the clear and save button
        JPanel subsubNorth = new JPanel();
		subsubNorth.setBackground(Color.BLACK);

		northPanel.add(this.toDoListLabel, BorderLayout.NORTH);
		northPanel.add(this.newTaskField, BorderLayout.CENTER);

		subsubNorth.add(this.saveTaskButton);
		subsubNorth.add(this.clearTasksButton);
		subNorth.add(subsubNorth, BorderLayout.NORTH);
		subNorth.add(tasksLabel, BorderLayout.SOUTH);
		northPanel.add(subNorth, BorderLayout.SOUTH);

		Container contentArea = this.getContentPane();
		contentArea.add(northPanel, "North");
		contentArea.add(this.centerPanel, "Center");
		this.setContentPane(contentArea);
    }

    /* METHODS - event handler */

    /**
     * Method is used when user has clicked some button
     *
     */
    @Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == this.saveTaskButton) { // if user clicks save
			
			// get what user has written
			String task = this.newTaskField.getText();
			this.newTaskField.setText("");
			
			Pattern format = Pattern.compile("\\s*"); 
			Matcher formatMatcher = format.matcher(task);
			
            if (!formatMatcher.matches()) { // checks so that user has not JUST put whitespace in field
            	
            	// create button, add it to view
                JButton newTask = this.makeTaskButton(task);
                centerPanel.add(newTask);
                Container contentArea = this.getContentPane();
				this.setContentPane(contentArea);

                try { // try to add string of task to file

                    this.fileHandler.add(task);
                }
                catch (IOException ex) {


                }
            }
        }
		else if (event.getSource() == this.clearTasksButton) { // if user has clicked on clear

			this.centerPanel = new JPanel(new GridLayout(0, 1, 10, 2)); // reinstantiate center section
			this.centerPanel.setBackground(Color.BLACK);
			getContentPane().removeAll(); // remove everything
			this.addComponentsToView(); // reapply components

            try { // try to erase everything from file

                this.fileHandler.clear();
            }
            catch (IOException ex) {


            }
		}
        else { 

			for (int i = 0; i < this.tasks.size(); i++) {

				if (event.getSource() == this.tasks.get(i)) { // a specific task button has been clicked

					this.centerPanel.remove(i); // remove it from panel
					getContentPane().removeAll(); // remove everything
					this.addComponentsToView(); // reapply components

                    try { // try to remove specific task from file

                        this.fileHandler.remove(i);
                    }
                    catch (IOException ex) {


                    }
				}
			}
        }
    }
}
