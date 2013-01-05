package llc.webcast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class WebcastUtility {

	private final static Logger log = Logger.getLogger(WebcastUtility.class.getName());
	
	private StreamProperties streamProperties;
	
	private JTextField ministerNameTextField;
	private JTextField bibleVerseTextField;
	private JTextField serviceTimeTextField;
	
	private JButton startStreamButton;
	private JButton stopStreamButton;
	private JLabel broadcastStatusLabel = new JLabel("NOT BROADCASTING");
	
	private StreamBroadcaster broadcaster;
	
	final static String LOOKANDFEEL = "System";
	final static String THEME = "Test";

	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				log.info("Initializing Application...");
				new WebcastUtility(args);
			}
		});
	}

	public WebcastUtility(String[] args) {
		parseCommandLine(args);
		createAndShowGUI();
		broadcaster = new StreamBroadcaster(streamProperties);
	}
	
	private void initLookAndFeel() {
		String lookAndFeel = null;

		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			else if (LOOKANDFEEL.equals("System")) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			}
			else if (LOOKANDFEEL.equals("Motif")) {
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			}
			else if (LOOKANDFEEL.equals("GTK")) {
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			}
			else {
				System.err.println("Unexpected value of LOOKANDFEEL specified: " + LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			try {
				UIManager.setLookAndFeel(lookAndFeel);
				// If L&F = "Metal", set the theme
				if (LOOKANDFEEL.equals("Metal")) {
					if (THEME.equals("DefaultMetal"))
						MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
					else if (THEME.equals("Ocean"))
						MetalLookAndFeel.setCurrentTheme(new OceanTheme());
					UIManager.setLookAndFeel(new MetalLookAndFeel());
				}
			}
			catch (ClassNotFoundException e) {
				System.err.println("Couldn't find class for specified look and feel:" + lookAndFeel);
				System.err.println("Did you include the L&F library in the class path?");
				System.err.println("Using the default look and feel.");
			}
			catch (UnsupportedLookAndFeelException e) {
				System.err.println("Can't use the specified look and feel (" + lookAndFeel + ") on this platform.");
				System.err.println("Using the default look and feel.");
			}
			catch (Exception e) {
				System.err.println("Couldn't get specified look and feel ("
						+ lookAndFeel + "), for some reason.");
				System.err.println("Using the default look and feel.");
				e.printStackTrace();
			}
		}
	}
	
	public Component createComponents() {
		
		
		JPanel pane = new JPanel(new GridLayout(2,1));
		pane.add(createMetaDataPanel());
		pane.add(createBroadcastPanel());
		
		pane.setBorder(BorderFactory.createEmptyBorder(30, // top
				30, // left
				30, // bottom
				30) // right
		);

		return pane;
	}
	
	private Component createBroadcastPanel() {
		
		JPanel pane = new JPanel(new GridLayout(3,1));
		
		startStreamButton = new JButton("START BROADCAST");
		startStreamButton.setEnabled(true);
		startStreamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startStreamButton.setEnabled(false);
				stopStreamButton.setEnabled(true);
				try {
					log.info("Starting Broadcast...");
					
					streamProperties.setBibleVerses(bibleVerseTextField.getText());
					streamProperties.setMinisterName(ministerNameTextField.getText());
					streamProperties.setBibleVerses(serviceTimeTextField.getText());
					
					broadcaster.start();
					broadcastStatusLabel.setText("BROADCASTING");
					broadcastStatusLabel.setBackground(Color.GREEN);
				} catch (Exception e1) {
					startStreamButton.setEnabled(true);
					stopStreamButton.setEnabled(false);
					broadcastStatusLabel.setText("NOT BROADCASTING");
					broadcastStatusLabel.setBackground(Color.RED);
					log.info("Broadcast Exception : " + e1.getStackTrace());
				}
			}
		});
		
		stopStreamButton = new JButton("STOP BROADCAST");
		stopStreamButton.setEnabled(false);
		stopStreamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.info("Stopping Broadcast...");
				broadcaster.stop();
				startStreamButton.setEnabled(true);
				stopStreamButton.setEnabled(false);
				broadcastStatusLabel.setText("NOT BROADCASTING");
				broadcastStatusLabel.setBackground(Color.RED);
			}
		});
		
		
		broadcastStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		broadcastStatusLabel.setOpaque(true);
		broadcastStatusLabel.setBackground(Color.RED);
		
		
		pane.add(startStreamButton);
		pane.add(broadcastStatusLabel);
		pane.add(stopStreamButton);
		
		return pane;
	}

	private Component createMetaDataPanel() {
		JPanel metaDataPanel = new JPanel(new GridLayout(3,2));
		metaDataPanel.setBorder(BorderFactory.createTitledBorder("Meta Data"));
		
		ministerNameTextField = new JTextField();
		bibleVerseTextField = new JTextField();
		serviceTimeTextField = new JTextField();
		
		metaDataPanel.add(new JLabel("Minister Name"));
		metaDataPanel.add(ministerNameTextField);
		
		metaDataPanel.add(new JLabel("Bible Verses"));
		metaDataPanel.add(bibleVerseTextField);
		
		metaDataPanel.add(new JLabel("Service Time"));
		metaDataPanel.add(serviceTimeTextField);
		
		return metaDataPanel;
	}

	private void createAndShowGUI() {
		log.info("Initializing GUI...");
		// Set the look and feel.
		initLookAndFeel();

		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Webcast Utility");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(300,400));
		java.net.URL url = ClassLoader.getSystemResource("llc/webcast/resources/internet.png");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.createImage(url);
		frame.setIconImage(img);
		Component contents = createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);

	}
	
	protected void parseCommandLine(String[] args) {
		log.info("parsing properties file...");
		
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options options = new Options();
		options.addOption("p", "properties", true, "webcasting config file");

		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("properties")) {
				streamProperties = new StreamProperties(
						line.getOptionValue("properties"));
				
				log.info("FFMPEG Location = " + streamProperties.getFfmpegLocation());
				log.info("FFMPEG Command = " + streamProperties.getFfmpegCommand());
			} else {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("EC2Driver", options, true);
				System.exit(0);
			}
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
		}
	}
}
