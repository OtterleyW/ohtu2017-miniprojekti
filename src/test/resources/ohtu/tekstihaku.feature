Feature: user can search tips with a keyword

  Scenario: user search with a keyword and founds results
    Given user goes to the search
    When user has entered keyword "Topologia"
    Then tips containig keyword "Topologia" are listed

Scenario: user search with a keyword and don't find results
    Given user goes to the search
    When user has entered keyword "Universumi"
    Then list contains no tips
