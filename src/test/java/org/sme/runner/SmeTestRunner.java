package org.sme.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "C:\\Users\\impelox-pc-048\\eclipse-workspace\\SmeSingleCategory\\src\\test\\resources\\SmeAllFeatures\\distributor_createquote.feature", glue = "org.sme.code", dryRun = false)
public class SmeTestRunner {

}
