@ApiTesting @integration_api
Feature: Post Methods Pets

  Scenario: POST - Create a new Pet
    Given I do a POST in /pet using body /BodyPetStore/bodies/Post_Pet.json
    Then I print the api Response
    Then I save the response key id as id_petId
    And I validate status code is 200
    Given I do a GET in /pet/$id_petId
    Then I print the api Response
    And I validate status code is 200

  Scenario: PUT - Change pet data
    Given I do a PUT in /pet using body /BodyPetStore/bodies/Put_Pet.json
      | name           | Little Pony     |
      | customCat      | Mythical horses |
    Then I print the api Response
    And I validate status code is 200

  Scenario: DELETE - Wipe pet data
    Given I do a DELETE on pet/$id_petId
    Then I print the api Response
    And I validate status code is 200
    And I assert entity message is $id_petId