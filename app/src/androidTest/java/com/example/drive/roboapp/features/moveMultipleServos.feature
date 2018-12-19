Feature: Move Multiple Servos

  Scenario: Control a multiple servos on a robot arm at once

  As a user I should be able to move multiple servos of an online and available robot arm

    Given I am in the Robot Arm Control View
    And a robot arm is online
    When I have selected an available robot arm
    And filled in all desired servo values
    When I click the 'send' button
    Then The application should move the arm by sending a message
