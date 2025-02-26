package org.smerak.code;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.checkerframework.checker.units.qual.s;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.sme.pojo.CreateQuotePojo;
import org.sme.utilities.BaseClass;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SmeDistributorCreateQuote extends BaseClass {

	CreateQuotePojo createQuoteElements;

	@Given("User must be in the create qoute page")
	public void user_must_be_in_the_create_qoute_page() {
		createQuoteElements = new CreateQuotePojo();
		clickButton(createQuoteElements.getCreateQuoteButton());
		assertTrue(createQuoteElements.getCreateNewQuotePage().getText().contains("Create New Quote"));
	}

	@When("User enter company name")
	public void user_enter_company_name() throws IOException {
		fillTextBox(createQuoteElements.getCompanyNameTextBox(), createQuoteData().getProperty("companyName"));
	}

	@When("User enter trade licence number")
	public void user_enter_trade_licence_number() throws IOException {
		fillTextBox(createQuoteElements.getLicenceNumberTextBox(), createQuoteData().getProperty("tradeLicenceNumber"));
	}

	@When("User enter email id")
	public void user_enter_email_id() throws IOException {

		fillTextBox(createQuoteElements.getEmailTextBox(), createQuoteData().getProperty("email"));
	}

	@When("User enter mobile number")
	public void user_enter_mobile_number() throws IOException {

		selectDropDownData(createQuoteElements.getMobileNumberDropDown(), "50");
		fillTextBox(createQuoteElements.getMobileNumberTextBox(), createQuoteData().getProperty("mobileNumber"));
	}

	@When("User select industry categories")
	public void user_select_industry_categories() {
		selectDropDownData(createQuoteElements.getCategoriesDropDown(), "0");
	}

	@When("User click company details next button")
	public void user_click_company_details_next_button() throws InterruptedException {
		Thread.sleep(3000);
		clickButton(createQuoteElements.getCompanyDetailsNextButton());
	}

	@When("User select start date and end date in census")
	public void user_select_start_date_and_end_date_in_census() throws InterruptedException {
		WebElement censusPolicyStartDate = createQuoteElements.getCensusPolicyStartDate();
		/* clickButton(censusPolicyStartDate); */
		censusPolicyStartDate.clear();
		Thread.sleep(2000);
		String currentDate = currentDate();
		fillTextBox(censusPolicyStartDate, currentDate);
		System.out.println(currentDate);

	}

	@When("User select number of categories")
	public void user_select_number_of_categories() throws InterruptedException {
		Thread.sleep(3000);
		selectDropDownData(createQuoteElements.getNumCategoriesDropDown(), "1");
	}

	@When("User enter distributor commission")
	public void user_enter_distributor_commission() {
		fillTextBox(createQuoteElements.getDistributorCommisionTextBox(), "2");
	}

	@When("User enter sales agent")
	public void user_enter_sales_agent() {
		fillTextBox(createQuoteElements.getSalesAgentTextBox(), "aura");
	}

	@When("User click census next button")
	public void user_click_census_next_button() throws InterruptedException {
		Thread.sleep(2000);
		clickButton(createQuoteElements.getCensusNextButton());

	}

	String group;

	@When("User should choose group")
	public void user_should_choose_group() throws InterruptedException {
		Thread.sleep(2000);
		Select groupDropDownData = selectDropDownData(createQuoteElements.getSelectGroupnameDropDown(), "0");
		group = groupDropDownData.getFirstSelectedOption().getText();
	}

	String emirate;

	@When("User should select emirates category")
	public void user_should_select_emirates_category() {
		Select emirateDropDownData = selectDropDownData(createQuoteElements.getSelectEmiratesDropDownCatA(), "2");
		emirate = emirateDropDownData.getFirstSelectedOption().getText();

	}

	String tpa;

	@When("User should select TPA category")
	public void user_should_select_TPA_category() {

		Select tpaDropDownData = selectDropDownData(createQuoteElements.getSelectTpaCatA(), "0");
		tpa = tpaDropDownData.getFirstSelectedOption().getText();

	}

	String plan;

	@When("User should select plan category")
	public void user_should_select_plan_category() {
		Select planDropDownData = selectDropDownData(createQuoteElements.getSelectPlanCatA(), "0");
		plan = planDropDownData.getFirstSelectedOption().getText();

	}

	@When("User should click and upload template")
	public void user_should_click_and_upload_template() throws InterruptedException {
		Thread.sleep(2000);
		fillTextBox(createQuoteElements.getUploadTemplateId(), "D:\\New Cencus for automation\\cencus_rak.xlsx");
		clickButton(createQuoteElements.getWarningPopupCancelButton());
	}

	@When("User click proceed")
	public void user_click_proceed() throws InterruptedException {

		Thread.sleep(2000);
		jsClick(createQuoteElements.getProceedBtn());
	}

	@When("User select the needed benefits")
	public void user_select_the_needed_benefits() {

		List<String> dropdownValues = Arrays.asList(
				selectDropDownData(createQuoteElements.getSumInsured(), "3").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getTerritorialScopeCoverage(), "2").getFirstSelectedOption()
						.getText(),
				selectDropDownData(createQuoteElements.getRoomType(), "0").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getOpConsultation(), "2").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getPharmacyLimit(), "0").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getPharamacyCoPay(), "0").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getOpServices(), "0").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getPhysiotherapy(), "0").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getOrganTransplant(), "0").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getReturnAirfare(), "0").getFirstSelectedOption().getText(),
				selectDropDownData(createQuoteElements.getSecondMedicalOpinion(), "0").getFirstSelectedOption()
						.getText(),
				selectDropDownData(createQuoteElements.getMaternityOPServices(), "0").getFirstSelectedOption()
						.getText(),
				selectDropDownData(createQuoteElements.getMaternityIPServices(), "0").getFirstSelectedOption()
						.getText(),
				selectDropDownData(createQuoteElements.getNewBornCover(), "0").getFirstSelectedOption().getText());

		writeDropDownDataToExcel(dropdownValues);

	}

	@When("User click next button")
	public void user_click_next_button() throws InterruptedException {
		Thread.sleep(3000);
		jsClick(createQuoteElements.getNextButtonChoosePlan());
	}

	@Then("User must displayed with quote created popup message")
	public void user_must_displayed_with_quote_created_popup_message() {
		System.out.println(createQuoteElements.getQouteCreatedPopUpMessage().getText());
		assertTrue(createQuoteElements.getQouteCreatedPopUpMessage().getText()
				.contains("Successfully created the the quote"));

	}

	String crn;

	@Then("User should validate the total premium for the created qoute")
	public void user_should_validate_the_total_premium_for_the_created_qoute() throws Exception {
		Thread.sleep(3000);
		crn = createQuoteElements.getCustomerId().getText();
		String benefits = benefitsQuery(calculatorData().getProperty("schema"), crn);
		System.out.println(benefits);
		System.out.println(group + "\n" + emirate + "\n" + tpa + "\n" + plan);

		newExcelOverride(basePremiumQuery(calculatorData().getProperty("schema"), group, emirate, tpa, plan), benefits,
				nationalityLoadingQuery(calculatorData().getProperty("schema"), group, emirate, tpa),
				industryLoadingQuery(calculatorData().getProperty("schema"), group, emirate, tpa),
				previousInsurerLoadingQuery(calculatorData().getProperty("schema"), group, emirate, tpa),
				commissionQuery(calculatorData().getProperty("schema"), group, emirate, tpa, plan));

		Thread.sleep(4000);
		createQuoteElements.getQuoteDetails().click();
		String expectedValue = createQuoteElements.getPremiumUiTotal().getText();
		double calculatedValue = getCalculatedDataCell();
		validateCalculatedValue(calculatedValue, expectedValue, 2.5);

	}
}
