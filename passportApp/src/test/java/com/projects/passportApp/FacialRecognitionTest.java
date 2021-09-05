package com.projects.passportApp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;
import org.opencv.core.Rect;

import com.projects.passportApp.controller.FacialRecognition;
import com.projects.passportApp.controller.ImageUtilities;
import com.projects.passportApp.controller.PlaceImage;


public class FacialRecognitionTest {
	private static final String BASE = "./userimg/";
	private static final String TYPE = "png";
	private static final int SCALETO = (int) (1.4 * 300); //max head size = 1.4"
	
	@Test
	public void locateFaceTest() throws IOException {
		String outputPath = BASE + "finalTEST." + TYPE;
		File output = new File(outputPath);
		
		String path = BASE + "test.png";
		File img = new File(path);
		FileInputStream inputStream = new FileInputStream(img);
		
		
		System.out.println("Start");
		Rect face = FacialRecognition.locateFace(path);
		
		System.out.println("Found Face.");
		float faceW = face.width;
		float faceH = face.height;
		float scale = (faceW > faceH) ? SCALETO / faceW : SCALETO / faceH;
		
		BufferedImage resized = PlaceImage.resizeFace(inputStream, face, scale);
		System.out.println("Resized.");
		BufferedImage onGrid = PlaceImage.placeOnGrid(resized);
		System.out.println("Gridded.");
		ImageUtilities.saveImage(onGrid, output);
		
		
		System.out.println("DONE");
	}
}
