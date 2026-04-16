package view;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIUtil {

	public static void setGlobalFont()
	{
		//for aa or antialiasing (cuz its pixel-y without it)
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");

		try
		{
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/canva-sans-medium.otf")).deriveFont(14f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);

			for(Object key : UIManager.getLookAndFeelDefaults().keySet())
			{
				if(key.toString().toLowerCase().contains("font"))
				{
					UIManager.put(key, customFont);
				}
			}
		} catch (Exception e)
		{
			System.out.println("uh oh ur font broke");
			e.printStackTrace();
		}
	}
	
	public static JFrame setupGUI(JFrame frame, Container cp, int width, int height) {
		setGlobalFont();
		frame = new JFrame();

		//null layout for custom placing (i hate layouts!!)
		cp = BackgroundPanel.create("assets/main_menu/bg.jpg"); //temp
		cp.setLayout(null);
		cp.setPreferredSize(new Dimension(width, height));

		frame.setContentPane(cp);
		frame.pack();
		frame.setVisible(true);

		/*
		Insets insets = frame.getInsets();
		int targetWidth = 720 + insets.left + insets.right;
		int targetHeight = 480 + insets.top + insets.bottom;
		frame.setSize(targetWidth, targetHeight);
		*/
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setTitle("BasketGram");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}
	
	public static JFrame setupGUI(JFrame frame, Container cp, int width, int height, String title, boolean dispose) {
		setGlobalFont();
		frame = new JFrame();

		//null layout for custom placing (i hate layouts!!)
		cp = BackgroundPanel.create("assets/main_menu/bg.jpg"); //temp
		cp.setLayout(null);
		cp.setPreferredSize(new Dimension(width, height));

		frame.setContentPane(cp);
		frame.pack();
		frame.setVisible(true);

		/*
		Insets insets = frame.getInsets();
		int targetWidth = 720 + insets.left + insets.right;
		int targetHeight = 480 + insets.top + insets.bottom;
		frame.setSize(targetWidth, targetHeight);
		*/
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setTitle(title);
		if(dispose)
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		else
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}

	//creates an invsible button for overlayed options
	public static JButton createIButton(int x, int y, int w, int h) {
		JButton button = new JButton();
		button.setBounds(x, y, w, h);

		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(true);
		button.setFocusPainted(false);

		//debugging
		button.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

		return button;
	}

	/**
	 * Creates a text field with transparent background and red border styling.dkjh
	 * 
	 * The text field is configured to be non-opaque with black text on a transparent background.
	 * 
	 * @param x the x-coordinate position of the text field.
	 * @param y the y-coordinate position of the text field.
	 * @param w the width of the text field.
	 * @param h the height of the text field.
	 * @return a configured {@code JTextField} with transparent background and red border.
	 */
	public static JTextField createTextField(int x, int y, int w, int h)
	{
		JTextField textField = new JTextField();

		textField.setBounds(x,y,w,h);
		textField.setBorder(BorderFactory.createLineBorder(Color.RED));
		textField.setBorder(null);
		textField.setOpaque(false);
		textField.setBackground(new Color(0,0,0,0));
		textField.setForeground(Color.BLACK);

		return textField;
	}

	public static JLabel createText(String txt, int x, int y, int w, int h)
	{
		JLabel label = new JLabel(txt);
		label.setBounds(x,y,w,h);
		//label.setBorder(BorderFactory.createLineBorder(Color.RED,2));
		return label;
	}

	public static JButton createCenterImageButton(String path, int y)
	{
		ImageIcon icon = new ImageIcon(path);
		JButton button = new JButton(icon);

		//bounds
		Dimension size = button.getPreferredSize();
		button.setSize(size);
		int x = (640 - size.width) /2;
		button.setLocation(x,y);

		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setOpaque(false);
	
		return button;
	}
	
	public static JPanel showScrollingPanel(LinkedHashMap<String, String> information, 
			int x, int yStart, int yGap, int visibleCount, 
			Consumer<String> onClick) {
		
		JPanel cp = new JPanel(null);
		JPanel buttonPanel = new JPanel(null);	
		ArrayList<JButton> buttonList = new ArrayList<>();
		cp.setOpaque(false);
		buttonPanel.setOpaque(false);
		
		int i = 0;
		for(Map.Entry<String, String> entry : information.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			JButton btn = createCenterImageButton("assets/banner.png",115); 
			btn.setHorizontalTextPosition(SwingConstants.RIGHT);
			btn.setVerticalTextPosition(SwingConstants.CENTER);
			btn.setIconTextGap(-620);
			btn.setMargin(new Insets(0, 10, 0, 10));
			btn.setText(value);
			btn.setLocation(x,yStart+i*yGap);
			btn.setBounds(x, yStart + i * yGap, 720, 63);

			final String btnValue = key + "/" + value;
			btn.addActionListener(e -> onClick.accept(btnValue));
			buttonList.add(btn);
			buttonPanel.add(btn);
			i++;
		}

		int panelHeight = information.size() * yGap;
		buttonPanel.setOpaque(false);
		buttonPanel.setSize(590,panelHeight);
		buttonPanel.setLocation(40,115);
		cp.add(buttonPanel);

		cp.addMouseWheelListener(new MouseWheelListener() {
			int offset = 0;

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) 
			{
				int rotation = e.getWheelRotation(); //1 down -1 up
				offset -= rotation * 20; //so 20 is the offset like when scrolled ykykyk

				int maxOffset = 0;
				int minOffset = Math.min(0, visibleCount * yGap - panelHeight);
				offset = Math.max(minOffset, Math.min(maxOffset, offset));

				buttonPanel.setLocation(40,115+offset);
				cp.repaint();
			}
		});

		return cp;
	}

    // create background color
    public static JPanel createColorPanel(String hex) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode(hex));
        return panel;
    }

    // create text label
    public static JLabel createTextLabel(String text, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);

        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;
    }

    // create small white layer
    public static JPanel createWhiteLayer(Container parent, int x, int y, int w, int h) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(x, y, w, h);
        parent.add(panel);
        return panel;
    }

}