@ApiTesting @integration_api
Feature: Post Methods Pets

  Scenario: POST - Create a new Pet
    Given I do a POST in /pet using body /BodyPetStore/Post_Pet.json
    Then I print the api Response
    And I assert Response values
      | id            | NOT NULL    |
      | category.name | Custom Cat  |
      | name          | Custom name |
      | status        | available   |
    Then I save the response key id as id_petId
    And I validate status code is 200
    Given I do a GET in /pet/$id_petId
    Then I print the api Response
    And I validate status code is 200

  Scenario: PUT - Change pet data
    Given I do a PUT in /pet using body /BodyPetStore/Put_Pet.json
      | name           | Little Pony     |
      | customCat      | Mythical horses |
    Then I print the api Response
    And I validate status code is 200
    And I assert Response values
      | id            | NOT NULL          |
      | id            | IS A NUMBER       |
      | category.name | Mythical horses   |
      | name          | Little Pony       |
      | status        | available         |

  Scenario: DELETE - Wipe pet data
    Given I do a DELETE on pet/$id_petId
    Then I print the api Response
    And I validate status code is 200
    And I assert entity message is $id_petId