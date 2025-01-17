package org.sme.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src\\test\\resources\\SmeAllFeatures\\distributor_createquote.feature", glue = "org.sme.code", dryRun = false)
public class SmeTestRunner {

}
