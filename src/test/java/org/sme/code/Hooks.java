package org.sme.code;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.sme.utilities.BaseClass;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;

public class Hooks extends BaseClass{
	
	 @AfterStep
	    public void takeScreenshot(Scenario scenario) {
	        if (scenario.isFailed() || scenario.getStatus().toString().equalsIgnoreCase("PASSED")) {
	            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	            String screenshotPath = "target/screenshots/" + scenario.getName().replace(" ", "_") + "_" + System.currentTimeMillis() + ".png";
	            File destination = new File(screenshotPath);
	            
	            try {
	                FileUtils.copyFile(srcFile, destination);
	                scenario.attach(FileUtils.readFileToByteArray(destination), "image/png", "Screenshot for Step");
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

}
