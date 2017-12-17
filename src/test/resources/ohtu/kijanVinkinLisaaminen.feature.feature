Feature: user can add new book with hint

  Scenario: user can add new book
    Given command lisaakirja is selected
    When user has entered an writer "Jussi Väisälä" and title "Topologia I"
    Then new book is added

  Scenario: user change mind and not want add new book with hint
    Given command lisaakirja is selected
    When user has selected command takaisin
    Then user is redirect to mainpage

  Scenario: user can not add new book hint if title is empty
    Given command lisaakirja is selected
    When user has entered an writer "Jussi Väisälä" and title ""
    Then new book is not added and error is shown

  Scenario: user can not add new book hint if writer is empty
    Given command lisaakirja is selected
    When user has entered an writer "" and title "Testikirja"
    Then new book is not added and error is shown
