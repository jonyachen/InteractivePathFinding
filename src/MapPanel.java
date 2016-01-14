
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
// import java.util.Date;
// import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * NOTHING FOR YOU TO DO HERE.
 * 
 * This class represents the map panel.
 * 
 */
public class MapPanel extends JPanel implements MouseListener, MouseMotionListener
{
	// Default value
	private static final long serialVersionUID = 1L;
	// private final Color backColor = new Color(208, 208, 208);
	private final Color backColor = Color.black;

	private final Color sepColor = new Color(64, 64, 64);
	// private final Color sepColor = Color.white;

	// private final Color wallColor = new Color(64, 64, 64);
	private final Color wallColor = new Color(32, 29, 209);
	private final Color sourceColor = new Color(0, 128, 0);
	private final Color destColor = new Color(160, 0, 0);
	
	
	private final Color visitedColor = new Color(40, 40, 40);
	
	
	// private final Color visitedColor = new Color(153, 51, 255);

	private final Color pathColor = new Color(192, 192, 64);

	// cell size
	private final int cellSize = 22;

	private final int sepSize = 1;

	/** The width of the panel (in pixels). */
	private int width;
	/** The height of the panel (in pixels). */
	private int height;
	/** The map. */
	private Map map;

	private GUI window;
	private Operation op;

	// Source
	private boolean sourceSet;
	private int iSource;
	private int jSource;

	// Destination
	private boolean destSet;
	private int iDest;
	private int jDest;

	// Mouse position
	private Point pMouse;
	private Point pMousePrev;
	// Position of mouse in grid
	private int iMouse;
	private int jMouse;

	private Map visitedMap;
	private Map pathMap;

	Graphics2D g2d;

	private boolean pathLitUp = false;
	static int pacmanDirection = 0;

	// Animation
	private Timer timer;
	private boolean animation;
	private final int animDelay = 5; // milliseconds
	private ArrayList<Integer> animVisited;
	private int indexVisited;
	private ArrayList<Integer> animPath;
	private int indexPath;

	private String pacmanImage = "pacman22x22.png";

	private boolean destinationReached = false;
	private boolean algorithmComplete = false;

