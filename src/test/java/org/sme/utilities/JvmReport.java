package org.sme.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;


public class JvmReport {
	
	public static void generateJVMReport(String jsonpath) {
		
		File f=new File("C:\\Users\\impelox-pc-048\\Desktop\\Impelox Projects\\Sme-Rak\\target\\jvmreport");
		
		Configuration con=new Configuration(f, "RAK Create Quote and Total Validation");
		con.addClassifications("TestName", "Validating Create Quote RAK");
		con.addClassifications("TestDescription", "verifying login and create a quote,total premium calculation");
		con.addClassifications("Category", "Regression");
		con.addClassifications("Author", "arshath");

		List<String> listofjson=new ArrayList<>();
		listofjson.add(jsonpath);
		
		ReportBuilder r=new ReportBuilder(listofjson, con);
		r.generateReports();
		
	}

}
