package specit.invocation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import specit.annotation.Variable;
import specit.element.InvocationContext;
import specit.util.ParametrizedString;

import org.junit.Test;
import java.lang.reflect.Method;

/**
 *
 *
 */
public class ParameterMappingsBuilderTest {

    @Test
    public void mappings_noParameter_noVariable() throws ParameterMappingException {
        ParameterMapping[] parameterMappings = mappings("noParameter", "content without variable");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(0));
    }

    @Test(expected = ParameterMappingException.class)
    public void mappings_oneParameter_noVariable_noVariableAnnotations() throws ParameterMappingException {
        mappings("oneParameterNoVariableAnnotations", "content without variable");
    }

    @Test
    public void mappings_oneParameter_oneVariable_noVariableAnnotations() throws ParameterMappingException {
        ParameterMapping[] parameterMappings = mappings("oneParameterNoVariableAnnotations", "content with $variable");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(1));
        assertThat(parameterMappings[0].getParameterIndex(), equalTo(0));
        assertThat(parameterMappings[0].getParameterType(), equalTo((Class) int.class));
        assertThat(parameterMappings[0].getVariableName(), equalTo("variable"));
    }

    @Test(expected = ParameterMappingException.class)
    public void mappings_oneParameter_twoVariables_noVariableAnnotations() throws ParameterMappingException {
        mappings("oneParameterNoVariableAnnotations", "content with $variable1 and $variable2");
    }

    @Test(expected = ParameterMappingException.class)
    public void mappings_twoParameters_oneVariable_noVariableAnnotations() throws ParameterMappingException {
        mappings("twoParametersNoVariableAnnotations", "content with one $variable");
    }

    @Test(expected = ParameterMappingException.class)
    public void mappings_twoParameters_twoVariables_oneVariableAnnotations() throws ParameterMappingException {
        mappings("notEnoughVariableAnnotation", "content with $variable1 and $variable2");
    }

    @Test(expected = ParameterMappingException.class)
    public void mappings_twoParameters_twoVariables_duplicateVariableAnnotation() throws ParameterMappingException {
        mappings("duplicateNameVariableAnnotations", "content with $variable1 and $variable2");
    }

    @Test
    public void mappings_twoParameters_twoVariables_noVariableAnnotations() throws ParameterMappingException {
        ParameterMapping[] parameterMappings = mappings(
                "twoParametersNoVariableAnnotations", "content with $variable1 and $variable2");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(2));
        assertThat(parameterMappings[0].getParameterIndex(), equalTo(0));
        assertThat(parameterMappings[0].getParameterType(), equalTo((Class) int.class));
        assertThat(parameterMappings[0].getVariableName(), equalTo("variable1"));

        assertThat(parameterMappings[1].getParameterIndex(), equalTo(1));
        assertThat(parameterMappings[1].getParameterType(), equalTo((Class) String.class));
        assertThat(parameterMappings[1].getVariableName(), equalTo("variable2"));
    }

    @Test(expected = ParameterMappingException.class)
    public void mappings_twoParameters_twoVariables_withVariableAnnotations_variableNamesMismatched()
            throws ParameterMappingException
    {
        mappings(
                "twoParametersWithVariableAnnotations", "content with $quantity and $firstname");
    }

    @Test
    public void mappings_twoParameters_twoVariables_withVariableAnnotations_inOrder() throws ParameterMappingException {
        ParameterMapping[] parameterMappings = mappings(
                "twoParametersWithVariableAnnotations", "content with $amount and $name");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(2));
        assertThat(parameterMappings[0].getParameterIndex(), equalTo(0));
        assertThat(parameterMappings[0].getParameterType(), equalTo((Class) int.class));
        assertThat(parameterMappings[0].getVariableName(), equalTo("amount"));

        assertThat(parameterMappings[1].getParameterIndex(), equalTo(1));
        assertThat(parameterMappings[1].getParameterType(), equalTo((Class) String.class));
        assertThat(parameterMappings[1].getVariableName(), equalTo("name"));
    }

    @Test
    public void mappings_twoParameters_twoVariables_withVariableAnnotations_reversedOrder()
            throws ParameterMappingException
    {
        ParameterMapping[] parameterMappings = mappings(
                "twoParametersWithVariableAnnotations", "content with $name and $amount");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(2));
        assertThat(parameterMappings[0].getParameterIndex(), equalTo(0));
        assertThat(parameterMappings[0].getParameterType(), equalTo((Class) int.class));
        assertThat(parameterMappings[0].getVariableName(), equalTo("amount"));

        assertThat(parameterMappings[1].getParameterIndex(), equalTo(1));
        assertThat(parameterMappings[1].getParameterType(), equalTo((Class) String.class));
        assertThat(parameterMappings[1].getVariableName(), equalTo("name"));
    }

    @Test
    public void mappings_twoParameters_oneVariable_oneSpecial_noVariableAnnotations() throws ParameterMappingException {
        ParameterMapping[] parameterMappings = mappings(
                "twoParametersNoVariableAnnotationsWithSpecial", "content with one $variable");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(2));
        assertThat(parameterMappings[0].getParameterIndex(), equalTo(0));
        assertThat(parameterMappings[0].getParameterType(), equalTo((Class) InvocationContext.class));
        assertThat(parameterMappings[0].getVariableName(), nullValue());

        assertThat(parameterMappings[1].getParameterIndex(), equalTo(1));
        assertThat(parameterMappings[1].getParameterType(), equalTo((Class) int.class));
        assertThat(parameterMappings[1].getVariableName(), equalTo("variable"));
    }

    @Test
    public void mappings_threeParameters_twoVariables_oneSpecial_noVariableAnnotations()
            throws ParameterMappingException
    {
        ParameterMapping[] parameterMappings = mappings(
                "threeParametersNoVariableAnnotationsWithSpecial", "content with $name and $amount");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(3));
        assertThat(parameterMappings[0].getParameterIndex(), equalTo(0));
        assertThat(parameterMappings[0].getParameterType(), equalTo((Class) String.class));
        assertThat(parameterMappings[0].getVariableName(), equalTo("name"));

        assertThat(parameterMappings[1].getParameterIndex(), equalTo(1));
        assertThat(parameterMappings[1].getParameterType(), equalTo((Class) InvocationContext.class));
        assertThat(parameterMappings[1].getVariableName(), nullValue());

        assertThat(parameterMappings[2].getParameterIndex(), equalTo(2));
        assertThat(parameterMappings[2].getParameterType(), equalTo((Class) int.class));
        assertThat(parameterMappings[2].getVariableName(), equalTo("amount"));
    }

    @Test
    public void mappings_threeParameters_twoVariables_oneSpecial_withVariableAnnotations()
            throws ParameterMappingException
    {
        ParameterMapping[] parameterMappings = mappings(
                "threeParametersWithVariableAnnotationsWithSpecial", "content with $name and $amount");
        assertThat(parameterMappings, notNullValue());
        assertThat(parameterMappings.length, equalTo(3));
        assertThat(parameterMappings[0].getParameterIndex(), equalTo(0));
        assertThat(parameterMappings[0].getParameterType(), equalTo((Class) int.class));
        assertThat(parameterMappings[0].getVariableName(), equalTo("amount"));

        assertThat(parameterMappings[1].getParameterIndex(), equalTo(1));
        assertThat(parameterMappings[1].getParameterType(), equalTo((Class) InvocationContext.class));
        assertThat(parameterMappings[1].getVariableName(), nullValue());

        assertThat(parameterMappings[2].getParameterIndex(), equalTo(2));
        assertThat(parameterMappings[2].getParameterType(), equalTo((Class) String.class));
        assertThat(parameterMappings[2].getVariableName(), equalTo("name"));
    }


    // ---

    private static ParameterMapping[] mappings(String methodName, String pStringContent)
            throws ParameterMappingException
    {
        return new ParameterMappingsBuilder(sampleMethodByName(methodName), pString(pStringContent)).getParameterMappings();
    }

    private static ParametrizedString pString(String content) {
        return new ParametrizedString(content);
    }

    private static Method sampleMethodByName(String methodName) {
        for (Method method : Sample.class.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("No method found matching <" + methodName + ">");
    }

    @SuppressWarnings("UnusedDeclaration")
    public static class Sample {

        public void noParameter() {
        }

        public void oneParameterNoVariableAnnotations(int intValue) {
        }

        public void twoParametersNoVariableAnnotationsWithSpecial(InvocationContext context, int intValue) {
        }

        public void threeParametersNoVariableAnnotationsWithSpecial(String param,
                                                                    InvocationContext context,
                                                                    int intValue)
        {
        }

        public void threeParametersWithVariableAnnotationsWithSpecial(@Variable("amount") int intValue,
                                                                      InvocationContext context,
                                                                      @Variable("name") String param)
        {
        }

        public void twoParametersNoVariableAnnotations(int intValue, String param) {
        }

        public void notEnoughVariableAnnotation(@Variable("amount") int intValue, String param) {
        }

        public void twoParametersWithVariableAnnotations(@Variable("amount") int intValue,
                                                         @Variable("name") String param)
        {
        }

        public void duplicateNameVariableAnnotations(@Variable("amount") int intValue,
                                                     @Variable("amount") String param)
        {
        }
    }
}
