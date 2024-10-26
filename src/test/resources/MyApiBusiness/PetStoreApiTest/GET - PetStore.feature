@ApiTesting @integration_api
Feature: Gets Methods Pets

  Scenario: Get pet By Status available
    Given I do a GET in /pet/findByStatus?status=available
    Then I print the api Response
    And I validate status code is 200

  Scenario: Get pet By Status sold
    Given I do a GET in /pet/findByStatus?status=sold
    Then I print the api Response
    And I validate status code is 200

  Scenario: Get pet By Status Pending
    Given I do a GET in /pet/findByStatus?status=pending
    Then I print the api Response
    And I validate status code is 200

  Scenario Outline: Get pet By Statuses
    Given I do a GET in /pet/findByStatus?status=<status>
    Then I print the api Response
    And I validate status code is 200
    Examples:
      | status    |
      | available |
      | sold      |
      | pending   |