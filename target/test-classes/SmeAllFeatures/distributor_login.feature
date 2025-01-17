
Feature: To Validate the login feature of distributor of SME application

  @login
  Scenario: To validate sme distributor login using postive credentials
    Given User must be in the login page
    When User enter valid emailId and password
    And User click login button
    Then User must be in the home page
    
    
   