package org.sme.pojo;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.sme.utilities.BaseClass;

public class DistributorLoginPojo extends BaseClass {

	public DistributorLoginPojo() {
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//label[text()='Email Id']//parent::div//input[@type='text']")
	private WebElement userEmailDistributor;

	@FindBy(xpath = "//label[text()='Password']//parent::div//input[@type='text']")
	private WebElement userPasswordDistributor;

	@FindBy(xpath = "//button[@type='submit' and text()='Login']")
	private WebElement userLoginDistributor;


	public WebElement getUserEmailDistributor() {
		return userEmailDistributor;
	}

	public WebElement getUserPasswordDistributor() {
		return userPasswordDistributor;
	}

	public WebElement getUserLoginDistributor() {
		return userLoginDistributor;
	}

}
