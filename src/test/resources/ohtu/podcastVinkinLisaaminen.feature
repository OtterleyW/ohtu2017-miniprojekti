Feature: user can add a new podcast

Scenario: user successfully adds a new podcast
Given command lisaapodcast is selected
When user has entered url "podcast.me" and author "myself and i"
Then new podcast is added

Scenario: user adds a new podcast without a url
Given command lisaapodcast is selected
When user has entered url "" and author "some author"
Then new podcast is not added

Scenario: user adds a new podcast without an author
Given command lisaapodcast is selected
When user has entered url "yourpodcast.url" and author ""
Then new podcast is not added