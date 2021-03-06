package com.wings2d.framework;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.wings2d.framework.rendering.DrawPanel;
import com.wings2d.framework.rendering.DrawPanelCanvas;
import com.wings2d.framework.rendering.DrawPanelJPanel;

/**
 * Extend this class with your main game class, and override the
 * init/update/render methods. super.init() / super.update() / super.render()
 * should be called by your int/update/render functions.
 */
public abstract class Game extends Thread {
	/** {@link javax.swing.JFrame JFrame} used to contain the canvas the game will be drawn on **/
	private JFrame frame;
	/** {@link java.awt.Canvas Canvas} needs to be inside a panel for sizing to work properly. 
	 * This is not exposed to the user
	 */
	private JPanel panel;
	/** Used to draw to canvas */
	private Graphics2D renderer;
	/** Used to stop program execution */
	private boolean isRunning = true;
	/** Used to run the init() function only once */
	private boolean initalized = false;
	/** Handles the drawing canvas */
	private DrawPanel draw;
	/** Background {@link java.awt.Color Color} of the draw panel */
	private Color backgroundColor;
	/** Background {@link java.awt.Color Color} of the frame */
	private Color frameColor;
	private double lastFpsTime = 0;
	/** The current fps */
	private int fps = 0;
	/** The width with the game was created. Used to determine scale */
	private int ogWidth;
	/** Control debug prints */
	private boolean debug = false;
	/** Level manager for this game. Other LevelManagers should not be created */
	private LevelManager manager;
	
	/**
	 * Call super(debug) from your constructor to use this
	 * @param debug Use debug prints (FPS, ect.)
	 */
	public Game(boolean debug, int width, int height) {
		this.debug = debug;
		manager = new LevelManager();
		
		ogWidth = width;
		
		backgroundColor = Color.DARK_GRAY;
		frameColor = Color.BLACK;
		
		frame = new JFrame();
		frame.addWindowListener(new FrameClose());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setMinimumSize(new Dimension(480, 270));
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setBackground(frameColor);

		panel = new JPanel();
		panel.setBackground(frameColor);
		panel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0f)));
		panel.setLayout(null);

//		draw = new DrawPanelCanvas();
		draw = new DrawPanelJPanel(this);
		panel.add(draw.getCanvas(), BorderLayout.CENTER);
		frame.add(panel);
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				draw.resizePanel(panel);
				onResize(draw);
			}
		});
		
		frame.setVisible(true);
		draw.initGraphics();
		start();
	}

	/**
	 * Used to handle closing events when the frame is closed
	 */
	private class FrameClose extends WindowAdapter {
		@Override
		/** Calls the onClose function */
		public void windowClosing(final WindowEvent e) {
			isRunning = false;
			onClose();
		}
	}
	
	/**
	 * Called when the frame is resized. Override this to use this event.
	 * @param draw {@link com.wings2d.framework.rendering.DrawPanel DrawPanel} The game's window
	 */
	public void onResize(final DrawPanel draw)
	{
		double scale = Double.valueOf(draw.getWidth()) / ogWidth; 
		manager.setScale(scale);
	}

	/**
	 * Does nothing, but is called when the frame is closed. Override to do
	 * something when the window is closed.
	 **/
	public void onClose() {
		if (debug)
		{
			System.out.println("Closed");
		}
	}

	/**
	 * Get the drawing graphics, creating them if null.
	 * @return Graphics2D object to draw with
	 */
	protected Graphics2D getDrawGraphics() {
		return draw.getGraphics();
	}

	public void run() {
		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;   

		while (isRunning)
		{
			if (!initalized)
			{
				init();
				initalized = true;
			}
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / ((double)OPTIMAL_TIME);

			// update the frame counter
			lastFpsTime += updateLength;
			fps++;

			// update our FPS counter if a second has passed since
			// we last recorded
			if (lastFpsTime >= 1000000000)
			{
				if (debug)
				{
					System.out.println("(FPS: "+fps+")");
				}
				lastFpsTime = 0;
				fps = 0;
			}

			update(delta);
		
			draw.render();
			draw.afterRender();

			// we want each frame to take 10 milliseconds, to do this
			// we've recorded when we started the frame. We add 10 milliseconds
			// to this and then factor in the current time to give 
			// us our final value to wait for
			// remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
			try {
				long time = (lastLoopTime-System.nanoTime() + OPTIMAL_TIME) / 1000000;
				if (time < 0)
				{
					time = 0;
				}
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Called once on startup
	 */
	public void init() {

	}

	/**
	 * Use to update game logic
	 * @param delta Time from last update
	 */
	public void update(double delta) {

	}

	/**
	 * Used to draw to screen
	 * 
	 * @param g2d Passed in by the base class. Don't pass in your own graphics
	 *            object.
	 */
	public void render(Graphics2D g2d) {
		g2d.setColor(backgroundColor);
		g2d.fillRect(0, 0, draw.getCanvas().getWidth(), draw.getCanvas().getHeight());
	}
	
	/**
	 * Get the main frame
	 * @return The {@link javax.swing.JFrame JFrame} used by this game
	 */
	public JFrame getFrame() 
	{
		return frame;
	}
	/**
	 * Get the background color of the canvas
	 * @return {@link java.awt.Color Color} of the canvas
	 */
	public Color getCanvasColor() {
		return backgroundColor;
	}
	/**
	 * Set the background color of the canvas
	 * @param color {@link java.awt.Color Color} to set the canvas to
	 */
	public void setCanvasColor(Color color) {
		draw.getCanvas().setBackground(color);
		this.backgroundColor = color;
	}
	/**
	 * Get the background color of the canvas
	 * @return {@link java.awt.Color Color} of the frame
	 */
	public Color getFrameColor() {
		return frameColor;
	}
	/**
	 * Set the background color of the canvas
	 * @param color {@link java.awt.Color Color} to set the frame to
	 */
	public void setFrameColor(Color color) {
		this.frame.setBackground(color);
		this.panel.setBackground(color);
		this.frameColor = color;
	}
	/**
	 * Get object to control the levels for the game
	 * @return {@link com.wings2d.framework.LevelManager LevelManager} for this game
	 */
	public LevelManager getManager()
	{
		return manager;
	}
	
	public DrawPanel getDrawPanel()
	{
		return draw;
	}
}