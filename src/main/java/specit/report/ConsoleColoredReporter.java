package specit.report;

import static specit.util.BashStyle.Blue;
import static specit.util.BashStyle.Bold;
import static specit.util.BashStyle.Green;
import static specit.util.BashStyle.Grey;
import static specit.util.BashStyle.Italic;
import static specit.util.BashStyle.Red;
import static specit.util.BashStyle.RedHi;
import static specit.util.BashStyle.Yellow;

import specit.SpecItRuntimeException;
import specit.element.Background;
import specit.element.InvokableStep;
import specit.element.Keyword;
import specit.element.Narrative;
import specit.element.RawElement;
import specit.element.Scenario;
import specit.element.Story;
import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;
import specit.invocation.UserContextSupport;
import specit.util.Java;
import specit.util.New;
import specit.util.ParametrizedString;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 *
 */
public class ConsoleColoredReporter implements Reporter {


    private PrintStream out = System.out;
    private List<InvokableStep> suggestedSteps = New.arrayList();

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {
        out.print(Red.open());
        out.print(
                "Error during " + lifecycle.getLifecycleAnnotation().annotationType().getName() + ": " + message + "");
        out.println(Red.close());
    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
        out.print(Red.open());
        out.println(
                "Error during " + lifecycle.getLifecycleAnnotation().annotationType().getName() + ": " + message + "");
        cause.printStackTrace(out);
        out.println(Red.close());
    }

    @Override
    public void lifecycleInvoked(Lifecycle lifecycle) {
    }

    @Override
    public void lifecycleSkipped(Lifecycle lifecycle) {
        out.print(Red.open());
        out.print("Skipped: " + lifecycle.getLifecycleAnnotation().annotationType().getName());
        out.println(Red.close());
    }

    @Override
    public void startStory(Story story) {
        out.println("(" + Yellow.stylize(story.getStoryPath()) + ")");
        Narrative narrative = story.getNarrative();
        if (narrative != null && narrative.hasRawPart()) {
            RawElement rawElement = narrative.getRawElement();
            out.print(Bold.stylize(rawElement.getKeywordAlias()));
            out.print(rawElement.contentAfterAlias());
        }
    }

    @Override
    public void startBackground(Background background) {
        RawElement rawElement = background.getRawElement();
        out.print(Yellow.open());
        out.print(Bold.stylize(rawElement.getKeywordAlias()));
        out.print(rawElement.contentAfterAlias());
        if (rawElement.endsWithBlankLine()) {
            out.println(Yellow.close());
        }
        else {
            out.print(Yellow.close());
        }
    }

    @Override
    public void endBackground(Background background) {
    }

    @Override
    public void startScenario(Scenario scenario) {
        suggestedSteps.clear();

        RawElement rawElement = scenario.getRawElement();
        out.print(Yellow.open());
        out.print(Bold.stylize(rawElement.getKeywordAlias()));
        out.print(rawElement.contentAfterAlias().trim());
        out.println(Yellow.close());
    }

