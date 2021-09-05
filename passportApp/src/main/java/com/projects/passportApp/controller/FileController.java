package com.projects.passportApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.opencv.core.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projects.passportApp.controller.*;

@CrossOrigin
@RestController
public class FileController {
	@Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private static int id = 0;
	private static final String BASE = "userimg/";
	private static final String TYPE = "png";
	private static final int SCALETO = (int) (1.4 * 300); //max head size = 1.4"
    
	/**
	 * you give me: image, I give you: formatted image
	 * 
	 * @param file - file to be modified
	 * @return modified image in byte array format
	 * @throws IllegalStateException
	 * @throws IOException
	 */

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> uploadFile(@RequestParam MultipartFile file) throws IllegalStateException, IOException {
    	String fileName = file.getOriginalFilename();
    	
        logger.info(String.format("File uploaded successfully (id=%s).", id));
        
        String filePath = request.getServletContext().getRealPath("/") + id + fileName ; 
        logger.info(filePath);
        String newFileName = id + "userImg." + TYPE;
        String newPath = BASE + newFileName;
        File input = new File(newPath);
        file.transferTo(input); // SAVE TO LOCAL
        logger.info(input.getAbsolutePath());
        
        // create output file
        String outputPath = BASE + "final" + newFileName;
        File output = new File(outputPath);
        
		// scale and crop image
        resizeImage(input, newPath, output);
		
		// log new id
		logger.info("id updated to: " + ++id);
		
		// return to front-end
		FileInputStream finalImg = new FileInputStream(outputPath);
        byte[] bytes = IOUtils.toByteArray(finalImg);
        
        return ResponseEntity.ok().body(bytes);
    }
    
    /**
     * helper function to resize image
     * 
     * @param input - input File object
     * @param path - path to input image
     * @param output - output File object
     * @throws IOException
     */
	private static int OFFSETY = 100;
  	private static int OFFSETX = 30;
    public void resizeImage(File input, String path, File output) throws IOException {
    	// recognize face
        FileInputStream inputStream = new FileInputStream(input);	
		Rect face = FacialRecognition.locateFace(path);
		
		BufferedImage resized;
		
		// if detected face (execute facial recognition resizing)
		if (face != null) {
			float faceW = face.width + OFFSETX;
      		float faceH = face.height + OFFSETY;
			float scale = (faceW > faceH) ? SCALETO / faceW : SCALETO / faceH;
			
			// resize and grid user-uploaded image
			resized = PlaceImage.resizeFace(inputStream, face, scale);
		}
		else 
			resized = PlaceImage.resize(inputStream);
		
		
		BufferedImage onGrid = PlaceImage.placeOnGrid(resized);
		ImageUtilities.saveImage(onGrid, output);
    }
    
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
    	CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    	multipartResolver.setMaxUploadSize(-1);
    	return multipartResolver;
    }
    
    
}