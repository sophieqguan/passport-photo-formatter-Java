package com.projects.passportApp;

import org.junit.Assert;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import nu.pattern.OpenCV;

public class FacialRecognitionTest2 {
	private static final String basePath = "./userimg/";
	public static void locateFace() {
		  OpenCV.loadLocally();
		  //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		  
	      // Reading the Image from the file and storing it in to a Matrix object
	      String file = basePath + "test.jpg";
	      Mat frame = Imgcodecs.imread(file);
	      // Instantiating the CascadeClassifier
	      String xmlFile = basePath + "lbpcascade_frontalface.xml";
	      CascadeClassifier classifier = new CascadeClassifier(xmlFile);

	      // Detecting the face in the snap
	      MatOfRect faceDetections = new MatOfRect();
	      classifier.detectMultiScale(frame, faceDetections);
	      System.out.println(String.format("Detected %s faces", 
	         faceDetections.toArray().length));

	      

	      MatOfRect faces = new MatOfRect();
			Mat grayFrame = new Mat();
			
			// convert the frame in gray scale
			Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
			// equalize the frame histogram to improve the result
			Imgproc.equalizeHist(grayFrame, grayFrame);
			
			// detect faces
		    classifier.detectMultiScale(frame, faceDetections);
					
			
	      
	      Rect[] rects = faces.toArray();
	      
	      // Drawing boxes
	      for (Rect rect : rects) {
	         Imgproc.rectangle(
	            frame,                                               // where to draw the box
	            new Point(rect.x, rect.y),                            // bottom left
	            new Point(rect.x + rect.width, rect.y + rect.height), // top right
	            new Scalar(0, 0, 255),
	            3                                                     // RGB colour
	         );
	      }
 
	      // Writing the image
	      Imgcodecs.imwrite(basePath + "result.png", frame);

	      System.out.println("Image Processed");
	}
	
	
	
	
	
	
	public static void main(String[] args) {

		FacialRecognitionTest2.locateFace();
		System.out.println("DONE");
	}
	
}
