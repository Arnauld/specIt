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
import specit.util.BashStyle;
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

    private boolean applyStyle;
    private boolean printUserContext = false;

    public ConsoleColoredReporter() {
        this(true);
    }

    public ConsoleColoredReporter(boolean applyStyle) {
        this.applyStyle = applyStyle;
    }

    /**
     * This allows to print information about <code>UserContext</code> such as when they are defined or discarded
     * and their value (according to their <code>toString()</code> method).
     * By default <code>UserContext</code> information is not printed.
     *
     * @param shouldPrintUserContext
     * @return <code>this</code> for chaining purpose
     */
    public ConsoleColoredReporter printUserContext(boolean shouldPrintUserContext) {
        this.printUserContext = shouldPrintUserContext;
        return this;
    }

    private String open(BashStyle style) {
        if (applyStyle) {
            return style.open();
        }
        else {
            return "";
        }
    }

    private String close(BashStyle style) {
        if (applyStyle) {
            return style.close();
        }
        else {
            return "";
        }
    }

    private String stylize(BashStyle style, String content) {
        if (applyStyle) {
            return style.stylize(content);
        }
        else {
            return content;
        }
    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {
        out.print(open(Red));
        out.print(
                "Error during " + lifecycle.getLifecycleAnnotation().annotationType().getName() + ": " + message + "");
        out.println(close(Red));
    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
        out.print(open(Red));
        out.println(
                "Error during " + lifecycle.getLifecycleAnnotation().annotationType().getName() + ": " + message + "");
        cause.printStackTrace(out);
        out.println(close(Red));
    }

    @Override
    public void lifecycleInvoked(Lifecycle lifecycle) {
    }

    @Override
    public void lifecycleSkipped(Lifecycle lifecycle) {
        out.print(open(Red));
        out.print("Skipped: " + lifecycle.getLifecycleAnnotation().annotationType().getName());
        out.println(close(Red));
    }

    @Override
    public void startStory(Story story) {
        String storyPath = story.getStoryPath();
        if(storyPath==null)
            storyPath = "n/a";
        out.println("(" + stylize(Yellow, storyPath) + ")");
        Narrative narrative = story.getNarrative();
        if (narrative != null && narrative.hasRawPart()) {
            RawElement rawElement = narrative.getRawElement();
            out.print(stylize(Bold, rawElement.getKeywordAlias()));
            out.print(rawElement.contentAfterAlias());
        }
    }

    @Override
    public void startBackground(Background background) {
        RawElement rawElement = background.getRawElement();
        out.print(open(Yellow));
        out.print(stylize(Bold, rawElement.getKeywordAlias()));
        out.print(' ');
        out.print(rawElement.contentAfterAlias());
        if (rawElement.endsWithBlankLine()) {
            out.println(close(Yellow));
        }
        else {
            out.print(close(Yellow));
        }
    }

    @Override
    public void endBackground(Background background) {
    }

    @Override
    public void startScenario(Scenario scenario) {
        suggestedSteps.clear();

        RawElement rawElement = scenario.getRawElement();
        out.print(open(Yellow));
        out.print(stylize(Bold, rawElement.getKeywordAlias()));
        out.print(' ');
        out.print(rawElement.contentAfterAlias().trim());
        out.println(close(Yellow));
    }

    @Override
    public void stepSkipped(InvokableStep invokableStep, CandidateStep candidateStep) {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();

        out.print(open(Yellow));
        if(!this.applyStyle) {
            out.print("[SKIPPED] ");
        }
        out.print(stylize(Bold, keywordAlias));
        out.print(' ');
        for (ParametrizedString.StringToken stringToken : candidateStep.getPattern().tokenize(stepInput)) {
            if (stringToken.isIdentifier()) {
                out.print(stylize(Bold, stringToken.getValue()));
            }
            else {
                out.print(stringToken.getValue());
            }
        }
        out.println(close(Yellow));
    }

    @Override
    public void stepInvoked(InvokableStep invokableStep, CandidateStep candidateStep) {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();


        out.print(open(Green));
        if(!this.applyStyle) {
            out.print("[OK] ");
        }
        out.print(stylize(Bold, keywordAlias));
        out.print(' ');
        for (ParametrizedString.StringToken stringToken : candidateStep.getPattern().tokenize(stepInput)) {
            if (stringToken.isIdentifier()) {
                out.print(stylize(Bold, stringToken.getValue()));
            }
            else {
                out.print(stringToken.getValue());
            }
        }
        out.println(close(Green));
    }

    @Override
    public void stepInvocationFailed(InvokableStep invokableStep,
                                     CandidateStep candidateStep,
                                     String message,
                                     Exception cause)
    {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();

        out.print(open(Red));
        if(!this.applyStyle) {
            out.print("[KO] ");
        }
        out.print(stylize(Bold, keywordAlias));
        out.print(' ');
        for (ParametrizedString.StringToken stringToken : candidateStep.getPattern().tokenize(stepInput)) {
            if (stringToken.isIdentifier()) {
                out.print(stylize(Bold, stringToken.getValue()));
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
        out.println(close(Red));
    }

    @Override
    public void stepInvocationFailed(InvokableStep invokableStep, List<CandidateStep> candidateSteps, String message) {
        String keywordAlias = invokableStep.getUnderlying().getKeywordAlias();
        String stepInput = invokableStep.getAdjustedInput();

        out.print(open(Red));
        if(!this.applyStyle) {
            out.print("[KO] ");
        }
        out.print(stylize(Bold, keywordAlias));
        out.print(' ');
        out.print(stepInput);
        out.print(close(Red));
        out.print(open(RedHi));
        if (candidateSteps.isEmpty()) {
            suggestedSteps.add(invokableStep);
            out.print(" " + stylize(Bold, "(no matching step defined)"));
        }
        else if (candidateSteps.size() > 0) {
            out.print(" " + stylize(Bold, "(Ambiguous steps #" + candidateSteps.size() + " matches)"));
        }
        //out.println("Ouch! " + message);
        out.println(close(RedHi));
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

        out.print(open(Blue));
        for (String suggestion : suggestions) {
            out.println(stylize(Italic, suggestion));
        }
        out.print(close(Blue));
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
        out.println();
    }

    @Override
    public void userContextDefined(UserContextSupport userContextSupport) {
        if(!printUserContext) {
            return;
        }
        out.print(open(Grey));
        out.print("UserContext  " + userContextSupport.scope() + ", " + userContextSupport.getUserContext());
        out.println(close(Grey));
    }

    @Override
    public void userContextDiscarded(UserContextSupport userContextSupport) {
    }
}
