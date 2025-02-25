package org.sme.runner;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.sme.utilities.BaseClass;
import org.sme.utilities.JvmReport;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "C:\\Users\\impelox-pc-048\\Desktop\\Impelox Projects\\Sme-Rak\\src\\test\\resources\\SmeAllFeatures\\distributor_createquote.feature", glue = "org.smerak.code", dryRun = false, plugin = {
		"json:C:\\Users\\impelox-pc-048\\Desktop\\Impelox Projects\\Sme-Rak\\target\\report\\rak.json" })
public class SmeTestRunner extends BaseClass {

	@AfterClass
	public static void generateReport() {
		JvmReport.generateJVMReport(
				"C:\\Users\\impelox-pc-048\\Desktop\\Impelox Projects\\Sme-Rak\\target\\report\\rak.json");

	}

}
