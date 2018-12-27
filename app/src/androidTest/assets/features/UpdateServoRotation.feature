Feature: UpdateServoRotation
  Update the database when rotation is inputted

  @updateServoRotation-feature
  Scenario Outline: Input rotation in a valid format
    Given I am in the robot controller activity
    When I input rotation "<rotation>"
    And I press submit button "<joint>"
    Then I should see a toast with text 'Updated successfully!'

    Examples:
      | rotation| joint|
      | 0       | foot |
      | 180     | hand |