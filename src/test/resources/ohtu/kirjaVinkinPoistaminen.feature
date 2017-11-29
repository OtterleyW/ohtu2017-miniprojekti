Feature: user can delete book

  Scenario: user delete book
    Given user has selected command poista
    When user click element poista
    Then selected book is deleted

  Scenario: user change mind when deleting book
    Given user has selected command poista
    When user click the element Takaisin listaukseen
    Then user is redirected to listing page

