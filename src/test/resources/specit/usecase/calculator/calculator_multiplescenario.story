Scenario: 2+2

  Given a variable x with value 2
   When I add 2 to x
   Then x should equal to 4


Scenario: 2+3

  Given a variable x with value 2
   When I add 3 to x
   Then x should equal to 5

Scenario: Additions using repeat and table

  Fragment: Add Value
  When I add <value> to <variable>

  Given a variable x with value 2
  Repeat [Add Value] with:
    | value | variable |
    |   3   |        x |
    |   5   |        x |
    |   7   |        x |
  Then x should equal to 17