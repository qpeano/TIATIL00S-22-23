import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class Window extends JFrame implements ActionListener {

	/* FIELDS */
	
	private JLabel toDoListLabel;
	private JLabel tasksLabel;
	private JTextField newTaskField;
	private JButton saveTaskButton; 
	private JButton clearTasksButton;
	
	private ArrayList<JButton> tasks;
	JPanel center;
	
	/* METHODS - constructor */
	
	public Window() {
		
        this.setTitle("ToDo-list");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.BLACK);
        
        this.tasks = new ArrayList<>();
        this.setUpView();
	}
	
	/* METHODS - internal */
	
	private void setUpView() {
		
		this.makeComponents();
		this.addComponentsToView();
	}
	
	private void makeComponents() {
		
		this.toDoListLabel = new JLabel("<html><span style='font-size:30px' >To-Do List</span><span> @qpeano</span></html>", SwingConstants.CENTER);
		this.toDoListLabel.setForeground(Color.WHITE);
		
		this.tasksLabel = new JLabel("<html><span style='font-size:20px' >Tasks</span></html>", SwingConstants.CENTER);
		this.tasksLabel.setForeground(Color.WHITE);
		
		this.newTaskField = new JTextField(10);
		this.newTaskField.setBackground(Color.WHITE);
		this.newTaskField.setForeground(Color.BLACK);
		
		this.saveTaskButton = new JButton("save");
		this.saveTaskButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		this.saveTaskButton.setForeground(Color.WHITE);
		this.saveTaskButton.setBackground(Color.BLACK);
		this.saveTaskButton.addActionListener(this);
		
		this.saveTaskButton.addMouseListener(new MouseAdapter() {
			
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        
				saveTaskButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
				saveTaskButton.setForeground(Color.GREEN);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
				saveTaskButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				saveTaskButton.setForeground(Color.WHITE);
		    }
		});
		
		this.clearTasksButton = new JButton("clear");
		this.clearTasksButton.setBackground(Color.WHITE);
		this.clearTasksButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		this.clearTasksButton.setForeground(Color.BLACK);
		this.clearTasksButton.addActionListener(this);
		
		this.clearTasksButton.addMouseListener(new MouseAdapter() {
			
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        
				clearTasksButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				clearTasksButton.setForeground(Color.RED);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
				clearTasksButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				clearTasksButton.setForeground(Color.BLACK);
		    }
		});
		
		this.center = new JPanel(new GridLayout(0, 1, 10, 2));
		this.center.setBackground(Color.BLACK);
	}
	
	private void addComponentsToView() {
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Color.BLACK);
		JPanel subNorth = new JPanel(new BorderLayout());
		subNorth.setBackground(Color.BLACK);
		JPanel subsubNorth = new JPanel();
		subsubNorth.setBackground(Color.BLACK);
		
		northPanel.add(this.toDoListLabel, BorderLayout.NORTH);
		northPanel.add(this.newTaskField, BorderLayout.CENTER);
		
		subsubNorth.add(this.saveTaskButton);
		subsubNorth.add(this.clearTasksButton);
		subNorth.add(subsubNorth, BorderLayout.NORTH);
		subNorth.add(tasksLabel, BorderLayout.SOUTH);
		northPanel.add(subNorth, BorderLayout.SOUTH);
		
		Container cont = this.getContentPane();
		cont.add(northPanel, "North");
		cont.add(center, "Center");
		this.setContentPane(cont);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource() == this.saveTaskButton) {
			
			String task = this.newTaskField.getText();
			
			if (!task.equals("")) {
				
				JButton newTask = new JButton(task);
				newTask.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				newTask.setForeground(Color.WHITE);
				newTask.setBackground(Color.BLACK);
				newTask.addActionListener(this);
				this.tasks.add(newTask);
				
				newTask.addMouseListener(new MouseAdapter() {
					
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				        
						newTask.setForeground(Color.BLACK);
						newTask.setBackground(Color.WHITE);
				    }

				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	
						newTask.setForeground(Color.WHITE);
						newTask.setBackground(Color.BLACK);
				    }
				});
				
				center.add(newTask);
				
				Container cont = this.getContentPane();
				//cont.add(center, "Center");
				this.setContentPane(cont);
			}
		}
		else if (event.getSource() == this.clearTasksButton) {
			
			this.center = new JPanel(new GridLayout(0, 1, 10, 2));
			this.center.setBackground(Color.BLACK);
			getContentPane().removeAll();
			this.addComponentsToView();
		}
		else {
			
			for (JButton task : this.tasks) {
				
				if (event.getSource() == task) {
					
					this.center.remove(task);
					getContentPane().removeAll();
					this.addComponentsToView();
				}
			}
		}
	}	
}
