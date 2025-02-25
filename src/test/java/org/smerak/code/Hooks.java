package org.smerak.code;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.sme.utilities.BaseClass;
import org.testng.annotations.BeforeClass;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;

public class Hooks extends BaseClass {

	@AfterStep
	public void takeScreenshot(Scenario scenario) {
		if (scenario.isFailed() || scenario.getStatus().toString().equalsIgnoreCase("PASSED")) {
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String screenshotPath = "target/screenshots/" + scenario.getName().replace(" ", "_") + "_"
					+ System.currentTimeMillis() + ".png";
			File destination = new File(screenshotPath);

			try {
				FileUtils.copyFile(srcFile, destination);
				scenario.attach(FileUtils.readFileToByteArray(destination), "image/png", "Screenshot for Step");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@BeforeClass
	@Given("User must be in the login page")
	public void user_must_be_in_the_login_page() throws IOException {
		launchBrowser("chrome");
		launchUrl(loginData().getProperty("url"));
		maximizeWindow();
		impWait();
	}

}
