@ApiTesting @integration_api
Feature: Gets Methods VideoGame

  Scenario: Get VideoGames from endpoint
    Given I do a GET in /app/videogames
    Then I print the api Response
    And I validate status code is 200
