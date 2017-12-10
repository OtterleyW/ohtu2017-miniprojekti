Feature: user can list books in database

  Scenario: user want list books
    Given user is sellected command heakirjat
    When page has list of all books and command Takaisin sivulle is selected
    Then user return to brevious page