	public MapPanel(GUI window, int mapWidth, int mapHeight) {
		this.window = window;

		map = new Map(mapWidth, mapHeight);
		visitedMap = new Map(mapWidth, mapHeight);
		pathMap = new Map(mapWidth, mapHeight);

		width = mapWidth * cellSize + (mapWidth + 1) * sepSize;
		height = mapHeight * cellSize + (mapHeight + 1) * sepSize;

		sourceSet = true;
		iSource = mapHeight - 1;
		jSource = 0;

		destSet = true;
		iDest = 0;
		jDest = mapWidth - 1;

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void reset() {
		// remove the animated Pacman
		pathLitUp = false;
		System.out.println("MapPanel: reset");
		sourceSet = false;
		destSet = false;
		map.reset();
		visitedMap.reset();
		pathMap.reset();
		animation = false;
		repaint();
	}

	public void setOp(Operation newOp) {
		op = newOp;
	}

	public void startAnimation(ArrayList<Integer> visited, ArrayList<Integer> path) {
		animVisited = visited;
		indexVisited = 0;
		animPath = path;
		indexPath = 0;
		animation = true;

		ActionListener taskPerformer = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt) {
				// System.out.println("tick");

				if (!animation) {
					return;
				}

				if (indexVisited < animVisited.size()) {
					int vtx = animVisited.get(indexVisited);
					indexVisited++;
					int mapWidth = map.getWidth();
					int i = vtx / mapWidth;
					int j = vtx % mapWidth;
					visitedMap.setObstacle(i, j, true);
					repaint();
					return;
				}

				timer.setDelay(2 * animDelay); // display path slower
				if (indexPath < animPath.size()) {
					int vtx = animPath.get(indexPath);
					indexPath++;
					int mapWidth = map.getWidth();
					int i = vtx / mapWidth;
					int j = vtx % mapWidth;
					pathMap.setObstacle(i, j, true);
					if ((iDest == i) && (jDest == j)) {
						System.out.println("Destination reached");
						destinationReached = true;
						// we can now do the animation
					}
					repaint();
					return;
				}
				timer.stop();

				// we've exhausted the algorithm
				algorithmComplete = true;
				
				if (destinationReached == true) {
					// reset for later
					
					
					// remove pacman and cherry
					sourceSet = false;
					destSet = false;

					// play animation on cherry
					int jPosition = jDest * cellSize + (jDest + 1) * sepSize;
					int iPosition = iDest * cellSize + (iDest + 1) * sepSize;

					Image complete;
					try {
						complete = ImageIO.read(new File("pacmancomplete.png"));
						g2d.drawImage(complete, jPosition, iPosition, null);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						// System.out.println("File not found");
					}

					final Timer animationTimer = new Timer(100, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("Running the eating animation...");
							pacmanDirection += 1;
							if (pacmanDirection % 2 == 0) {
								pacmanImage = "pacman_complete.png";
							} else {
								pacmanImage = "pacman22x22.png";
							}
							repaint();

							// end the algorithm
							if (pacmanDirection > 15) {
								((Timer)e.getSource()).stop();
								pacmanImage = "pacman22x22.png";
								pacmanDirection = 0;
								destinationReached = false;
							}
							
							
							repaint();
							
						}
					});
					animationTimer.restart();

				}
		
			}
			
		};
		
		timer = new Timer(animDelay, taskPerformer);
		timer.start();
	}

	public void runDijkstra() {
		if (algorithmComplete) {
			sourceSet = true;
			destSet = true;
			algorithmComplete = false;
		}
		window.setMsg("Run Dijkstra.");

		if (!sourceSet) {
			window.setMsg("Source vertex not set.");
			return;
		}
		if (!destSet) {
			window.setMsg("Destination vertex not set.");
			return;
		}

		// System.out.println(map);
		visitedMap.reset();
		pathMap.reset();

		DigraphW graph = map.getGraph();
		// System.out.println(graph);
		int source = map.getIndex(iSource, jSource);
		int dest = map.getIndex(iDest, jDest);

		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		long t0 = -System.currentTimeMillis();
		double distance = graph.shortestPath(source, dest, visited, path);
		t0 += System.currentTimeMillis();
		window.setMsg("distance = " + distance + ", time=" + t0 + "ms");

		startAnimation(visited, path);
	}

	// Run A*
	public void runAStar() {
		if (algorithmComplete) {
			sourceSet = true;
			destSet = true;
			algorithmComplete = false;
		}

		window.setMsg("Run A*.");
		if (!sourceSet) {
			window.setMsg("Source vertex not set.");
			return;
		}
		if (!destSet) {
			window.setMsg("Destination vertex not set.");
			return;
		}

		// System.out.println(map);
		visitedMap.reset();
		pathMap.reset();

		DigraphW graph = map.getGraph();
		// System.out.println(graph);
		int source = map.getIndex(iSource, jSource);
		int dest = map.getIndex(iDest, jDest);

		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		long t0 = -System.currentTimeMillis();
		double distance = graph.shortestPathHeur(source, dest, visited, path);
		t0 += System.currentTimeMillis();
		window.setMsg("distance = " + distance + ", time=" + t0 + "ms");

		startAnimation(visited, path);
	}


	// run DFS
	public void runAlgorithm(String algorithm) {
		if (algorithmComplete) {
			sourceSet = true;
			destSet = true;
			algorithmComplete = false;
		}

		if (algorithm == "dfs") {
			window.setMsg("Run DFS algorithm");
		} else if (algorithm == "bfs") {
			window.setMsg("Run BFS algorithm");
		} else if (algorithm == "greedy") {
			window.setMsg("Run greedy best algorithm");
		} else if (algorithm == "depthLtd") {
			window.setMsg("Run depth limited algorithm");
		} else if (algorithm == "uniformCost") {
			window.setMsg("Run uniform cost algorithm");
		} else if (algorithm == "ida") {
			window.setMsg("Run IDA algorithm");
		} else if (algorithm == "bidirectional") {
			window.setMsg("Run bidirectional algorithm using BFS");
		} else if (algorithm == "smoothA") {
			window.setMsg("Run smooth A*");
		} else {
			window.setMsg("Sorry, I don't recognize your algorithm");
		}

		if (!sourceSet) {
			window.setMsg("Source vertex not set.");
			return;
		}
		if (!destSet) {
			window.setMsg("Destination vertex not set.");
			return;
		}

		// System.out.println(map);
		visitedMap.reset();
		pathMap.reset();

		DigraphW graph = map.getGraph();
		// System.out.println(graph);
		int source = map.getIndex(iSource, jSource);
		int dest = map.getIndex(iDest, jDest);

		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		long t0 = -System.currentTimeMillis();

		double distance = 0;
		if (algorithm == "dfs") {
			distance = graph.DFS(source, dest, visited, path);
		} else if (algorithm == "bfs") {
			distance = graph.BFS(source, dest, visited, path);
		} else if (algorithm == "greedy") {
			distance = graph.greedyBestFirst(source, dest, visited, path);
		} else if (algorithm == "depthLtd") {
			// fixedDepth
			int fixedDepth = 300;
			distance = graph.depthLimitedDFS(source, dest, visited, path, fixedDepth);
		} else if (algorithm == "uniformCost") {

			distance = graph.uniformCost(source, dest, visited, path);
		} else if (algorithm == "ida") {

			distance = graph.iterativeDeepeningAStar(source, dest, visited, path);
		} else if (algorithm == "bidirectional") {
			distance = graph.bidirectional(source, dest, visited, path);

		} else if (algorithm == "iterativeDeepening") {
			distance = graph.IterativeDeepening(source, dest, visited, path);
		} else {
			System.out.println("Not a recognized algorithm");
			distance = graph.DFS(source, dest, visited, path);
		}

		t0 += System.currentTimeMillis();
		window.setMsg("distance = " + distance + ", time=" + t0 + "ms");

		startAnimation(visited, path);
	}

	// run BFS
	public void runBFS() {
		window.setMsg("Run BFS");
		if (!sourceSet) {
			window.setMsg("Source vertex not set.");
			return;
		}
		if (!destSet) {
			window.setMsg("Destination vertex not set.");
			return;
		}

		// System.out.println(map);
		visitedMap.reset();
		pathMap.reset();

		DigraphW graph = map.getGraph();
		// System.out.println(graph);
		int source = map.getIndex(iSource, jSource);
		int dest = map.getIndex(iDest, jDest);

		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		long t0 = -System.currentTimeMillis();
		// change this here
		double distance = graph.DFS(source, dest, visited, path);
		t0 += System.currentTimeMillis();
		window.setMsg("distance = " + distance + ", time=" + t0 + "ms");

		startAnimation(visited, path);
	}


	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		// System.out.println("Paint map panel.");
		g2d = (Graphics2D) g;

		super.paintComponent(g);

		// Paint with background color.
		g2d.setColor(backColor);
		g2d.fillRect(0, 0, width, height);

		// Draw the lines.
		g2d.setColor(sepColor);
		// Draw the horizontal lines.
		for (int y = 0, i = 0; i <= map.getHeight(); i++) {
			g2d.fillRect(0, y, width, sepSize);
			y += sepSize + cellSize;
		}
		// Draw the vertical lines.
		for (int x = 0, i = 0; i <= map.getWidth(); i++) {
			g2d.fillRect(x, 0, sepSize, height);
			x += sepSize + cellSize;
		}

		// Paint visited
		g2d.setColor(visitedColor);
		for (int i = 0; i < visitedMap.getHeight(); i++) {
			for (int j = 0; j < visitedMap.getWidth(); j++) {
				if (visitedMap.getObstacle(i, j)) {
					int x = j * cellSize + (j + 1) * sepSize;
					int y = i * cellSize + (i + 1) * sepSize;
					g2d.fillRect(x, y, cellSize, cellSize);
				}
			}
		}

		// Paint path
		g2d.setColor(pathColor);
		for (int i = 0; i < pathMap.getHeight(); i++) {
			for (int j = 0; j < pathMap.getWidth(); j++) {
				if (pathMap.getObstacle(i, j)) {
					int x = j * cellSize + (j + 1) * sepSize;
					int y = i * cellSize + (i + 1) * sepSize;

					Image dot;
					try {
						dot = ImageIO.read(new File("white_dot.png"));
						g2d.drawImage(dot, 
								x, y, null);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						// System.out.println("File not found");
						g2d.fillRect(x, y, cellSize, cellSize);
						e.printStackTrace();
					}
				} else {
					// Do nothing
				}
			}
		}


		// Paint obstacles
		g2d.setColor(wallColor);
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				if (map.getObstacle(i, j)) {
					int x = j * cellSize + (j + 1) * sepSize;
					int y = i * cellSize + (i + 1) * sepSize;
					g2d.fillRect(x, y, cellSize, cellSize);
				}
			}
		}

		if (sourceSet) {
			g2d.setColor(sourceColor);
			int x = jSource * cellSize + (jSource + 1) * sepSize;
			int y = iSource * cellSize + (iSource + 1) * sepSize;
			// g2d.fillRect(x, y, cellSize, cellSize);
			Image pacman;
			try {
				pacman = ImageIO.read(new File(pacmanImage));
				g2d.drawImage(pacman, 
						x, y, null);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				// System.out.println("File not found");
				g2d.fillRect(x, y, cellSize, cellSize);
				e.printStackTrace();
			}

		}

		if (destSet) {
			g2d.setColor(destColor);
			int x = jDest * cellSize + (jDest + 1) * sepSize;
			int y = iDest * cellSize + (iDest + 1) * sepSize;
			// g2d.fillRect(x, y, cellSize, cellSize);
			
			if ((algorithmComplete = true) && (destinationReached == true)) {
				Image pacman;
				try {
					pacman = ImageIO.read(new File("pacman22x22.png"));
					g2d.drawImage(pacman, 
							x, y, null);
	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// System.out.println("File not found");
					g2d.fillRect(x, y, cellSize, cellSize);
					e.printStackTrace();
				}
			} else {
				Image cherry;
				try {
					cherry = ImageIO.read(new File("cherry22x22.png"));
					g2d.drawImage(cherry, 
							x, y, null);
	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// System.out.println("File not found");
					g2d.fillRect(x, y, cellSize, cellSize);
					e.printStackTrace();
				}
			}
		}

		if ((destSet == false) && (algorithmComplete = true) && (destinationReached == true)) {
			System.out.println("drawing Pacman");
			
			int jPosition = jDest * cellSize + (jDest + 1) * sepSize;
			System.out.println("jPosition: " + jPosition);
			int iPosition = iDest * cellSize + (iDest + 1) * sepSize;
			System.out.println("iPosition: " + iPosition);

			Image pacman;
			try {
				pacman = ImageIO.read(new File(pacmanImage));
				g2d.drawImage(pacman, jPosition, iPosition, null);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				// System.out.println("File not found");
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Nothing to do here.
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Nothing to do here.
	}

	private Point screenToGridCoords(Point p) {
		int x = p.x;
		int y = p.y;

		// Search for iPos.
		int iPos = -1;
		for (int i = 0; i < height; i++) {
			y -= sepSize;
			if (y < 0) {
				break;
			}
			if (y < cellSize) {
				iPos = i;
				break;
			}
			y -= cellSize;
		}

		// Search for jPos.
		int jPos = -1;
		for (int j = 0; j < height; j++) {
			x -= sepSize;
			if (x < 0) {
				break;
			}
			if (x < cellSize) {
				jPos = j;
				break;
			}
			x -= cellSize;
		}

		return new Point(iPos, jPos);
	}

	private boolean updateMousePos(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		pMousePrev = pMouse;
		pMouse = new Point(x, y);

		Point gridP = screenToGridCoords(pMouse);
		int iPos = gridP.x;
		int jPos = gridP.y;
		if (iPos != -1 && jPos != -1) {
			iMouse = iPos;
			jMouse = jPos;
			return true;
		}

		return false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		boolean OK = updateMousePos(e);
		if (!OK) {
			System.out.println("Clicked on separator");
		}
		System.out.println("mousePressed: " + iMouse + "," + jMouse);
		if (!OK) {
			return;
		}

		if (op == Operation.SET_SOURCE) {
			if (map.getObstacle(iMouse, jMouse)) {
				return;
			}
			sourceSet = true;
			iSource = iMouse;
			jSource = jMouse;
			repaint();
		} else if (op == Operation.SET_DEST) {
			if (map.getObstacle(iMouse, jMouse)) {
				return;
			}
			destSet = true;
			iDest = iMouse;
			jDest = jMouse;
			repaint();
		} else if (op == Operation.SET_WALL) {
			if (sourceSet && iSource == iMouse && jSource == jMouse) {
				return;
			}
			if (destSet && iDest == iMouse && jDest == jMouse) {
				return;
			}
			map.setObstacle(iMouse, jMouse, true);
			repaint();
		} else if (op == Operation.CLEAR_WALL) {
			map.setObstacle(iMouse, jMouse, false);
			repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Nothing to do here.
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Nothing to do here.
	}

	private void genPoints(Point2D.Double a, Point2D.Double b, ArrayList<Point2D.Double> list) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		double dist = Math.abs(dx * dx + dy * dy);
		if (dist < 0.1) {
			return;
		}

		double mx = (a.x + b.x) / 2;
		double my = (a.y + b.y) / 2;
		Point2D.Double m = new Point2D.Double(mx, my);
		list.add(m);
		genPoints(a, m, list);
		genPoints(m, b, list);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateMousePos(e);
		System.out.println("mouseDragged: " + iMouse + "," + jMouse);

		if (op == Operation.SET_WALL) {
			if (pMousePrev != null) {
				// paint all intermediate blocks
				ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
				Point2D.Double a = new Point2D.Double(pMousePrev.x, pMousePrev.y);
				Point2D.Double b = new Point2D.Double(pMouse.x, pMouse.y);
				genPoints(a, b, points);
				System.out.println("list size = " + points.size());
				for (Point2D.Double p : points) {
					Point intP = new Point((int) Math.round(p.getX()), (int) Math.round(p.getY()));
					Point gridP = screenToGridCoords(intP);
					int iPos = gridP.x;
					int jPos = gridP.y;
					if (iPos < 0 || iPos >= map.getHeight() || jPos < 0 || jPos >= map.getWidth()) {
						continue;
					}
					// if (iPos == -1 || jPos == -1) continue;
					map.setObstacle(iPos, jPos, true);
				}
			} else {
				if (iMouse < 0 || iMouse >= map.getHeight() || jMouse < 0 || jMouse >= map.getWidth()) {
					return;
				}
				map.setObstacle(iMouse, jMouse, true);
			}

			repaint();
		} else if (op == Operation.CLEAR_WALL) {
			if (iMouse < 0 || iMouse >= map.getHeight() || jMouse < 0 || jMouse >= map.getWidth()) {
				return;
			}
			map.setObstacle(iMouse, jMouse, false);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Nothing to do here.
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

}
