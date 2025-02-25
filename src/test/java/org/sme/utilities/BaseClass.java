package org.sme.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class BaseClass {

	public static WebDriver driver;

	public static void launchBrowser(String browser) {

		if (browser.equals("chrome")) {
			driver = new ChromeDriver();
		}
		if (browser.equals("ie")) {
			driver = new InternetExplorerDriver();
		}
		if (browser.equals("firefox")) {
			driver = new FirefoxDriver();
		}

		if (browser.equals("edge")) {
			driver = new EdgeDriver();
		}

	}

	public static void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public static void quitBrowser() {
		driver.quit();
	}

	public static void launchUrl(String url) {
		driver.get(url);
	}

	public static void fillTextBox(WebElement ele, String name) {
		ele.sendKeys(name);
	}

	public static void clickButton(WebElement btn) {
		btn.click();
	}

	public static void dateAndTime() {
		Date d = new Date();
		System.out.println(d);

	}

	public static String getPageUrl() {
		String currentUrl = driver.getCurrentUrl();
		return currentUrl;

	}

	public static String getPageTitle() {

		String title = driver.getTitle();
		return title;
	}

	public static void impWait() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));

	}

	public static Properties loginData() throws IOException {
		FileReader reader = new FileReader(
				System.getProperty("user.dir") + "\\target\\DatasForDistributor\\LoginData.properties");
		Properties props = new Properties();
		props.load(reader);
		return props;

	}

	public static Properties createQuoteData() throws IOException {
		FileReader reader = new FileReader(
				System.getProperty("user.dir") + "\\target\\DatasForDistributor\\createQuote.properties");
		Properties props = new Properties();
		props.load(reader);
		return props;

	}

	public static void jsSendKeys(WebElement args) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('value','')", args);

	}

	public static Select selectDropDownData(WebElement element, String value) {
		Select s = new Select(element);
		s.selectByValue(value);
		return s;
	}

	public static WebElement dynamicWait(WebElement element) {
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
		w.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	public static void jsClick(WebElement args) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", args);

	}

	public static String currentDate() {
		LocalDate currentDate = LocalDate.now();

		/* LocalDate tommrowDate = currentDate.plusDays(1); */
		String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		return formattedDate;

	}

	public static Properties calculatorData() throws IOException {
		FileReader reader = new FileReader(
				System.getProperty("user.dir") + "\\target\\DatasForDistributor\\CalculationData.properties");
		Properties props = new Properties();
		props.load(reader);
		return props;

	}

	// Database connection details
	static String dbURL = "jdbc:mysql://qa-database.cwfjz6cyloxy.me-south-1.rds.amazonaws.com:3306";
	static String dbUsername = "appuser";
	static String dbPassword = "appuseradmin";

	public static String basePremiumQuery(String schema, String groupName, String emirate, String tpa, String plan)
			throws Exception {

		// Variables to hold user inputs
		String emirateName, tpaName, planName;

		try (Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
			// Assigning input values
			emirateName = emirate;
			tpaName = tpa;
			planName = plan;

			// Step 1: Get Active Version ID
			String activeVersionQuery = "WITH ActiveVersion AS (" + " SELECT pv.id FROM " + schema
					+ ".product_versions pv" + " WHERE pv.status = 1 AND pv.effective_date <= CURDATE()"
					+ " ORDER BY pv.effective_date DESC LIMIT 1)" + " SELECT id FROM ActiveVersion;";

			int activeVersionId = 0;
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(activeVersionQuery)) {
				if (rs.next()) {
					activeVersionId = rs.getInt("id");
					System.out.println("Active Version ID: " + activeVersionId);
				}
			}

			// Step 2: Get Group ID based on Active Version and Group Name
			String groupQuery = "SELECT id FROM " + schema
					+ ".group WHERE status = 1 AND version_id = ? AND group_name LIKE ?";
			int groupId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(groupQuery)) {
				pstmt.setInt(1, activeVersionId);
				pstmt.setString(2, "%" + groupName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						groupId = rs.getInt("id");
						System.out.println("Group ID: " + groupId);
					}
				}
			}

			// Step 3: Get Emirate ID based on Group ID and Emirate Name
			String emirateQuery = "SELECT id FROM " + schema + ".emirate WHERE group_id = ? AND emirate_name LIKE ?";
			int emirateId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(emirateQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setString(2, "%" + emirateName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						emirateId = rs.getInt("id");
						System.out.println("Emirate ID: " + emirateId);
					}
				}
			}

			// Step 4: Get TPA ID based on Group ID, Emirate ID, and TPA Name
			String tpaQuery = "SELECT id FROM " + schema
					+ ".tpa WHERE group_id = ? AND emirate_id = ? AND tpa_name LIKE ?";
			int tpaId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(tpaQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setInt(2, emirateId);
				pstmt.setString(3, "%" + tpaName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						tpaId = rs.getInt("id");
						System.out.println("TPA ID: " + tpaId);
					}
				}
			}

			// Step 5: Get Plan ID based on TPA ID and Plan Name
			String planQuery = "SELECT id FROM " + schema + ".plan WHERE tpa_id = ? AND Plan_name LIKE ?";
			int planId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(planQuery)) {
				pstmt.setInt(1, tpaId);
				pstmt.setString(2, "%" + planName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						planId = rs.getInt("id");
						System.out.println("Plan ID: " + planId);
					}
				}
			}

			// Step 6: Fetch Premium Details with correctly inserted Plan ID
			String premiumQuery = "SELECT * FROM " + schema + ".premium WHERE plan_id = " + planId + " AND status = 1";
			System.out.println("Final Premium Query: " + premiumQuery);

			return premiumQuery;
		}
	}

	public static String benefitsQuery(String schema, String clientReferenceNumber) {
		// Construct the query with dynamic schema
		String benefitsQuery = "SELECT * FROM " + schema + ".benefits_table " + "WHERE client_reference_number LIKE '%"
				+ clientReferenceNumber.trim() + "%'";

		try (Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {

			return benefitsQuery.replaceAll(" /1", ""); // Return the clean SQL query string

		} catch (SQLException e) {
			e.printStackTrace();
			return "Error while generating benefits query: " + e.getMessage();
		}
	}

	// nationality loading
	public static String nationalityLoadingQuery(String schema, String groupName, String emirate, String tpa) {

		String nationalityLoadingsQuery = null;

		// Variables to hold user inputs
		String emirateName, tpaName;

		try (Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
			// Assigning input values
			emirateName = emirate;
			tpaName = tpa;

			// Step 1: Get Active Version ID
			String activeVersionQuery = "WITH ActiveVersion AS (\r\n" + " SELECT pv.id\r\n" + " FROM " + schema
					+ ".product_versions pv\r\n" + " WHERE pv.status = 1 AND pv.effective_date <= CURDATE()\r\n"
					+ " ORDER BY pv.effective_date DESC\r\n" + " LIMIT 1\r\n" + ") SELECT id FROM ActiveVersion;";

			int activeVersionId = 0;
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(activeVersionQuery)) {
				if (rs.next()) {
					activeVersionId = rs.getInt("id");
				}
			}

			// Step 2: Get Group ID based on Active Version and Group Name
			String groupQuery = "SELECT id FROM " + schema
					+ ".group WHERE status = 1 AND version_id = ? AND group_name LIKE ?";
			int groupId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(groupQuery)) {
				pstmt.setInt(1, activeVersionId);
				pstmt.setString(2, "%" + groupName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						groupId = rs.getInt("id");

					}
				}
			}

			// Step 3: Get Emirate ID based on Group ID and Emirate Name
			String emirateQuery = "SELECT id FROM " + schema + ".emirate WHERE group_id = ? AND emirate_name LIKE ?";
			int emirateId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(emirateQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setString(2, "%" + emirateName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						emirateId = rs.getInt("id");
					}
				}
			}

			// Step 4: Get UW Rules Schema Name based on Group ID, Emirate ID, and TPA Name
			String uwRulesQuery = "SELECT uw_rules_schema_name FROM " + schema + ".tpa "
					+ "WHERE group_id = ? AND emirate_id = ? AND tpa_name LIKE ?";

			String uwRulesSchemaName = "";
			try (PreparedStatement pstmt = connection.prepareStatement(uwRulesQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setInt(2, emirateId);
				pstmt.setString(3, "%" + tpaName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						uwRulesSchemaName = rs.getString("uw_rules_schema_name");
					}
				}
			}

			// Step 5: Generate the updated Nationality Loadings query
			if (!uwRulesSchemaName.isEmpty()) {
				nationalityLoadingsQuery = "SELECT ng.group_name, n.nationality, ng.loading_discount " + "FROM "
						+ uwRulesSchemaName + ".nationality_group_mapping gm " + "LEFT JOIN " + uwRulesSchemaName
						+ ".nationality_group ng " + "ON ng.nationality_group_id = gm.nationality_group_id "
						+ "LEFT JOIN " + uwRulesSchemaName + ".nationality n "
						+ "ON n.nationality_id = gm.nationality_id " + "WHERE gm.version_id = " + activeVersionId + ";";

				System.out.println(nationalityLoadingsQuery);
			} else {
				System.out.println("No UW Rules Schema Name found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nationalityLoadingsQuery;
	}

	// Industry Loading Query
	public static String industryLoadingQuery(String schema, String groupName, String emirate, String tpa) {

		// Variables to hold user inputs
		String emirateName, tpaName;
		String industryLoadingsQuery = null;

		try (Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
			// Assign input values
			emirateName = emirate;
			tpaName = tpa;

			// Step 1: Get Active Version ID
			String activeVersionQuery = "WITH ActiveVersion AS (" + "SELECT pv.id " + "FROM " + schema
					+ ".product_versions pv " + "WHERE pv.status = 1 AND pv.effective_date <= CURDATE() "
					+ "ORDER BY pv.effective_date DESC " + "LIMIT 1) " + "SELECT id FROM ActiveVersion;";

			int activeVersionId = 0;
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(activeVersionQuery)) {
				if (rs.next()) {
					activeVersionId = rs.getInt("id");
				}
			}

			// Step 2: Get Group ID based on Active Version and Group Name
			String groupQuery = "SELECT id FROM " + schema
					+ ".group WHERE status = 1 AND version_id = ? AND group_name LIKE ?";
			int groupId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(groupQuery)) {
				pstmt.setInt(1, activeVersionId);
				pstmt.setString(2, "%" + groupName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						groupId = rs.getInt("id");

					}
				}
			}

			// Step 3: Get Emirate ID based on Group ID and Emirate Name
			String emirateQuery = "SELECT id FROM " + schema + ".emirate WHERE group_id = ? AND emirate_name LIKE ?";
			int emirateId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(emirateQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setString(2, "%" + emirateName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						emirateId = rs.getInt("id");
					}
				}
			}

			// Step 4: Get UW Rules Schema Name based on Group ID, Emirate ID, and TPA Name
			String uwRulesQuery = "SELECT uw_rules_schema_name " + "FROM " + schema + ".tpa "
					+ "WHERE group_id = ? AND emirate_id = ? AND tpa_name LIKE ?";

			String uwRulesSchemaName = "";
			try (PreparedStatement pstmt = connection.prepareStatement(uwRulesQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setInt(2, emirateId);
				pstmt.setString(3, "%" + tpaName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						uwRulesSchemaName = rs.getString("uw_rules_schema_name");
					}
				}
			}

			// Step 5: Generate the updated Industry Loadings query
			if (!uwRulesSchemaName.isEmpty()) {
				industryLoadingsQuery = String.format(
						"SELECT im.industry_name, ig.loading_discount " + "FROM %s.industry_group_mapping igm "
								+ "LEFT JOIN %s.industry_group ig ON ig.industry_group_id = igm.industry_group_id "
								+ "LEFT JOIN %s.industry_master im ON im.industry_master_id = igm.industry_master_id;",
						uwRulesSchemaName, uwRulesSchemaName, uwRulesSchemaName);
			} else {
				System.out.println("No UW Rules Schema Name found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return industryLoadingsQuery;
	}

	public static String previousInsurerLoadingQuery(String schema, String groupName, String emirate, String tpa) {

		// Variables to hold user inputs
		String emirateName, tpaName;
		String previousInsurerLoadingsQuery = null;

		try (Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
			// Assign input values
			emirateName = emirate;
			tpaName = tpa;

			// Step 1: Get Active Version ID
			String activeVersionQuery = "WITH ActiveVersion AS (" + "SELECT pv.id " + "FROM " + schema
					+ ".product_versions pv " + "WHERE pv.status = 1 AND pv.effective_date <= CURDATE() "
					+ "ORDER BY pv.effective_date DESC " + "LIMIT 1) " + "SELECT id FROM ActiveVersion;";

			int activeVersionId = 0;
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(activeVersionQuery)) {
				if (rs.next()) {
					activeVersionId = rs.getInt("id");
				}
			}

			// Step 2: Get Group ID based on Active Version and Group Name
			String groupQuery = "SELECT id FROM " + schema
					+ ".group WHERE status = 1 AND version_id = ? AND group_name LIKE ?";
			int groupId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(groupQuery)) {
				pstmt.setInt(1, activeVersionId);
				pstmt.setString(2, "%" + groupName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						groupId = rs.getInt("id");

					}
				}
			}

			// Step 3: Get Emirate ID based on Group ID and Emirate Name
			String emirateQuery = "SELECT id FROM " + schema + ".emirate WHERE group_id = ? AND emirate_name LIKE ?";
			int emirateId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(emirateQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setString(2, "%" + emirateName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						emirateId = rs.getInt("id");
					}
				}
			}

			// Step 4: Get UW Rules Schema Name based on Group ID, Emirate ID, and TPA Name
			String uwRulesQuery = "SELECT uw_rules_schema_name " + "FROM " + schema + ".tpa "
					+ "WHERE group_id = ? AND emirate_id = ? AND tpa_name LIKE ?";

			String uwRulesSchemaName = "";
			try (PreparedStatement pstmt = connection.prepareStatement(uwRulesQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setInt(2, emirateId);
				pstmt.setString(3, "%" + tpaName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						uwRulesSchemaName = rs.getString("uw_rules_schema_name");
					}
				}
			}

			// Step 5: Generate the updated Previous Insurer Loadings query
			if (!uwRulesSchemaName.isEmpty()) {
				previousInsurerLoadingsQuery = String.format(
						"SELECT pg.group_name, pim.previous_insurer_name, pg.loading_discount "
								+ "FROM %s.previous_insurer_group_mapping pgm "
								+ "LEFT JOIN %s.previous_insurer_group pg ON pg.previous_insurer_group_id = pgm.previous_insurer_group_id "
								+ "LEFT JOIN %s.previous_insurer_master pim ON pim.previous_insurer_master_id = pgm.previous_insurer_master_id;",
						uwRulesSchemaName, uwRulesSchemaName, uwRulesSchemaName);
			} else {
				System.out.println("No UW Rules Schema Name found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return previousInsurerLoadingsQuery;
	}

	public static String commissionQuery(String schema, String groupName, String emirate, String tpa, String plan)
			throws Exception {

		// Variables to hold user inputs
		String emirateName, tpaName, planName;
		String commissionQuery = null;

		try (Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
			// Assign input values
			emirateName = emirate;
			tpaName = tpa;
			planName = plan;

			// Step 1: Get Active Version ID
			String activeVersionQuery = "WITH ActiveVersion AS (" + "SELECT pv.id " + "FROM " + schema
					+ ".product_versions pv " + "WHERE pv.status = 1 AND pv.effective_date <= CURDATE() "
					+ "ORDER BY pv.effective_date DESC " + "LIMIT 1) " + "SELECT id FROM ActiveVersion;";

			int activeVersionId = 0;
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(activeVersionQuery)) {
				if (rs.next()) {
					activeVersionId = rs.getInt("id");
				}
			}

			// Step 2: Get Group ID based on Active Version and Group Name
			String groupQuery = "SELECT id FROM " + schema
					+ ".group WHERE status = 1 AND version_id = ? AND group_name LIKE ?";
			int groupId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(groupQuery)) {
				pstmt.setInt(1, activeVersionId);
				pstmt.setString(2, "%" + groupName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						groupId = rs.getInt("id");

					}
				}
			}

			// Step 3: Get Emirate ID based on Group ID and Emirate Name
			String emirateQuery = "SELECT id FROM " + schema + ".emirate WHERE group_id = ? AND emirate_name LIKE ?";
			int emirateId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(emirateQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setString(2, "%" + emirateName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						emirateId = rs.getInt("id");
					}
				}
			}

			// Step 4: Get TPA ID based on Group ID, Emirate ID, and TPA Name
			String tpaQuery = "SELECT id FROM " + schema
					+ ".tpa WHERE group_id = ? AND emirate_id = ? AND tpa_name LIKE ?";
			int tpaId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(tpaQuery)) {
				pstmt.setInt(1, groupId);
				pstmt.setInt(2, emirateId);
				pstmt.setString(3, "%" + tpaName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						tpaId = rs.getInt("id");
					}
				}
			}

			// Step 5: Get Plan ID based on TPA ID and Plan Name
			String planQuery = "SELECT id FROM " + schema + ".plan WHERE tpa_id = ? AND plan_name LIKE ?";
			int planId = 0;
			try (PreparedStatement pstmt = connection.prepareStatement(planQuery)) {
				pstmt.setInt(1, tpaId);
				pstmt.setString(2, "%" + planName + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						planId = rs.getInt("id");
					}
				}
			}

			// Step 6: Fetch Commission
			if (planId > 0) {
				commissionQuery = String.format(
						"SELECT insurer_fee, tpa_fee, aura_commission, distributor_commission, member_type, total "
								+ "FROM %s.ceding_commission WHERE plan_id = %d;",
						schema, planId);
			} else {
				System.out.println("No Plan ID found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return commissionQuery;
	}

	public static void newExcelOverride(String basepremium, String benefits, String nationality, String industry,
			String previousInsurer, String commission) {

		// Queries to execute
		String[] queries = { basepremium, benefits, nationality, industry, previousInsurer, commission };

		// Excel file path
		String excelFilePath = System.getProperty("user.dir")
				+ "\\target\\ExcelCalculatorForDistributor\\Premium Calculator - RAK.xlsx";

		Connection connection = null;

		try {
			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection
			connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

			// Load the Excel workbook
			FileInputStream fileInputStream = new FileInputStream(excelFilePath);
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);

			// Remove all rows in the sheet
			int rowCount = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					sheet.removeRow(row); // Removes the row from the sheet
				}
			}

			int startColumn = 0; // Start writing at column A

			// Execute each query and write results with column gaps
			for (String query : queries) {
				startColumn = writeQueryToSheet(connection, query, workbook, sheet, startColumn);
				startColumn += 2; // Leave one empty column as a gap
			}

			// Recalculate formulas
			workbook.setForceFormulaRecalculation(true);

			// Save the workbook
			try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
				workbook.write(outputStream);
			}

			fileInputStream.close();
			workbook.close();
			System.out.println("Data successfully written to Excel sheet with column gaps.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close database connection
			try {
				if (connection != null) {
					connection.close();
					System.out.println("Database connection closed.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private static int writeQueryToSheet(Connection connection, String query, Workbook workbook, Sheet sheet,
			int startColumn) {
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			int rowCount = 0;

			// Write headers in the first row
			Row headerRow = sheet.getRow(rowCount);
			if (headerRow == null) {
				headerRow = sheet.createRow(rowCount);
			}
			for (int i = 0; i < columnCount; i++) {
				Cell cell = headerRow.createCell(startColumn + i);
				cell.setCellValue(metaData.getColumnName(i + 1));
			}
			rowCount++;

			// Write data
			while (resultSet.next()) {
				Row row = sheet.getRow(rowCount);
				if (row == null) {
					row = sheet.createRow(rowCount);
				}
				for (int i = 0; i < columnCount; i++) {
					Cell cell = row.createCell(startColumn + i);
					Object value = resultSet.getObject(i + 1);

					if (value instanceof String) {
						cell.setCellValue((String) value);
					} else if (value instanceof Number) {
						cell.setCellValue(((Number) value).doubleValue());
					} else if (value instanceof Boolean) {
						cell.setCellValue((Boolean) value);
					} else if (value instanceof Date) {
						cell.setCellValue(value.toString());
					} else {
						cell.setCellValue(value != null ? value.toString() : "");
					}
				}
				rowCount++;
			}

			System.out.println(
					"Data from query written in columns " + (startColumn + 1) + " to " + (startColumn + columnCount));
			return startColumn + columnCount;

		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
			return startColumn;
		}
	}

	public static double getCalculatedDataCell() throws IOException {
		try (FileInputStream file = new FileInputStream(new File(System.getProperty("user.dir")
				+ "\\target\\ExcelCalculatorForDistributor\\Premium Calculator - RAK.xlsx"));
				Workbook workbook = new XSSFWorkbook(file)) {

			Sheet sheet = workbook.getSheet("Updated Premium Calc");
			Row row = sheet.getRow(7);
			Cell cell = row.getCell(48);

			double calculatedValue = cell.getNumericCellValue();
			return calculatedValue;
		}

	}

	public static void validateCalculatedValue(double calculatedValue, String expectedValue, double tolerance) {

		// Round the calculated value
		BigDecimal roundedValue = new BigDecimal(calculatedValue).setScale(2, RoundingMode.HALF_UP);

		// Format the rounded value as per currency format
		String formattedValue = "AED " + String.format("%,.2f", roundedValue);

		double expectedNumeric = Double.parseDouble(expectedValue.replace("AED ", "").replace(",", ""));
		double actualNumeric = roundedValue.doubleValue();

		// verify the difference is within the allowed tolerance
		if (Math.abs(expectedNumeric - actualNumeric) <= tolerance) {
			System.out.println("Test Passed! Expected: " + expectedValue + ", Actual: " + formattedValue);
		} else {
			Assert.fail("Test Failed! Values do not match within the allowed tolerance! Expected: " + expectedValue
					+ ", but got: " + formattedValue);
		}
	}

	public static void writeDropDownDataToExcel(List<String> dropdownValues) {

		FileInputStream fileInputStream;
		Workbook workbook;
		String filePath = System.getProperty("user.dir")
				+ "\\target\\ExcelCalculatorForDistributor\\Premium Calculator - RAK.xlsx";
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("Excel file does not exist. Please provide a valid file.");
				return; // Exit if file doesn't exist
			}

			fileInputStream = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fileInputStream);
			fileInputStream.close();

			// Get "Sheet 2"
			Sheet sheet = workbook.getSheet("Sheet2");
			if (sheet == null) {
				System.out.println("Sheet 2 does not exist. Please provide a valid sheet.");
				return; // Exit if sheet doesn't exist
			}

			// Clear all rows by creating new ones
			int totalRows = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < totalRows; i++) {
				sheet.createRow(i); // Instead of getting a row, create a new row
			}

			// Create a new row at index 0
			Row row = sheet.createRow(0);
			for (int i = 0; i < dropdownValues.size(); i++) {
				row.createCell(i).setCellValue(dropdownValues.get(i));
			}

			// Save the updated Excel file
			FileOutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();

			System.out.println("Data successfully written to Sheet 2 in: " + filePath);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}