package specit.usecase.calculator.annotationcontext;

import specit.ScenarioContext;
import specit.annotation.*;
import specit.annotation.lifecycle.AfterScenario;
import specit.usecase.calculator.Calculator;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CalculatorSteps {

    @UserContextFactory()
    public void inializeScenario(ScenarioContext scenarioContext) {
        scenarioContext.setUserData(Calculator.class, new Calculator());
    }

    @AfterScenario
    public void disposeScenario(ScenarioContext scenarioContext) {
        // not necessary since scenario context will be disposed
        // but kept for information purpose
        scenarioContext.removeUserData(Calculator.class);
    }

    @Given("a variable $variable with value $value")
    public void defineNamedVariableWithValue(ScenarioContext scenarioContext, String variable, int value) {
        calculator(scenarioContext).defineVariable(variable, value);
    }

    @When("I add $value to $variable")
    public void addValueToVariable(ScenarioContext scenarioContext,
                                   @Variable("variable") String variable,
                                   @Variable("value") String value) {
        try {
            if (value.matches("\\d+")) {
                calculator(scenarioContext).addToVariable(variable, Integer.parseInt(value));
            } else {
                calculator(scenarioContext).addToVariable(variable, value);
            }
        } catch (Exception e) {
            scenarioContext.setUserData(Exception.class, e);
        }
    }

    @Then("$variable should equal to $expected")
    public void assertVariableEqualTo(ScenarioContext scenarioContext,
                                      String variable,
                                      int expectedValue) {
        assertThat(calculator(scenarioContext).getVariableValue(variable), equalTo(expectedValue));
    }

    @Then("the calculator should display the message '$errorMessage'")
    public void assertErrorMessageIsDisplayed(ScenarioContext scenarioContext,
                                              String errorMessage) {
        Exception lastError = scenarioContext.getUserData(Exception.class);
        assertThat("Not in error situtation", lastError, notNullValue());
        assertThat("Wrong error message", lastError.getMessage(), equalTo(errorMessage));
    }

    @Then("the calculator should not be in error")
    public void assertNoErrorMessageIsDisplayed(ScenarioContext scenarioContext) {
        Exception lastError = scenarioContext.getUserData(Exception.class);
        assertThat(lastError, nullValue());
    }

    private static Calculator calculator(ScenarioContext scenarioContext) {
        return scenarioContext.getUserData(Calculator.class);
    }

}