Feature: user can delete a podcast

  Scenario: user successfully deletes a podcast
    Given command poistapodcast is selected
    When user clicks element poistapodcast
    Then the podcast is deleted
