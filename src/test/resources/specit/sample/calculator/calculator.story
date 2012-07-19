/*!
  The narrative part of our Story. Whereas this is not mandatory, it is strongly recommended.
  It provides the intent of the story
 */
Narrative:
As a
I want to
So that I can

/*!
   Let's start by a complete but yet simple scenario.
   First, one describes the intent of the scenario:

   > be able to add a number to a predefined variable
 */
Scenario: x+2

  /*!
    `Given` ...
   */
  # This is a single line comment, the value will be ignored during execution
  Given a variable x with value 2
  /*!
    `When` ...
   */
   When i add 2 to x
  /*!
    `Then` ...
   */
   Then x should equal to 4


/*!
   A Story can contains any number of scenarios. This is up to the writer to organize the scenarios
   according to their intent, and according to the story intent.
 */
Scenario: Additions using repeat and table

  /*!
     Let's see a two new behaviors:

     First, the possiblity to define `Fragment` that is fragment of steps,
     or named sequence of steps. In our case, the fragment is named `Add Value`.
      The fragment is composed of a sequence of steps that will get executed
       every time the fragment is invoked. See that as a function ;).
   */
  Fragment: Add Value
  When I add <value> to <variable>

  Given a variable x with value 2
  Repeat [Add Value] with:
    | value | variable |
    |   3   |        x |
    |   5   |        x |
    |   7   |        x |
  Then x should equal to 17