    @Override
    public void stepSkipped(InvokableStep invokableStep, CandidateStep candidateStep) {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();

        out.print(Yellow.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        for (ParametrizedString.StringToken stringToken : candidateStep.getPattern().tokenize(stepInput)) {
            if (stringToken.isIdentifier()) {
                out.print(Bold.stylize(stringToken.getValue()));
            }
            else {
                out.print(stringToken.getValue());
            }
        }
        out.println(Yellow.close());
    }

    @Override
    public void stepInvoked(InvokableStep invokableStep, CandidateStep candidateStep) {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();


        out.print(Green.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        for (ParametrizedString.StringToken stringToken : candidateStep.getPattern().tokenize(stepInput)) {
            if (stringToken.isIdentifier()) {
                out.print(Bold.stylize(stringToken.getValue()));
            }
            else {
                out.print(stringToken.getValue());
            }
        }
        out.println(Green.close());
    }

    @Override
    public void stepInvocationFailed(InvokableStep invokableStep,
                                     CandidateStep candidateStep,
                                     String message,
                                     Exception cause)
    {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();

        out.print(Red.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        for (ParametrizedString.StringToken stringToken : candidateStep.getPattern().tokenize(stepInput)) {
            if (stringToken.isIdentifier()) {
                out.print(Bold.stylize(stringToken.getValue()));
            }
            else {
                out.print(stringToken.getValue());
            }
        }
        out.println();
        //out.println("Ouch! " + message);
        List<StackTraceElement> selected = New.arrayList();
        boolean skipped = false;
        for (StackTraceElement traceElement : cause.getStackTrace()) {
            if (traceElement.getClassName().startsWith("specit")) {
                if (!skipped) {
                    selected.add(traceElement);
                    selected.add(new StackTraceElement(
                            "(----8<------trunked---------)",
                            "",
                            "<<specit framework>>",
                            -1));
                }
                skipped = true;
            }
            else {
                skipped = false;
                selected.add(traceElement);
            }
        }
        cause.setStackTrace(selected.toArray(new StackTraceElement[selected.size()]));
        cause.printStackTrace(out);
        out.println(Red.close());
    }

    @Override
    public void stepInvocationFailed(InvokableStep invokableStep, List<CandidateStep> candidateSteps, String message) {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();

        out.print(Red.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        out.print(stepInput);
        out.print(Red.close());
        out.print(RedHi.open());
        if (candidateSteps.isEmpty()) {
            suggestedSteps.add(invokableStep);
            out.print(" " + Bold.stylize("(no matching step defined)"));
        }
        else if (candidateSteps.size() > 0) {
            out.print(" " + Bold.stylize("(Ambiguous steps #" + candidateSteps.size() + " matches)"));
        }
        //out.println("Ouch! " + message);
        out.println(RedHi.close());
    }

    @Override
    public void endScenario(Scenario scenario) {
        if (suggestedSteps.isEmpty()) {
            return;
        }

        out.println();
        LinkedHashSet<String> suggestions = New.linkedHashSet();

        for (InvokableStep suggestedStep : suggestedSteps) {
            String suggestion = formatSuggestion(suggestedStep);
            suggestions.add(suggestion);
        }

        out.print(Blue.open());
        for (String suggestion : suggestions) {
            out.println(Italic.stylize(suggestion));
        }
        out.print(Blue.close());
    }

    private static final String NL = "\n";

    protected String formatSuggestion(InvokableStep invokableStep) {
        Keyword keyword = invokableStep.getUnderlying().getKeyword();
        StringBuilder content = new StringBuilder();
        switch (keyword) {
            case Given:
            case When:
            case Then:
                content.append('@').append(keyword.annotationType().getSimpleName());
                break;
            case And:
                throw new SpecItRuntimeException("Fix me!");
            default:
                throw new SpecItRuntimeException("Invalid keyword for step! got: " + keyword);
        }

        // one use the original step def. and not the resolved ones due to possible
        // placeholder presence...
        String suggestedStep = invokableStep.getUnderlying().getRawElement().contentAfterAlias().trim();


        content.append("(\"").append(StringEscapeUtils.escapeJava(suggestedStep)).append("\")").append(NL);
        content.append("public void ").append(methodNameFor(keyword, suggestedStep)).append("() {").append(NL);
        content.append("    throw new UnsupportedOperationException(\"Not implemented\");").append(NL);
        content.append("}").append(NL);

        return content.toString();
    }

    private String methodNameFor(Keyword keyword, String suggestedStep) {
        return Java.filterInvalidMethodNameCharacters(
                WordUtils.uncapitalize(keyword.name()) + WordUtils.capitalize(suggestedStep));
    }

    @Override
    public void endStory(Story story) {
    }

    @Override
    public void userContextDefined(UserContextSupport userContextSupport) {
        out.print(Grey.open());
        out.print("UserContext  " + userContextSupport.scope() + ", " + userContextSupport.getUserContext());
        out.println(Grey.close());
    }

    @Override
    public void userContextDiscarded(UserContextSupport userContextSupport) {
    }
}
