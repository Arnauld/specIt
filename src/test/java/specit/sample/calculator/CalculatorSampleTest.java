package specit.sample.calculator;

import static org.fest.assertions.api.Assertions.assertThat;

import specit.SpecIt;
import specit.StoryLoader;
import specit.annotation.Given;
import specit.annotation.Then;
import specit.annotation.UserContext;
import specit.annotation.Variable;
import specit.annotation.When;
import specit.element.Keyword;
import specit.invocation.ParameterMappingException;
import specit.junit.SpecItRunner;
import specit.report.ConsoleColoredReporter;
import specit.support.ResourceStoryLoader;
import specit.util.IDE;
import specit.util.New;

import org.junit.runner.RunWith;
import java.util.Map;

/*!

  Let's start by our unit test. SpecIt fully integrates with the well known JUnit framework.
  To indicate Junit to use the SpecIt build-in runner one just have to annotate the Test class
  with `@RunWith(SpecItRunner.class)`.

 */
@RunWith(SpecItRunner.class)
public class CalculatorSampleTest {

    /*!
       Next one has to provide the settings for our tests.
       This annotated method is required by the SpecIt Junit Runner, it provides
       the configuration required to run our test: the stories to execute, the
       glue to use to bridge the steps with the actual code.
     */
    @SpecItRunner.Parameters
    public static SpecIt conf() throws ParameterMappingException {
        return new SpecIt()
                /*!
                   Let's define our own aliases.

                   Whereas this is not necessary because all those aliases are already predefined,
                   it shows how simple it is to add your own alias for a specific keyword.
                 */
                /*!
                   An example would be for context specification, it is possible to express story like:

                     When a product is added that is not already in the cart
                      - The cart item factory should be used to create a cart item for the product being added.
                     When an item is added
                      - The item count should be incremented
                      - The item should be added to the underlying list

                   by simply define `-` as an alias for the Keyword `Then`.

                     specIt.withAlias(Keyword.Then, "-")

                   Alias are used when scanning step to figure out what kind of the step it belongs to,
                   technically it is based on the starting words of a new line. Thus if a line starts
                   with `-` (trailing whitespaces are _usually_ ignored) it will be considered as a `Then`
                   step.
                 */
                .withAlias(Keyword.Scenario, "Scenario:")
                .withAlias(Keyword.Given, "Given")
                .withAlias(Keyword.When, "When")
                .withAlias(Keyword.Then, "Then")
                /*!
                   **This is one of the most important part**. It indicates to SpecIt
                   where to find the glue used for bridging story to code.
                   The `Steps` class is defined above, it contains annotated methods that
                   must match the steps used in the story. Each step must match one annotated
                   method.
                   In this case, only one class is being parsed, but it is possible to
                   use any number of annotated classes.
                */
                .scanAnnotations(Steps.class)
                /*!
                   One indicates the stories that should be executed.
                   The story paths are used as key for the `StoryLoader`. Thus it may
                   be anything as long as the corresponding `StoryLoader` knows how to handle it.
                   **Usually they simply refer to resource or file names.**
                */
                .withStoryPaths("simple_additions.story")
                /*!
                   The Story loader used. In this cas, one create a basic one.
                   Creating a `StoryLoader` is a really simple thing to do. It may be usefull
                   to write your own if the default ones doesn't fulfills your needs.
                   e.g. load stories from a remote location or from a database...
                */
                .withStoryLoader(createStoryLoader())
                /*!
                   Defines a Simple Console output.

                   If launched from outside an IDE (eclipse or intellij)
                   output will be colored using ANSI patterns.
                */
                .withReporter(new ConsoleColoredReporter(!IDE.isExecutedWithinIDE()))
                ;
    }

    /*!
       A build-in story loader that will provides our stories directly from string.
     */
    /*!
       <small>
       Whereas this is not a usual case, and `specit.support.ResourceStoryLoader` should be
       preferred, one uses this approach to show how simple it is to write a `StoryLoader`,
       and to keep all the data in this single class.
       </small>

       There are predefined implementation such as `ResourceStoryLoader` that allows
       to load story based on resource classpath.
     */

