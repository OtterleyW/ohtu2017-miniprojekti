Feature: user can list books in database

  Scenario: user want list books
    Given user is sellected command heakirjat
    When page has list of all books and command Takaisin sivulle is selected
    Then user return to brevious page
#
#  Scenario: user is listing existing books and database same error
#    Given user is sellected command heakirjat
#    When database has error and system will send error message
#    Then user is redirect to previouspage
