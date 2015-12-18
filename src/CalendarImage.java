import javax.swing.*;

import java.io.*;
import java.awt.*;

public class CalendarImage extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4858029387148747756L;
	
	File imageFile;
	Image image;
	Toolkit tool;

	CalendarImage() {
		tool = getToolkit();
	}

	public void setImageFile(File f) {
		imageFile = f;
		try {
			image = tool.getImage(imageFile.toURI().toURL());
		} catch (Exception exp) {
		}
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int w = getBounds().width;
		int h = getBounds().height;
		g.drawImage(image, 0, 0, w, h, this);
	}
}