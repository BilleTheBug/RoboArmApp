Feature: UpdateServoRotation
  Update the database when rotation is inputted

  @updateServoRotation-feature
  Scenario Outline: Input rotation in valid format
    Given I am in the robot controller activity
    When I input rotation "<rotation>"
    And I press submit button "<joint>"
    Then I should see a successfull toast

    Examples:
      | rotation| joint   |
      | 0       | foot    |
      | 180     | hand    |
      |-1       | shoulder|
      | 181     | wrist   |
      | 180     | arm     |

  @updateServoRotation-feature
  Scenario: Send without inputting rotation
    Given I am in the robot controller activity
    When No value is in one of the rotation fields
    And I press submit button "<joint>"
    Then I should see an error toast