package com.projects.passportApp;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.projects.passportApp.controller.FileController;

public class FileControllerTest {
	private static final String BASE = "./userimg/";
	private static final String TYPE = "png";
	
	@Test
	public void resizeImageTest() throws IOException {
		String outputPath = BASE + "finalTEST." + TYPE;
		File output = new File(outputPath);
		
		String path = BASE + "test." + TYPE;
		File input = new File(path);
		
		FileController f = new FileController();
		f.resizeImage(input, path, output);
	}
}
