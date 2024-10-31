Feature: API Client Managemen


  Scenario Outline: Create a new API client and submit an order
    Given I have a new API client request ClientName "esraa" and ClientEmail "esraa@test.com"

    When I send a POST request to create an API client
    Then the response status code should be 201
    And I store the access token from the response

    When I send a GET request to fetch "<Type>" books
    Then the response status code should be 200
    And the response should contain a list of "<Type>" books
    Then I store the book ID
    When I send a GET request to fetch book with ID
    Then the response status code should be 200
    And the response should contain details of the requested book

    When I submit an order for the stored book ID with customer name "Salvador Windler"
    Then the response status code should be 201

    When I submit an order for the stored book ID with customer name "Salvador Windler 2 "
    Then the response status code should be 201

##      assert customerName,createdBy, and get id
    When I send a GET request to get all orders
    Then the response status code should be 200
    And the response should contain orders details
    Then I store the first order id

    Then I send a GET request to get order with selected id
    And the response should contain orders details


    When I send a "Patch" request to "update" the order
    Then the response status code should be 204

    When I send a "Delete" request to "Delete" the order
    Then the response status code should be 204


    Examples:
      |Type|
      |fiction|
      |non-fiction|
