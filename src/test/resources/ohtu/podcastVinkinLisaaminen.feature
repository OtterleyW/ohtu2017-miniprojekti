Feature: user can add a new podcast

Scenario: user adds new podcast
Given command lisaapodcast is selected
When user has entered url "podcast.me" and author "myself and i"
Then new podcast is added