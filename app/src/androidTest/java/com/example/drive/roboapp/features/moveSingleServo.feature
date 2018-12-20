Feature: Move Single Servo

  As a user I should be able to move a single servo of an online and available robot arm

  Scenario: Move a singe servo on a robot arm

    Given I am in the Robot Arm Control View
    And a robot arm is online
    When I have selected an available robot arm
    And filled in a value of the desired servo to rotate
    When I click the 'send' button
    Then the application should move the arm by sending a message