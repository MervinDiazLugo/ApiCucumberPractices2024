@ApiTesting @integration_api
Feature: Gets Methods Booker

  Scenario: Get Booker server status
    Given I do a GET in /ping
    Then I print the api Response
    And I validate status code is 201
