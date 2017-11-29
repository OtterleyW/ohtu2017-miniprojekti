Feature: user can add new video

  Scenario: user add new video
    Given command lisaavideo is selected
    When user has entered "jokuvideo" and a url "www.jokuvideo.com"
    Then new video is added

  Scenario: user change mind when add new video
    Given command lisaavideo is selected
    When user click element Takaisin
    Then user is redirect to mainpage

  Scenario: user try add null video
    Given command lisaavideo is selected
    When user click element lisaa
    Then null video is not added
