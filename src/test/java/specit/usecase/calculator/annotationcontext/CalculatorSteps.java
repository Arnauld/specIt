package specit.usecase.calculator.annotationcontext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import specit.annotation.Given;
import specit.annotation.Then;
import specit.annotation.UserContext;
import specit.annotation.Variable;
import specit.annotation.When;
import specit.annotation.lifecycle.AfterScenario;
import specit.usecase.calculator.Calculator;

public class CalculatorSteps {

    public static class ExceptionHolder {
        private Exception exception;

        public void setException(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }
    }

    @UserContext.Factory(scope = UserContext.Scope.Scenario)
    public ExceptionHolder exceptionHolder() {
        return new ExceptionHolder();
    }

    @UserContext.Factory(scope = UserContext.Scope.Scenario)
    public Calculator scenarioCalculator() {
        Calculator calculator = new Calculator();
        return calculator;
    }

    @AfterScenario
    public void disposeScenario(@UserContext Calculator calculator) {
        System.out.println("CalculatorSteps.disposeScenario(" + calculator + ")");
        calculator.clearVariables();
    }

    @Given("a variable $variable with value $value")
    public void defineNamedVariableWithValue(@UserContext Calculator calculator, String variable, int value) {
        calculator.defineVariable(variable, value);
    }

    @When("I add $value to $variable")
    public void addValueToVariable(@UserContext Calculator calculator,
                                   @UserContext ExceptionHolder exceptionHolder,
                                   @Variable("variable") String variable,
                                   @Variable("value") String value)
    {
        try {
            if (value.matches("\\d+")) {
                calculator.addToVariable(variable, Integer.parseInt(value));
            }
            else {
                calculator.addToVariable(variable, value);
            }
        }
        catch (Exception e) {
            exceptionHolder.setException(e);
        }
    }

    @Then("$variable should equal to $expected")
    public void assertVariableEqualTo(@UserContext Calculator calculator,
                                      String variable,
                                      int expectedValue)
    {
        assertThat(calculator.getVariableValue(variable), equalTo(expectedValue));
    }

    @Then("the calculator should display the message '$errorMessage'")
    public void assertErrorMessageIsDisplayed(@UserContext ExceptionHolder exceptionHolder,
                                              String errorMessage)
    {
        Exception lastError = exceptionHolder.getException();
        assertThat("Not in error situtation", lastError, notNullValue());
        assertThat("Wrong error message", lastError.getMessage(), equalTo(errorMessage));
    }

    @Then("the calculator should not be in error")
    public void assertNoErrorMessageIsDisplayed(@UserContext ExceptionHolder exceptionHolder) {
        System.out.println("CalculatorSteps.assertNoErrorMessageIsDisplayed");
        assertThat(exceptionHolder.getException(), nullValue());
    }

}