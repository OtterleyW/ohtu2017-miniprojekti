Feature: user can modify book information

Scenario: user can modify existing book
Given user has selected command Muokkaa kirjaa
When user has entered an writer "Jussi Väisälä" and title "Topologia II"
Then existing book is modified
#
#Scenario: user cant modify non existing book
#     Given user has sellected command Muokkaa kirjaa
#     When user has entered writer "Homeros" and title "Ilias & Odysseia"
#     Then system will respond with error message
