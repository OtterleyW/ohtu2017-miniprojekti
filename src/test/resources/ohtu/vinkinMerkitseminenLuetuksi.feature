Feature: user can set hint as read or unread

  Scenario: user can set unread hint as read
    Given command merkitseluetuksi is selected
    Then the hint is marked as read

  Scenario: user can set read hint as unread
    Given command merkitselukemattomaksi is selected
    Then the hint is marked as unread