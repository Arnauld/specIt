@author(Pacman) @draft

Background: Create predefined users for all the scenarios

  Given there are users:
    | username | password | email               |
    | Travis   | 123456   | travis@mccallum.fr  |
    | Vlad     | 22@222   | vladt@mccallum.fr   |

Scenario: Login several time with an invalid password should block my account

  Fragment: Login
  When I fill "username" with "<username>"
  When I fill "password" with "invalid"
  When I press "login"
  Then I should be informed my credentials are wrong

  When I am on "/login/"
  Repeat [Login] 3 times
  Then I should be informed my account has been locked

Scenario: Create entries is limited to 5

  Fragment: Create Entry
  When I create a new entry named <name>
  And I assign the severity  #<severity>

  When I am logged as "Travis"
  Repeat [Create Entry] with:
    | name    | severity |
    | to do   |        3 |
    | fix me  |        1 |
    | later   |        4 |
    | rework  |        4 |
    | wording |        5 |
  Then I should be informed the number of entries has reached its maximum


