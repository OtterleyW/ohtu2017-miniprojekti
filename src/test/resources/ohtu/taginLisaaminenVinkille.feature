Feature: user can add tag

  Scenario: user add tag
    Given user has selected Lisaa tagi
    When button Lisaa tagi is selected and user has entered tag "omena"
    Then user is redirect to mainpage

  Scenario: user try add empty tag
    Given user has selected Lisaa tagi
    When button Lisaa tagi is selected
    Then user is redirect to mainpage

  Scenario: user suspend action to add tag
    Given user has selected Lisaa tagi
    When user select link text to Vinkkikirjasto
    Then user is redirect to mainpage
