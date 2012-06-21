@author(Pacman) @draft

Scenario: Login several time with an invalid password should block my account

  Fragment: Login
  When I fill "username" with "Travis"
  When I fill "password" with "invalid"
  When I press "login"
  Then I should be informed my credentials are wrong

  When I am on "/login/"
  Repeat [Login] 3 times
  Then I should be informed my account has been locked
