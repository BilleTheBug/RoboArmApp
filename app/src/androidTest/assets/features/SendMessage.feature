Feature: SendMessage
  Send a message to the robot

  Scenario: Input email and password in correct format
    Given I am in the main activity
    When I select robo arm one
    And I write numbers in all fields
    Then I should see a toast with text success