package specit.usecase.incubation;

import specit.annotation.Then;
import specit.annotation.When;

/**
 *
 */
public class IncubationSteps {
    @When("I am on \"$pageName\"")
    public void gotoPage(String pageName) {
    }

    @When("I fill \"$fieldName\" with \"$fieldValue\"")
    public void fillField(String fieldName, String fieldValue) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @When("I press \"$buttonId\"")
    public void pressButton(String buttonId) {
    }

    //@Then("I should be informed my credentials are wrong")
    public void thenIShouldBeInformedMyCredentialsAreWrong() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Then("I should be informed my account has been locked")
    public void thenIShouldBeInformedMyAccountHasBeenLocked() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Then("I should be informed my account has been locked")
    public void thenIShouldBeInformedMyAccountHasBeenLocked_duplicate() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
