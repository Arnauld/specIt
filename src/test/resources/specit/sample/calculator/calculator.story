/*!
   Hey!

 */

/*!
   The narrative part of our Story. Whereas this is not mandatory, it is strongly recommended.
   It provides the intent of the story
 */
Narrative:
As a ...
I want to ...
So that ...

/*!
   Let's start by a complete but yet simple scenario.
   First, one describes the intent of the scenario:

> be able to add a number to a predefined variable
 */
Scenario: x+2

  /*!
     By default, SpecIt supports the following comment syntax:

     * bash-style single line comment: a line must starts with `#`
     * C-style single line comment: a line must starts with `//`
     * C-style block comment: the block starts with <code>&#47;&#42;</code> and ends with <code>&#42;&#47;</code>
   */

  #  This is a single line comment, the value will be ignored during execution
  // This is an other single line comment

  /**
     This is
     a
     block
     comment!
   */

  /*!
    `Given` ...
   */
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
Scenario: Additions using repeat and variables

  /*!
     Let's see a two new behaviors:

     First, the possiblity to define `Fragment` that is fragment of steps,
     or named sequence of steps. In our case, the fragment is named `Add Value twice`.

     The fragment is composed of a sequence of steps that will get executed
     every time the fragment is invoked. See that as a function ;).

     The sequence of steps of the fragment.
     Note that the step may refer to variable locally defined: `<value>` and `<variable>`.
     A variable is of the form: `<` `variable name` `>`.

     Those variables will be resolved "as is" with their corresponding values, it is up
     to the caller to define them.

     Note that the variable here must not have to refer to a variable within the step definition
     what matters is the resulting string once the variable is resolved.

     Given one has the following step: `When I <what> <value> to <variable>`
     And the supported steps are:

     * `@Given("I add $value to $variable")`
     * `@Given("I substract $value to $variable")`

     Then it can be invoked with the following:

         |    what    | value | variable |
         |        add |   3   |        x |
         |        add |   5   |        x |
         |  substract |   7   |        x |

     Even if `what` is not a variable within the step annotation definition.
   */
  Fragment: Add Value twice
  When I add <value> to <variable>
  When I add <value> to <variable>

  /*!
     An empty line is used to mark the end of the fragment.
   */

  /*!
     Let's start the content of the scenario by itself.
     One defines our `x` variable with an initial value of `2`
   */
  Given a variable x with value 2
  /*!
     Then, one invokes our fragment.
     The Fragment invoked is identified by its name `Add Value twice` and it will be invoked for each
     row of our table. This will act as if the fragment is sequentially called with:

     1. `{value: "3", variable: "x"}`
     2. `{value: "5", variable: "x"}`
     3. `{value: "7", variable: "x"}`

   */
  Repeat [Add Value twice] with:
    | value | variable |
    |   3   |        x |
    |   5   |        x |
    |   7   |        x |
  # 2+(3+3)+(5+5)+(7+7)
  /*!
     Once the fragment get invoked with each row, then one continue our scenario with the assertion.
   */
  Then x should equal to 32


/*!
   Lets expose quickly an other usage of the fragment: be invoked a number of time.
 */
Scenario: Additions using repeat n times

Fragment: Add 3 to x
  When I add 3 to x

/*!
   Let's start the content of the scenario by itself.
   One defines our `x` variable with an initial value of `2`
 */
Given a variable x with value 2
Repeat [Add 3 to x] 5 times
Then x should equal to 17