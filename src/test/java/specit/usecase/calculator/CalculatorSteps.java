package specit.usecase.calculator;

import specit.annotation.Given;
import specit.annotation.Then;
import specit.annotation.Variable;
import specit.annotation.When;

/**
 *
 *
 */
public class CalculatorSteps {

    @Given("a variable $variable with value $value")
    public void defineNamedVariableWithValue(String variable, int value) {
        System.out.println("CalculatorSteps.defineNamedVariableWithValue(" + variable + ", " + value + ")");
    }

    @When("I add $value to $variable")
    public void addValueToVariable(@Variable("variable") String variable,
                                   @Variable("value")int value) {
        System.out.println("CalculatorSteps.addValueToVariable(" + variable + ", " + value + ")");
    }

    @Then("$variable should equal to $expected")
    public void assertVariableEqualTo(String variable, int expectedValue) {
        System.out.println("CalculatorSteps.assertVariableEqualTo(" + variable + ", " + expectedValue + ")");
    }

    @Then("the calculator should display the message '$errorMessage'")
    public void assertErrorMessageIsDisplayed(String errorMessage) {
        System.out.println("CalculatorSteps.assertErrorMessageIsDisplayed(" + errorMessage + ")");
    }
}