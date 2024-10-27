@ApiTesting @integration_api
Feature: Gets Methods Booker

  Scenario: Get Booker server status
    Given I do a GET in /ping
    Then I print the api Response
    And I validate status code is 201

  Scenario: POST - Create a new book
    Given I do a POST in /booking using body /BodyBooker/CreateBooking.json
    Then I print the api Response
    Then I validate the Response Schema using body /BodyBooker/schemas/CreateBookingSchema.json
    And I assert Response values
      | bookingid                   | NOT NULL           |
      | booking.firstname           | Tom Sawyer         |
      | booking.lastname            | Diaz Ortiz         |
      | booking.totalprice          | 60000              |
      | booking.additionalneeds     | No requierements   |
    Then I save the response key bookingid as bookingid
    And I validate status code is 200
    Given I do a GET in /booking/$bookingid
    Then I print the api Response
    And I validate status code is 200


  Scenario: PUT - Change book data
    Given I make a PUT in /booking/$bookingid using body /BodyBooker/UpdateBooking.json
    Then I print the api Response
    And I validate status code is 200
    And I assert Response values
      | firstname           | James Jose         |
      | lastname            | Diaz Periñon       |
      | totalprice          | 6000               |
      | additionalneeds     | For Free           |