Feature: To Validate the create quote feature of distributor SME application

  Scenario: To validate the qoute creation SME distributor
    Given User must be in the login page
    When User enter valid emailId and password
    And User click login button
    Then User must be in the home page
    Given User must be in the create qoute page 
    When User enter company name
    And User enter trade licence number
    And User enter email id
    And User enter mobile number
    And User select industry categories
    And User click company details next button 
    And User select start date and end date in census 
    And User select number of categories
    And User enter distributor commission 
    And User enter sales agent
    And User click census next button
    And User should choose group 
    And User should select emirates category 
    And User should select TPA category 
    And User should select plan category 
    And User should click and upload template
    And User click proceed
    And User click next button
    Then User must displayed with quote created popup message
    Then User should validate the total premium for the created qoute
    
    
    
