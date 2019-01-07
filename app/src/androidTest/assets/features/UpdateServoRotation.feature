Feature: UpdateServoRotation
  Update the database when rotation is inputted

  @updateServoRotation-feature
  Scenario Outline: Input rotation in valid format in all fields, and send via 'send all'-button
    Given I am in the robot controller activity
    When I input rotation "<rotation>" in all fields
    And I press the 'send all' button
    Then I should see a successful toast

    Examples:
      | rotation|
      | 0       |
      | 180     |
      |-1       |
      | 181     |
      | 90      |

  @updateServoRotation-feature
  Scenario: Send via 'send all' button, without inputting rotation in fields
    Given I am in the robot controller activity
    When No value is in at least one of the rotation fields
    And I press the 'send all' button
    Then I should see an error toast

  @updateServoRotation-feature
  Scenario Outline: Input rotation in a valid format in the foot joints field, a mix of valid and invalid in the other joints fields and send via the foot joints send button
    Given I am in the robot controller activity
    When I input rotation "<footRotation>" in the field of foot joint
    And I input rotation "<restRotation>" in the fields of the other joints
    And I press the 'foot' button
    Then I should see a successful toast saying 'updated successfully'

    Examples:
      | footRotation | restRotation |
      | 90           |              |
      | 180          | 0            |
      |-1            | 181          |
      | 181          | 180          |
      | 0            | -1           |

  @updateServoRotation-feature
  Scenario: Send via the 'foot' button, without inputting rotation in the foot joints field
    Given I am in the robot controller activity
    When No value is in the foot joints rotation fields
    And I press the 'foot' button
    Then I should see an error toast saying 'please add a number'