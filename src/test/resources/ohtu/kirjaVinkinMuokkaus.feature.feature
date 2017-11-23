Feature: user can modify book information

Scenario: user can modify existing book
Given user has selected command Muokkaa kirjaa
When user has entered an writer "Jussi Väisälä" and title "Topologia II"
Then existing book is modified
#
Scenario: user can not modify non existing book
Given user tries to edit a non-existing book
Then user will end up on the error page
