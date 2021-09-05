package com.projects.passportApp.controller;


import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

public class FacialRecognition {	
	/**
	 * find face in image
	 * 
	 * @param fileName - file name as String
	 * @return a Rect object containing details on the largest face found in image
	 */
	public static Rect locateFace(String fileName) {
		// System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
		nu.pattern.OpenCV.loadLocally();
		  
		// Reading the Image from the file and storing it in to a Matrix object
		Mat src = Imgcodecs.imread(fileName);
		// Instantiating facial recognizer cascade classifier
		String xmlFile = "./resource/lbpcascade_frontalface.xml";
		CascadeClassifier classifier = new CascadeClassifier(xmlFile);
		
		// Detecting faces in the picture
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(src, faceDetections);
		Rect[] faces = faceDetections.toArray();
		System.out.println(String.format("Detected %s face(s)", 
				faces.length));
		
		// if no face was found
		if (faces.length == 0) return null;
		
		// locate largest face
		Rect face = faces[0];
		for (Rect r : faces) 
			face = (face.area() > r.area()) ? face : r;
	    
		/*  
	    // draw red rectangles around face
        Imgproc.rectangle(
	        src,                                               // where to draw the box
	        new Point(face.x, face.y),                            // bottom left
	        new Point(face.x + face.width, face.y + face.height), // top right
	        new Scalar(0, 0, 255),
	        3                                                     // RGB colour
        );
	    
	    // Writing the image
	    Imgcodecs.imwrite(BASE + "result.png", src);
		*/
	      
	    return face;
	}
		
}