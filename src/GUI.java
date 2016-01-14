import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager.*;

/**
 * NOTHING FOR YOU TO DO HERE.
 * 
 * The class for the main window of the program.
 * 
 */
class GUI extends JFrame implements ActionListener
{
	// default value
	private static final long serialVersionUID = 1L;
	private final int mapWidth = 50;
	private final int mapHeight = 30;
	// private final int mapWidth = 25;
	// private final int mapHeight = 25;

	private MapPanel mapPanel;

	// top buttons
	private JToggleButton sourceButton;
	private JToggleButton destButton;
	private JToggleButton wallButton;
	private JToggleButton eraseButton;
	private JToggleButton resetButton;
	
	// path finding buttons
	private JButton dijkstraButton;
	private JButton aStarButton;
	private JButton dfsButton;
	private JButton bfsButton;
	private JButton depthLtdButton;
	private JButton greedyBestButton;
	private JButton uniformCostButton;
	private JButton idaButton;
	private JButton bidirectionalButton;
	private JButton iterativeDeepeningButton;

	private JLabel msgLabel;

	public GUI() {
		super("Interactive Path Finding with Pac-Man");
		setLayout(new BorderLayout());
		setResizable(false);

		mapPanel = new MapPanel(this, mapWidth, mapHeight);
		JToolBar toolBar = setUpToolBar();

		// Message panel
		JPanel msgPanel = new JPanel();
		msgPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		msgPanel.setForeground(Color.black);
		msgPanel.setLayout(new GridLayout(1, 4));
		msgLabel = new JLabel("Ready to play! Create a maze and choose an algorithm.");
		// msgLabel.setFont(new Font("Courier New", Font.PLAIN, 14));

		msgPanel.add(msgLabel);

		// Add to window
		add(mapPanel, BorderLayout.EAST);
		add(toolBar, BorderLayout.WEST);
		add(msgPanel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setMsg(String msg) {
		String str = msg;
		msgLabel.setText(str);
	}

	// Handle events from toolbar.
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed");

		// Run dfs is the placeholder for the algorithms
		Object s = e.getSource();
		if (s == sourceButton) {
			mapPanel.setOp(Operation.SET_SOURCE);
		} else if (s == destButton) {
			mapPanel.setOp(Operation.SET_DEST);
		} else if (s == wallButton) {
			mapPanel.setOp(Operation.SET_WALL);
		} else if (s == eraseButton) {
			mapPanel.setOp(Operation.CLEAR_WALL);
		} else if (s == resetButton) {
			mapPanel.reset();
		} else if (s == dijkstraButton) {
			mapPanel.runDijkstra();
		} else if (s == aStarButton) {
			mapPanel.runAStar();
		} else if (s == dfsButton) {
			mapPanel.runAlgorithm("dfs");
		} else if (s == bfsButton) {
			mapPanel.runAlgorithm("bfs");
		} else if (s == greedyBestButton) {
			mapPanel.runAlgorithm("greedy");
		} else if (s == depthLtdButton) {
			mapPanel.runAlgorithm("depthLtd");
		} else if (s == uniformCostButton) {
			mapPanel.runAlgorithm("uniformCost");
		} else if (s == idaButton) {
			mapPanel.runAlgorithm("ida");
		} else if (s == bidirectionalButton) {
			mapPanel.runAlgorithm("bidirectional");
		} else if (s == iterativeDeepeningButton) {
			mapPanel.runAlgorithm("iterativeDeepening");
		} else {
			System.err.println(e);
		}
	}

	private JToolBar setUpToolBar() {
		// Set up background
		JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		// Color lightBlue = new Color(184, 207, 229);
		toolBar.setBackground(Color.white);

		// Add title
		MatteBorder matteBorder = new MatteBorder(2, 2, 2, 2, Color.black);
		toolBar.setBorder(BorderFactory.createTitledBorder(matteBorder, 
                "Menu Options"));
		MatteBorder thinMatteBorder = new MatteBorder(1, 1, 1, 1, Color.black);

		JPanel mazeToolsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.ipadx = 0;
		c.ipady = 0;
		// c.gridwidth = 1;
		// c.gridheight = 1;
		c.weighty = 0.01;   //request any extra vertical space

		ButtonGroup tools = new ButtonGroup();

		mazeToolsPanel.setBorder(BorderFactory.createTitledBorder(thinMatteBorder, 
                "GAME SET-UP"));

		// SOURCE
		ImageIcon startIcon = createImageIcon("images/pacman.png", "pacmanIcon");
		sourceButton = new JToggleButton("SOURCE", startIcon, false);
		sourceButton.addActionListener(this);
		sourceButton.setBackground(Color.white);
		tools.add(sourceButton);
		c.gridy = 0;
		mazeToolsPanel.add(sourceButton, c);

		// CHERRY
		ImageIcon cherryIcon = createImageIcon("images/cherryIcon.png", "pizzaIcon");
		destButton = new JToggleButton("GOAL", cherryIcon);
		destButton.addActionListener(this);
		destButton.setBackground(Color.white);
		tools.add(destButton);
		c.gridy = 1;
		mazeToolsPanel.add(destButton, c);
		
		// WALL
		ImageIcon wallIcon = createImageIcon("images/wall.png", "wallIcon");
		wallButton = new JToggleButton("WALL", wallIcon);
		wallButton.addActionListener(this);
		wallButton.setBackground(Color.white);
		tools.add(wallButton);
		c.gridy = 2;
		mazeToolsPanel.add(wallButton, c);

		// ERASER
		ImageIcon eraserIcon = createImageIcon("images/eraser.png", "eraserIcon");
		eraseButton = new JToggleButton("REMOVE", eraserIcon);
		eraseButton.addActionListener(this);
		eraseButton.setBackground(Color.white);
		tools.add(eraseButton);
		c.gridy = 3;
		mazeToolsPanel.add(eraseButton, c);
		
		// RESET
		ImageIcon resetIcon = createImageIcon("images/reset.png", "resetIcon");
		resetButton = new JToggleButton("RESET MAZE", resetIcon);
		resetButton.addActionListener(this);
		resetButton.setBackground(Color.white);
		tools.add(resetButton);
		c.gridy = 4;
		mazeToolsPanel.add(resetButton, c);
		
		JPanel uninformedSearchPanel = new JPanel(new GridBagLayout());
		uninformedSearchPanel.setBorder(BorderFactory.createTitledBorder(thinMatteBorder, 
                "UNINFORMED"));
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0;
		c2.ipadx = 0;
		c2.ipady = 0;
		c2.weighty = 0.01;   //request any extra vertical space

		bidirectionalButton = new JButton("BIDIRECTIONAL");
		bidirectionalButton.addActionListener(this);
		bidirectionalButton.setBackground(Color.white);

		dijkstraButton = new JButton("DIJKSTRA");
		dijkstraButton.addActionListener(this);
		dijkstraButton.setBackground(Color.white);

		aStarButton = new JButton("A-STAR");
		aStarButton.addActionListener(this);
		aStarButton.setBackground(Color.white);

		dfsButton = new JButton("DFS");
		dfsButton.addActionListener(this);
		dfsButton.setBackground(Color.white);

		bfsButton = new JButton("BFS");
		bfsButton.addActionListener(this);
		bfsButton.setBackground(Color.white);

		depthLtdButton = new JButton("DEPTH LTD");
		depthLtdButton.addActionListener(this);
		depthLtdButton.setBackground(Color.white);

		greedyBestButton = new JButton("GREEDY BEST");
		greedyBestButton.addActionListener(this);
		greedyBestButton.setBackground(Color.white);

		uniformCostButton = new JButton("UNIFORM COST");
		uniformCostButton.addActionListener(this);
		uniformCostButton.setBackground(Color.white);

		idaButton = new JButton("IDA*");
		idaButton.addActionListener(this);
		idaButton.setBackground(Color.white);

		iterativeDeepeningButton = new JButton("ID DFS");
		iterativeDeepeningButton.addActionListener(this);
		iterativeDeepeningButton.setBackground(Color.white);

		// UNINFORMED SEARCH PANEL
		c2.gridy = 0;
		uninformedSearchPanel.add(dfsButton, c2);
		c2.gridy = 1;
		uninformedSearchPanel.add(bfsButton, c2);
		c2.gridy = 2;
		uninformedSearchPanel.add(depthLtdButton, c2);
		c2.gridy = 3;
		uninformedSearchPanel.add(uniformCostButton, c2);
		c2.gridy = 4;
		uninformedSearchPanel.add(bidirectionalButton, c2);
		c2.gridy = 5;
		uninformedSearchPanel.add(dijkstraButton, c2);
		c2.gridy = 6;
		uninformedSearchPanel.add(iterativeDeepeningButton, c2);
		
		JPanel informedSearchPanel = new JPanel(new GridBagLayout());
		informedSearchPanel.setBorder(BorderFactory.createTitledBorder(thinMatteBorder, "INFORMED"));

		GridBagConstraints c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.gridx = 0;
		c3.ipadx = 0;
		c3.ipady = 0;
		c3.weighty = 0.01;   //request any extra vertical space

		// informed search panel
		c3.gridy = 0;
		informedSearchPanel.add(greedyBestButton, c3);
		c3.gridy = 1;
		informedSearchPanel.add(aStarButton, c3);
		c3.gridy = 2;
		// informedSearchPanel.add(idaButton, c3);
		
		toolBar.setPreferredSize(new Dimension(175, 600));
		mazeToolsPanel.setPreferredSize(new Dimension(150, 200));
		uninformedSearchPanel.setPreferredSize(new Dimension(150, 280));
		informedSearchPanel.setPreferredSize(new Dimension(150, 120));
		
		mazeToolsPanel.setBackground(Color.white);
		informedSearchPanel.setBackground(Color.white);
		uninformedSearchPanel.setBackground(Color.white);

		// add the panels onto the toolbar
        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;
        // gbc.gridheight = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridy = 0;
        toolBar.add(mazeToolsPanel, gbc);
        gbc.gridy = 1;
        toolBar.add(new JLabel(" "));

		toolBar.add(uninformedSearchPanel, gbc);

        toolBar.add(new JLabel(" "));
		toolBar.add(informedSearchPanel, gbc);

        gbc.gridy = 2;
		toolBar.add(informedSearchPanel, gbc);

        toolBar.add(new JLabel(" "));
        toolBar.add(new JLabel(" "));
        toolBar.add(new JLabel(" "));
        toolBar.add(new JLabel(" "));
		return toolBar;
	}

	// from docs.oracle.com
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Metal".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            setUIFont (new javax.swing.plaf.FontUIResource("Courier New",Font.BOLD,13));
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If metal is not available, use the default (system).
		}

		GUI mainWindow = new GUI();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// http://stackoverflow.com/questions/7434845/setting-the-default-font-of-swing-program
	// to change font
	@SuppressWarnings("rawtypes")
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	    } 


}
