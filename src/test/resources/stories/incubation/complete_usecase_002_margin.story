Narrative:

Background: Create predefined users for all the scenarios
  Given there are users:
    | username | password | email               |
    | Travis   | 123456   | travis@mccallum.fr  |
    | Vlad     | 22@222   | vladt@mccallum.fr   |

Scenario: Login with an invalid username
  When I am on "/login/"
  When I fill "username" with "TrAvis"
  When I fill "password" with "123456"
  When I press "login"
  Then I should be informed my credentials are wrong

Scenario: Login several time with an invalid password should block my account
  When I am on "/login/"
  |  When I fill "username" with "<username>"
  |  When I fill "password" with "invalid"
  |  When I press "login"
  |  Then I should be informed my credentials are wrong
  Repeat: 3 times
  Then I should be informed my account has been locked


Scenario: Create entries is limited to 5
  When I am logged as "Travis"
  |  When I create a new entry named <name>
  |   And I assign the severity  #<severity>
  With:
      | name    | severity |
      | to do   |        3 |
      | fix me  |        1 |
      | later   |        4 |
      | rework  |        4 |
      | wording |        5 |
  Then I should be informed the number of entried has reach its maximum