    /**
     * @see ResourceStoryLoader
     */
    public static StoryLoader createStoryLoader() {
        return new StoryLoader() {
            @Override
            public String loadStoryAsText(String storyPath) {
                /*!
                   When the story path is `simple_additions`.

                   This is roughly equivalent to store the content in a file `simple_addition.story`

                    Scenario: 2+2

                      Given a variable x with value 2
                       When I add 2 to x
                       Then x should equal to 4

                   and to use the `ResourceStoryLoader` instead of our custom one.

                 */
                if ("simple_additions.story".equals(storyPath)) {
                    return "Scenario: 2+2\n" +
                            "\n" +
                            "  Given a variable x with value 2\n" +
                            "   When I add 2 to x\n" +
                            "   Then x should equal to 4";
                }


                /*!
                   Let's be a bit paranoid ;)
                 */
                throw new RuntimeException("No story found at path <" + storyPath + ">");
            }
        };
    }

    /*!
       Let's define the Calculator that will be tested.

       Actually this will be the class that will be under test.

       It is a simple calculator that provides three methods:

       * define a new variable,
       * add a given value to a known variable,
       * retrieve the value of a variable
    */
    public static class Calculator {
        private Map<String, Integer> variables = New.hashMap();

        public void defineVariable(String variable, int value) {
            variables.put(variable, value);
        }

        public void addValueToVariable(String variable, int value) {
            Integer previousValue = variables.get(variable);
            if (previousValue == null) {
                throw new IllegalStateException(String.format("Variable %s not defined", variable));
            }
            variables.put(variable, previousValue + value);
        }

        public int getVariableValue(String variable) {
            return variables.get(variable);
        }
    }

    /*!
       Once our story is defined. Let's now write the glue between the story and the actual code that
       is instrumented through our steps.
     */
    public static class Steps {

        /*!
           In order to execute our scenario, one will need a new instance of the Calculator.
           Because scenarios or stories could be executed in parallel, the calculator must relies
           in the context of execution of the current scenario. According to this, it would be
           a bad idea to store the value as a field of the class Steps. Indeed, there is only one instance
           of the Steps for the execution of SpecIt, and it is a good practice to keep it stateless.

           Whereas it is possible to manually manage the context using a ThreadLocal for example,
           this can be a pain. SpecIt provides a build-in mecanism that allows to create new object
           in a specific scope, this instance can then be automatically provided as an argument of the step
           invoked.
         */
        @UserContext.Factory(scope = UserContext.Scope.Scenario)
        public Calculator calculator() {
            return new Calculator();
        }

        /*!
           A Given step. One can see that the step defines two variables (they are by default on the
           form `$[a-zA-Z0-9]+`. The variables are provided as argument of the method in the same order
           they are defined. Build-in converters are used to attempt to convert the String extracted
           from the step into the parameter type.
         */
        @Given("a variable $variable with value $value")
        public void defineVariable(
                /*!
                   Provides the calculator currently defined in the context.
                   The calculator has automatically been created by the
                   `calculator()` method
                 */
                @UserContext Calculator calculator,
                /*!
                   Provides the name of the variable, extracted from the step.
                   The value will be retrieved from the `$variable` value.
                   Note that the variable name and the parameter name does not have
                   to match. By default, the variables are provided in the order they are defined,
                   otherwise one must indicates by annotating each parameter with the name of
                   the variable it refers to.
                 */
                String variableName,
                /*!
                   Provides the expected value of the variable.
                   Default converters handle the conversion of String to `int`, enum value (based on their
                   name) and `Table` for tabular parameters.
                 */
                int value)
        {
            calculator.defineVariable(variableName, value);
        }

        @When("I add $value to $variable")
        public void addValueToVariable(@UserContext Calculator calculator,
                                       @Variable("variable") String variableName,
                                       @Variable("value") int value)
        {
            calculator.addValueToVariable(variableName, value);
        }

        /*!
           A `Then` is the place to check the result: almost all assertions should be
           placed within a `Then`.
         */
        @Then("$variable should equal to $value")
        public void assertVariableIsEqualTo(
                /*!
                   Provides the calculator currently defined in the context.
                 */
                @UserContext Calculator calculator,
                /*!
                   Provides the name of the variable, extracted from the step.
                 */
                String variableName,
                /*!
                   Provides the expected value of the variable.
                */
                int expectedValue)
        {
            /*!
               One relies on FestAssert to ease our assertion.
             */
            assertThat(calculator.getVariableValue(variableName)).isEqualTo(expectedValue);
        }

    }

}
