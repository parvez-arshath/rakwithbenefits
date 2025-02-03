package org.sme.runner;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.sme.utilities.BaseClass;
import org.sme.utilities.JvmReport;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "C:\\Users\\impelox-pc-048\\eclipse-workspace\\SmeSingleCategory\\src\\test\\resources\\SmeAllFeatures\\distributor_createquote.feature", glue = "org.sme.code", dryRun = false, plugin = {
		"json:C:\\Users\\impelox-pc-048\\eclipse-workspace\\SmeSingleCategory\\target\\report\\AIAW.json" })
public class SmeTestRunner extends BaseClass {

	@AfterClass
	public static void generateReport() {
		JvmReport.generateJVMReport(
				"C:\\Users\\impelox-pc-048\\eclipse-workspace\\SmeSingleCategory\\target\\report\\AIAW.json");

	}

}
