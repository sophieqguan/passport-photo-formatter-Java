package com.projects.passportApp.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Rect;

public class PlaceImage {
	private static final File g = new File("resource/grid.png");
	private static BufferedImage grid;
	// private static final String BASE = "userimg/";
	// private static final String TYPE = "png";
	// private static File output;
	
	private static final int IN2PX = 300;
	private static final int IMGPX = 2 * IN2PX;
	
	/**
	 * resize image into 600 x 600 px (without facial recognition)
	 * 
	 * @param inputStream - image as an FileInputStream
	 * @return - resized image as a BufferedStream
	 * @throws IOException
	 */
	public static BufferedImage resize(FileInputStream inputStream) throws IOException {
	
		BufferedImage result, scaled, orig = ImageIO.read(inputStream);
		int w = orig.getWidth(); // in pixels
		int h = orig.getHeight(); // in pixels
		if (w != IMGPX || h != IMGPX) {
			double pct = 1;
			if (w > h)	pct = IMGPX / h;
			else pct = ((float) IMGPX) / w;
			int newH = (int) (h * pct);
			int newW = (int) (w * pct);
			scaled = ImageUtilities.scaleImage(orig, newW, newH);
			newH = (int) ((newH - IMGPX) / 2);
			newW= (int) ((newW - IMGPX) / 2);
			result = ImageUtilities.cropImage(scaled, newH, newH, newW, newW);
		}
		else result = orig;	
		
		return result;
	}
	
	 private static int HEADSZ = 190; // unit = px
 	 private static int OFFSET = 40;
	/**
	 * resize image into 600 x 600 px (using facial recognition)
	 * 
	 * @param inputStream - image as an FileInputStream
	 * @param face - a Rect object containing the face in image
	 * @param scale - scale ratio to resize face into standard
	 * @return resized image as a BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage resizeFace(FileInputStream inputStream, Rect face, double scale) throws IOException {
		
		BufferedImage result, scaled, orig = ImageIO.read(inputStream);
		int w = orig.getWidth(); // in pixels
		int h = orig.getHeight(); // in pixels
		if (w != HEADSZ || h != HEADSZ) {

		// scale image so face fit 420 x 420 px
		int newH = (int) (h * scale);
		int newW = (int) (w * scale);
		scaled = ImageUtilities.scaleImage(orig, newW, newH);

		if (newH <= IMGPX && newW <= IMGPX)
			return scaled;

		// find surrounding white space from face
		double maxTop = (face.y) * scale;
		double maxLeft = (face.x) * scale;

		double padTop = (int) ((IMGPX - (HEADSZ)) / 2);
		double padLeft = (int) ((IMGPX - (HEADSZ)) / 3);

		// if exceeds max height
		padTop = Math.min(padTop, maxTop / 2);
		padLeft = Math.min(padLeft, maxLeft / 2);

		// crop image
		int x = (int) ((face.x) * scale - padLeft);
		int y = (int) ((face.y) * scale - padTop) - OFFSET;
		result = ImageUtilities.cropImageByPoint(scaled, x, y, IMGPX, IMGPX);
		} else
		result = orig;

		return result;
	}
	
	/**
	 * place image onto print grid layout
	 * 
	 * @param userImg - user image (resized) as a BufferedImage
	 * @return gridded image as a BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage placeOnGrid(BufferedImage userImg) throws IOException {
        // Create a Graphics  from the background image	
		grid = ImageIO.read(new FileInputStream(g));
        Graphics2D g = grid.createGraphics();
       
        //Antialiasing Rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw grid layout as background at location (0,0), unit = pixels
        g.drawImage(grid, 0, 0, null);
        // Draw portrait photo (left) at location (150, 300), unit = pixels
        g.drawImage(userImg, (int) (0.5 * IN2PX), 1 * IN2PX, null);
        // Draw portrait photo (right) at location (1050, 300), unit = pixels
        g.drawImage(userImg, (int) (3.5 * IN2PX), 1 * IN2PX, null);
 
        g.dispose();
                
        return grid;
	}
	
}