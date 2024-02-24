package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.DB;
import domain.logic.Container;

/**
 * The main GUI frame for the application, serving as the entry point for user
 * interaction. It includes functionality to add and edit container names, and
 * navigate between different views.
 */
public class HomeView implements ActionListener {

	/**
	 * Frame window of the application
	 */
	static JFrame frame = new JFrame("Perfect Pantry");

	public static DB data = new DB();

	/**
	 * home panel of the application, will hold all components pertaining to home
	 * screen
	 */
	private JPanel homePanel = new JPanel();

	/**
	 * Title part of home screen
	 */
	private JPanel titlePanel = new JPanel();

	/**
	 * Title name
	 */
	private JLabel titleLabel = new JLabel("Perfect Pantry");;

	/**
	 * Button that will add new container, apart of homeButtonsPanel
	 */
	private JButton createContainer = new JButton("Create");;

	/**
	 * Hold buttons pertaining to its containers
	 */
	private JPanel containerButtonsPanel = new JPanel();

	/**
	 * Map of button and its corresponding Container object
	 */
	private ConcurrentHashMap<JButton, Container> containerMap;

	/**
	 * Stages of the home screen; 0 = home 1 = edit screen 2= view conntainers
	 */
	private int stage;

	/**
	 * main components of edit view
	 */
	private JPanel editPanel = new JPanel();

	/**
	 * Hold components of editView
	 */
	private JPanel editNameOfContainerPanel = new JPanel();

	/**
	 * Title panel of edit view
	 */
	private JPanel editNameOfContainerTitlePanel = new JPanel();

	/**
	 * Text of edit view
	 */
	private JLabel editNameLabel = new JLabel("Click the Container Button you wish to rename");
	;

	/**
	 * Holds buttons of edit view
	 */
	private JPanel editGUIButtonsPanel = new JPanel();

	/**
	 * Button to go back from edit screen to home screen
	 */
	private JButton editBackToHome = new JButton("Back to Home");
	;

	/**
	 * Panel to hold container buttons
	 */
	private JPanel editContainerButtonsPanel = new JPanel();

	/**
	 * Add text for name of container
	 */
	private JTextField newContainerText = new JTextField(40);;

	/**
	 * Button to go to container list view
	 */
	private JButton viewContainers = new JButton("View Containers");

	/**
	 * Holds components for the container list view
	 */
	private JPanel viewOfContainerPanel = new JPanel();

	/**
	 * Holds button on container list view
	 */
	private JPanel backPanel = new JPanel();

	/**
	 * Button to go from container view to home
	 */
	private JButton viewOfContainer2HomeButton = new JButton("Back");

	/**
	 * Button to change name of container
	 */
	private JButton editContainerNameButton = new JButton("Edit Name of Container");
	;

	public static void main(String[] args) {
		HomeView m = new HomeView();
	}

	/**
	 * Launches the application and initializes the main GUI components.
	 */
	public HomeView() {

		// Initialize frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
		frame.setVisible(true);
		frame.setResizable(false); // stop resize
		frame.setMinimumSize(new Dimension(800, 600));
		frame.getContentPane().setBackground(new Color(245, 223, 162));

		// Initilize containerMap
		containerMap = new ConcurrentHashMap<>();

		//Initialise all actionlisteners here
		newContainerText.addActionListener(this);
		createContainer.addActionListener(this);
		viewContainers.addActionListener(this);
		viewOfContainer2HomeButton.addActionListener(this);
		editContainerNameButton.addActionListener(this);
		editBackToHome.addActionListener(this);

		stage = 0; //home stage

		changeStageOfHome();
	}



	/**
	 * Changes the stage of the home screen between the main view and the Container list view.
	 */
	public void changeStageOfHome() {
		if (stage == 0) { // Home screen
			if (editPanel != null) {
				editPanel.setVisible(false);
			}
			if( viewOfContainerPanel != null) {
				viewOfContainerPanel.setVisible(false);
			}
			// Initialse mainMenuPanel
			homePanel.setLayout(null);
			frame.getContentPane().add(homePanel);
			frame.setLocationRelativeTo(null);

			homePanel.setBackground(new Color(253, 241, 203));
			homePanel.setVisible(true);

			// Initialize titleLabel
			homePanel.add(titleLabel);
			titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 50));
			titleLabel.setBounds(240, 150, 500, 100);

			homePanel.add(newContainerText);
			newContainerText.setText("Pantry" + (containerMap.size() + 1));
			newContainerText.setForeground(Color.GRAY);
			newContainerText.setVisible(true);
			newContainerText.setBounds(240, 250, 250, 40);

			homePanel.add(createContainer);
			createContainer.setBackground(new Color (76, 183, 242));
			createContainer.setBounds(500, 250, 80, 40);

