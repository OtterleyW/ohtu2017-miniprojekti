Feature: user can add new book with hint

  Scenario: user can add new book
    Given command lisaakirja is selected
    When user has entered an writer "Jussi Väisälä" and title "Topologia I"
    Then new book is added

  Scenario: user change mind and not want add new book with hint
    Given command lisaakirja is selected
    When user has selected command takaisin
    Then user is redirect to mainpage

##  Scenario: user try add existing book with same title
##    Given command lisaakirja is selected
##    When user has entered an writer "Jussi Väisälä" and title "Topologia I"
##    Then system sent message sent error message