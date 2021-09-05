package com.projects.passportApp.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

//https://github.com/Astalion/CubeMaker/blob/master/utilities/ImageUtilities.java

public class ImageUtilities {	
	private static final double INCH_2_CM = 2.54;
	
	/**
	 * crop an image by how much to crop from top, bottom, left, and right
	 * 
	 * @param original - image as a BufferedImage
	 * @param top - how much to crop from top
	 * @param bot - how much to crop from bottom
	 * @param left - how much to crop from left
	 * @param right - how much to crop from right
	 * @return cropped image as a BufferedImage
	 */
	
	public static BufferedImage cropImage(BufferedImage original, int top, int bot, int left, int right) {
		return original.getSubimage(left, top, 
				original.getWidth()-left-right, original.getHeight()-top-bot);
		
	}
	
	/**
	 * crop an image using BufferedImage's getSubImage()
	 * 
	 * @param original - image as a BufferedImage
	 * @param x - leftest point of image to crop
	 * @param y - toppest point of image to crop
	 * @param w - width of crop rectangle from x
	 * @param h - height of crop rectangle from y
	 * @return cropped image as a BufferedImage
	 */
	public static BufferedImage cropImageByPoint(BufferedImage original, int x, int y, int w, int h) {
		return original.getSubimage(x, y, w, h);
		
	}

	/**
	 * scale an image to a specified width and height
	 * 
	 * @param original - image as a BufferedImage
	 * @param width - width to scale to
	 * @param height - height to scale to
	 * @return scaled image as a BufferedImage
	 */
	public static BufferedImage scaleImage(BufferedImage original, int width, int height){
		
		BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(((float)width)/original.getWidth(), ((float)height)/original.getHeight());
		AffineTransformOp scaleOp = 
				new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		scaled = scaleOp.filter(original, scaled);
		return scaled;
		
	}
	
	/**
	 * save image to local
	 * 
	 * @param img - image to save as a BufferedImage
	 * @param output - File object to save image to
	 * @throws IOException
	 */
	public static void saveImage(BufferedImage img, File output) throws IOException {
		output.delete();
		
		final String formatName = "png";
		
		for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
		   ImageWriter writer = iw.next();
		   ImageWriteParam writeParam = writer.getDefaultWriteParam();
		   ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
		   IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
		   if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
		      continue;
		   }
		
		   setDPI(metadata, 300);
		
		   final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
		   
		   try {
		      writer.setOutput(stream);
		      writer.write(metadata, new IIOImage(img, null, metadata), writeParam);
		   } finally {
		      stream.close();
		   }
		   break;
		}
	}
	
	/**
	 * set DPI of image
	 * 
	 * @param metadata - image metadata
	 * @param DPI - DPI to set image to
	 * @throws IIOInvalidTreeException
	 */
	private static void setDPI(IIOMetadata metadata, int DPI) throws IIOInvalidTreeException {

		// for PNG, it's dots per millimeter
		double dotsPerMilli = 1.0 * DPI / 10 / INCH_2_CM;
		
		IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
		horiz.setAttribute("value", Double.toString(dotsPerMilli));
		
		IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
		vert.setAttribute("value", Double.toString(dotsPerMilli));
		
		IIOMetadataNode dim = new IIOMetadataNode("Dimension");
		dim.appendChild(horiz);
		dim.appendChild(vert);
		
		IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
		root.appendChild(dim);

		    metadata.mergeTree("javax_imageio_1.0", root);
	}
	
	/**
     * Converts an image to another format
     *
     * @param inputImagePath Path of the source image
     * @param outputImagePath Path of the destination image
     * @param formatName the format to be converted to, one of: jpeg, png,
     * bmp, wbmp, and gif
     * @return true if successful, false otherwise
     * @throws IOException if errors occur during writing
     */
    public static boolean convertFormat(String inputImagePath,
    		String outputImagePath, String formatName) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputImagePath);
        FileOutputStream outputStream = new FileOutputStream(outputImagePath);
         
        // reads input image from file
        BufferedImage inputImage = ImageIO.read(inputStream);
         
        // writes to the output image in specified format
        boolean result = ImageIO.write(inputImage, formatName, outputStream);
         
        // needs to close the streams
        outputStream.close();
        inputStream.close();
         
        return result;
    }
	
}
