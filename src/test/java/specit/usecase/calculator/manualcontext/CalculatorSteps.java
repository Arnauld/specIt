package specit.usecase.calculator.manualcontext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static specit.usecase.calculator.manualcontext.CalculatorContext.calculator;
import static specit.usecase.calculator.manualcontext.CalculatorContext.context;

import specit.annotation.Given;
import specit.annotation.Then;
import specit.annotation.Variable;
import specit.annotation.When;
import specit.annotation.lifecycle.AfterScenario;
import specit.annotation.lifecycle.BeforeScenario;

public class CalculatorSteps {

    // TODO make it possible to be static!
    @BeforeScenario
    public void inializeScenario() {
        CalculatorContext.initialize();
    }

    @AfterScenario
    public void disposeScenario() {
        CalculatorContext.dispose();
    }

    @Given("a variable $variable with value $value")
    public void defineNamedVariableWithValue(String variable, int value) {
        calculator().defineVariable(variable, value);
    }

    @When("I add $value to $variable")
    public void addValueToVariable(@Variable("variable") String variable,
                                   @Variable("value") String value)
    {
        try {
            if (value.matches("\\d+")) {
                calculator().addToVariable(variable, Integer.parseInt(value));
            }
            else {
                calculator().addToVariable(variable, value);
            }
        }
        catch (Exception e) {
            context().setLastError(e);
        }
    }

    @Then("$variable should equal to $expected")
    public void assertVariableEqualTo(String variable, int expectedValue) {
        assertThat(calculator().getVariableValue(variable), equalTo(expectedValue));
    }

    @Then("the calculator should display the message '$errorMessage'")
    public void assertErrorMessageIsDisplayed(String errorMessage) {
        Exception lastError = context().getLastError();
        assertThat("Not in error situtation", lastError, notNullValue());
        assertThat("Wrong error message", lastError.getMessage(), equalTo(errorMessage));
    }

    @Then("the calculator should not be in error")
    public void assertNoErrorMessageIsDisplayed() {
        Exception lastError = context().getLastError();
        assertThat(lastError, nullValue());
    }
}