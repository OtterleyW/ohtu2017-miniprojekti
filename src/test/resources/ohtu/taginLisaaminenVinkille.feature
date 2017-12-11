Feature: user can add tag

  Scenario: user can add tag
    Given user has selected Lisaa tagi for Topologia I
    When button Lisaa tagi is selected and user has entered tag "omena"
    Then Topologia I has new tag "omena"

  Scenario: user try add empty tag
    Given user has selected Lisaa tagi for Topologia I
    When button Lisaa tagi is selected
    Then user is redirect to mainpage

  Scenario: user suspend action to add tag
    Given user has selected Lisaa tagi for Topologia I
    When user select link text to Vinkkikirjasto
    Then user is redirect to mainpage
