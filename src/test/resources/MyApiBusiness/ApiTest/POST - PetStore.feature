@ApiTesting @integration_api
Feature: Post Methods Pets

  Scenario: POST - Create a new Pet
    #Given I Save in scenario data following customCat and Fantasy Category
    Given I do a POST in /pet using body /BodyPetStore/bodies/Post_Pet.json
    Then I print the api Response
    Then I save the response key id as id_petId
    And I validate status code is 200
    Given I do a GET in /pet/$id_petId
    Then I print the api Response
    And I validate status code is 200