			homePanel.add(viewContainers);
			viewContainers.setBackground(new Color (76, 183, 242));
			viewContainers.setBounds(240, 300, 250, 40);
			homePanel.setVisible(true);


		} else if (stage == 1) { // Edit name of container screen
			homePanel.setVisible(false);
			viewOfContainerPanel.setVisible(false);

			editPanel.setLayout(null);
			frame.add(editPanel);

			editNameOfContainerPanel.setBounds(0, 0, 800, 80);
			editNameOfContainerPanel.setBackground(new Color(203, 253, 232));
			editPanel.add(editNameOfContainerPanel);

			editNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
			editNameOfContainerPanel.add(editNameLabel);

			editGUIButtonsPanel.setBackground(new Color(203, 253, 232));
			editGUIButtonsPanel.setBounds(0, frame.getHeight()-90, 800, 90);
			editPanel.add(editGUIButtonsPanel);

			editGUIButtonsPanel.add(editBackToHome);

			editContainerButtonsPanel.setBounds(0, 80, 800, 500);
			editContainerButtonsPanel.setBackground(new Color(203, 253, 232));
			editContainerButtonsPanel.setLayout(new FlowLayout());
			addContainerButtons(editContainerButtonsPanel);

			editPanel.add(editContainerButtonsPanel);
			editPanel.setVisible(true);
		}

		else if (stage == 2) { // See list of containers as buttons
			homePanel.setVisible(false);

			frame.add(viewOfContainerPanel);
			viewOfContainerPanel.setLayout(null);
			viewOfContainerPanel.setBackground(new Color(253, 241, 203));

			backPanel.setBackground(new Color(253, 241, 203));
			viewOfContainerPanel.add(backPanel);
			backPanel.setLayout(new FlowLayout());
			backPanel.setBounds(0, 0, 800, 50);

			backPanel.add(viewOfContainer2HomeButton);
			backPanel.add(editContainerNameButton);

			viewOfContainerPanel.add(containerButtonsPanel);
			containerButtonsPanel.setBounds(0, 50, 800, 500);
			containerButtonsPanel.setBackground(new Color(253, 241, 200));
			addContainerButtons(containerButtonsPanel);



			viewOfContainerPanel.setVisible(true);


		}
	}

	/**
	 * Adds a new container and its corresponding button to the GUI.
	 */
	private void addNewContainer() {
		String nameOfContainer = newContainerText.getText();
		int opt = JOptionPane.showConfirmDialog(frame, "Create Container \"" + nameOfContainer + "\"?" );
		if (nameOfContainer != null && !nameOfContainer.trim().isEmpty() && opt == JOptionPane.YES_OPTION) { // if not cancelled nor empty
			Container c = new Container(nameOfContainer, this);
			JButton b = new JButton(c.getName());
			b.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
			containerMap.put(b, c);
			newContainerText.setText("Pantry" + (containerMap.size() + 1));
			data.addContainer(nameOfContainer, c);

		}

	}

	/**
	 * Dynamically adds container buttons to the specified panel based on the
	 * current containerMap state.
	 * 
	 * @param p The panel to which container buttons will be added.
	 */
	private void addContainerButtons(JPanel p) {
		p.removeAll();
		containerMap.forEach((button, container) -> {
			p.add(button);
			button.addActionListener(this);
		});
		p.revalidate(); // refresh panel
	}

	/**
	 * Initiates the container renaming process for a given container button.
	 * 
	 * @param b The button corresponding to the container to be renamed.
	 */
	private void renameContainerButton(JButton b) {
		Container c = containerMap.get(b);
		String nameOfContainer = JOptionPane
				.showInputDialog("What would you like to rename container " + c.getName() + " to?");
		if (nameOfContainer != null && !nameOfContainer.trim().isEmpty()) {
			c.setName(nameOfContainer);
			b.setText(c.getName()); // Update the button text directly instead of replacing the button in the map
			stage = 0;
			changeStageOfHome();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (stage == 0) { // home screen
			if(source == createContainer) {
				addNewContainer();
			}
			else if (source == viewContainers) {
				stage = 2;
				changeStageOfHome();
			}


		} else if (stage == 1) { //edit name screen
			if (source == editBackToHome) {
				stage = 0;
				changeStageOfHome();
			} else {
				Container c = containerMap.get(source); // This will return null if the button is not found
				if (c != null) {
					renameContainerButton((JButton) source);
				}
			}
		}

		else if (stage == 2) { //view containers screen
			if (source == viewOfContainer2HomeButton) {
				stage = 0;
				changeStageOfHome();
			}
			else if (source == editContainerNameButton) {
				stage = 1;
				changeStageOfHome();
			}
			else {
				Container c = containerMap.get(source); // This will return null if the button is not found
				if (c != null) {
					c.getGUI();
				}

			}
		}
	}

	/**
	 * Provides access to the main application frame.
	 * 
	 * @return The main JFrame of the application.
	 */
	public static JFrame getFrame() {
		return frame;
	}
}