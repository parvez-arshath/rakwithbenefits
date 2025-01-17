package org.sme.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import net.masterthought.cucumber.json.support.Durationable;

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

	/*
	 * public static void dateAndTime() { Date d = new Date();
	 * System.out.println(d);
	 * 
	 * }
	 */
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
				"C:\\Users\\impelox-pc-048\\Downloads\\Sme\\Sme\\target\\DatasForDistributor\\LoginData.properties");
		Properties props = new Properties();
		props.load(reader);
		return props;

	}

	public static Properties createQuoteData() throws IOException {
		FileReader reader = new FileReader(
				"C:\\Users\\impelox-pc-048\\Downloads\\Sme\\Sme\\target\\DatasForDistributor\\createQuote.properties");
		Properties props = new Properties();
		props.load(reader);
		return props;

	}

	/*
	 * public static void jsSendKeys(WebElement args) { JavascriptExecutor js =
	 * (JavascriptExecutor) driver;
	 * js.executeScript("arguments[0].setAttribute('value','')", args);
	 * 
	 * 
	 * }
	 */
	public static void selectDropDownData(WebElement element, String value) {
		Select s = new Select(element);
		s.selectByValue(value);
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

		LocalDate tommrowDate = currentDate.plusDays(1);
		String formattedDate = tommrowDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		return formattedDate;

	}

	public static Properties calculatorData() throws IOException {
		FileReader reader = new FileReader(
				"C:\\Users\\impelox-pc-048\\eclipse-workspace\\SmeSingleCategory\\target\\DatasForDistributor\\DataForCalculator.properties");
		Properties props = new Properties();
		props.load(reader);
		return props;

	}

	public static void fetchDataFromDatabase(String dbURL, String dbUsername, String dbPassword, String query,
			String excelFilePath, int sheetNum) throws SQLException {
		Connection connection = null;

		try {
			// Connect to the database
			connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			// Open the Excel file
			FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(sheetNum); // Modify index if needed

			// Remove existing rows except the first row
			int lastRow = sheet.getLastRowNum();
			for (int i = 1; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
			}

			// Write new data to the Excel sheet
			int rowCount = 1; // Start writing from the second row

			while (resultSet.next()) {
				Row row = sheet.getRow(rowCount);
				if (row == null) {
					row = sheet.createRow(rowCount); // Create a new row if it doesn't exist
				}

				// Loop through each column and write data
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					// Stop writing after column 'O' (15th column, index 14)
					if (i > 15) {
						break;
					}

					Cell cell = row.getCell(i - 1);
					if (cell == null) {
						cell = row.createCell(i - 1);
					}

					// Preserve formulas: Skip overwriting if the cell is a formula
					if (cell.getCellType() == CellType.FORMULA) {
						continue; // Skip cells with formulas
					}

					// Set new value from the database
					cell.setCellValue(resultSet.getString(i));
					System.out.println(cell.getStringCellValue());

				}

				rowCount++; // Move to the next row
			}

			// Close the input stream
			fileInputStream.close();

			// Save the changes to the Excel file
			FileOutputStream fileOutputStream = new FileOutputStream(new File(excelFilePath));
			workbook.write(fileOutputStream);
			fileOutputStream.close();

			// Close workbook
			workbook.close();

			System.out.println("Data has been overwritten in the Excel file successfully.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void toFetchCensusSheet(String sourceFilePath, String targetFilePath, int sourceSht, int targetSht) {
		/*
		 * String sourceFilePath =
		 * "C:\\Users\\impelox-pc-048\\Desktop\\New folder\\census_ab_automation.xlsx";
		 * // Source // Excel // file String targetFilePath =
		 * "C:\\Users\\impelox-pc-048\\eclipse-workspace\\CalculationSME\\target\\Arshad AIAW.xlsx"
		 * ; // Target // Excel // file
		 */
		try {
// Open the source Excel file
			FileInputStream sourceFile = new FileInputStream(new File(sourceFilePath));
			Workbook sourceWorkbook = new XSSFWorkbook(sourceFile);
			Sheet sourceSheet = sourceWorkbook.getSheetAt(sourceSht); // Source sheet

// Open the target Excel file
			FileInputStream targetFile = new FileInputStream(new File(targetFilePath));
			Workbook targetWorkbook = new XSSFWorkbook(targetFile);
			Sheet targetSheet = targetWorkbook.getSheetAt(targetSht); // Target sheet (modify index as needed)

// Remove existing rows in the target sheet, except the first row
			int lastRow = targetSheet.getLastRowNum();
			for (int i = 1; i <= lastRow; i++) {
				Row row = targetSheet.getRow(i);
				if (row != null) {
					targetSheet.removeRow(row);
				}
			}

// Copy data from source sheet to target sheet
			int rowCount = 1; // Start from the second row to preserve headers

			for (int i = 1; i <= sourceSheet.getLastRowNum(); i++) { // Skip the first row
				Row sourceRow = sourceSheet.getRow(i);
				Row targetRow = targetSheet.createRow(rowCount);

				if (sourceRow != null) {
					for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
						Cell sourceCell = sourceRow.getCell(j);
						Cell targetCell = targetRow.createCell(j);

						if (sourceCell != null) {
// Preserve formulas: Skip overwriting if the cell is a formula
							if (targetCell.getCellType() == CellType.FORMULA) {
								continue;
							}

// Copy the cell value based on the type
							switch (sourceCell.getCellType()) {
							case STRING:
								targetCell.setCellValue(sourceCell.getStringCellValue());
								break;
							case NUMERIC:
								if (DateUtil.isCellDateFormatted(sourceCell)) {
									// Format the date as dd-MM-yyyy
									Date date = sourceCell.getDateCellValue();
									SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
									targetCell.setCellValue(dateFormat.format(date));
								} else {
									targetCell.setCellValue(sourceCell.getNumericCellValue());
								}
								break;

							case BOOLEAN:
								targetCell.setCellValue(sourceCell.getBooleanCellValue());
								break;
							case FORMULA:
								targetCell.setCellFormula(sourceCell.getCellFormula());
								break;
							case BLANK:
								targetCell.setBlank();
								break;
							default:
								break;
							}

						}
					}
				}
				rowCount++;
			}

// Close the input files
			sourceFile.close();
			targetFile.close();

// Save the changes to the target Excel file
			FileOutputStream outputStream = new FileOutputStream(new File(targetFilePath));
			targetWorkbook.write(outputStream);
			outputStream.close();

// Close the workbooks
			sourceWorkbook.close();
			targetWorkbook.close();

			System.out.println("Data copied from source Excel to target Excel successfully.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String basePremium(int planId, int status) {
		// Base Premium
		String premiumQuery = String.format(
				"SELECT * FROM 7003_group_medical_aiaw_transactions.premium WHERE plan_id=%d AND status=%d;", planId,
				status);
		return premiumQuery;

	}

}