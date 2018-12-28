Feature: UpdateServoRotation
  Update the database when rotation is inputted

  @updateServoRotation-feature
  Scenario Outline: Input rotation in valid and invalid format
    Given I am in the robot controller activity
    When I input rotation "<rotation>"
    And I press submit button "<joint>"
    Then I should see a toast"<expected>"

    Examples:
      | rotation| joint   | expected|
      | 0       | foot    | success |
      | 180     | hand    | success |
      |-1       | shoulder| success |
      | 181     | wrist   | success |
      |         | arm     | fail    |