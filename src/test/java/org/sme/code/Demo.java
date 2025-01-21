package org.sme.code;

import java.io.IOException;
import java.sql.SQLException;

import org.sme.utilities.BaseClass;

public class Demo extends BaseClass {

	public static void main(String[] args) throws SQLException, IOException {
		// base premium
		fetchDataFromDatabase(calculatorData().getProperty("dbUrlUAT"), calculatorData().getProperty("dbUsernameUAT"),
				calculatorData().getProperty("dbPasswordUAT"), calculatorData().getProperty("queryAIAWBasePremium"),
				calculatorData().getProperty("excelCalculatorFilePath"), 0);

		// benefits
		fetchDataFromDatabase(calculatorData().getProperty("dbUrlUAT"), calculatorData().getProperty("dbUsernameUAT"),
				calculatorData().getProperty("dbPasswordUAT"), calculatorData().getProperty("queryAIAWBenefits"),
				calculatorData().getProperty("excelCalculatorFilePath"), 1);

		// nationality loadings
		fetchDataFromDatabase(calculatorData().getProperty("dbUrlUAT"), calculatorData().getProperty("dbUsernameUAT"),
				calculatorData().getProperty("dbPasswordUAT"),
				calculatorData().getProperty("queryAIAWNationalityLoadings"),
				calculatorData().getProperty("excelCalculatorFilePath"), 4);

		// industry loading
		fetchDataFromDatabase(calculatorData().getProperty("dbUrlUAT"), calculatorData().getProperty("dbUsernameUAT"),
				calculatorData().getProperty("dbPasswordUAT"),
				calculatorData().getProperty("queryAIAWIndustryLoadings"),
				calculatorData().getProperty("excelCalculatorFilePath"), 5);

		// previous insurer loading
		fetchDataFromDatabase(calculatorData().getProperty("dbUrlUAT"), calculatorData().getProperty("dbUsernameUAT"),
				calculatorData().getProperty("dbPasswordUAT"),
				calculatorData().getProperty("queryAIAWPreviousInsurerLoadings"),
				calculatorData().getProperty("excelCalculatorFilePath"), 6);

		// Commission
		fetchDataFromDatabase(calculatorData().getProperty("dbUrlUAT"), calculatorData().getProperty("dbUsernameUAT"),
				calculatorData().getProperty("dbPasswordUAT"), calculatorData().getProperty("queryAIAWCommission"),
				calculatorData().getProperty("excelCalculatorFilePath"), 9);

		// Census
		toFetchCensusSheet("C:\\Users\\impelox-pc-048\\Desktop\\censuses sheet\\census_a_automation.xlsx",
				"C:\\Users\\impelox-pc-048\\eclipse-workspace\\SmeSingleCategory\\target\\ExcelCalculatorForDistributor\\Arshad New Calculator.xlsx",
				0, 10);

	}

}
