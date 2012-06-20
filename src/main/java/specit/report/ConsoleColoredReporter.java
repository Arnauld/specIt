package specit.report;

import specit.element.*;
import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;

import java.io.PrintStream;
import java.util.List;

import static specit.util.BashStyle.*;

/**
 *
 *
 */
public class ConsoleColoredReporter implements Reporter {


    private PrintStream out = System.out;

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {
        out.print(Red.open());
        out.print("Error during " + lifecycle.getLifecycleAnnotation().annotationType().getName() + ": " + message + "");
        out.println(Red.close());
    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
        out.print(Red.open());
        out.println("Error during " + lifecycle.getLifecycleAnnotation().annotationType().getName() + ": " + message + "");
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
        if(narrative!=null && narrative.hasRawPart()) {
            RawPart rawPart = narrative.getRawPart();
            out.print(Bold.stylize(rawPart.getKeywordAlias()));
            out.print(rawPart.contentAfterAlias());
        }
    }

    @Override
    public void startBackground(Background background) {
        RawPart rawPart = background.getRawPart();
        out.print(Yellow.open());
        out.print(Bold.stylize(rawPart.getKeywordAlias()));
        out.print(rawPart.contentAfterAlias());
        if(rawPart.endsWithBlankLine())
            out.println(Yellow.close());
        else
            out.print(Yellow.close());
    }

    @Override
    public void endBackground(Background background) {
    }

    @Override
    public void startScenario(Scenario scenario) {
        RawPart rawPart = scenario.getRawPart();
        out.print(Yellow.open());
        out.print(Bold.stylize(rawPart.getKeywordAlias()));
        out.print(rawPart.contentAfterAlias());
        if(rawPart.endsWithBlankLine())
            out.println(Yellow.close());
        else
            out.print(Yellow.close());
    }

    @Override
    public void stepSkipped(String keywordAlias, String stepInput, CandidateStep candidateStep) {
        out.print(Yellow.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        out.print(stepInput);
        out.println(Yellow.close());
    }

    @Override
    public void stepInvoked(String keywordAlias, String stepInput, CandidateStep candidateStep) {
        out.print(Green.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        out.print(stepInput);
        out.println(Green.close());
    }

    @Override
    public void stepInvocationFailed(String keywordAlias, String stepInput, CandidateStep candidateStep, String message, Exception cause) {
        out.print(Red.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        out.print(stepInput);
        //out.println("Ouch! " + message);
        cause.printStackTrace(out);
        out.println(Red.close());
    }

    @Override
    public void stepInvocationFailed(String keywordAlias, String stepInput, List<CandidateStep> candidateSteps, String message) {
        out.print(Red.open());
        out.print(Bold.stylize(keywordAlias));
        out.print(' ');
        out.print(stepInput);
        if(candidateSteps.isEmpty()) {
            out.print(Italic.stylize(Bold.stylize(" (no matching step defined)")));
        }
        else if(candidateSteps.size()>0) {
            out.print(Italic.stylize(Bold.stylize(" (Ambiguous steps #" + candidateSteps.size() +" matches)")));
        }
        //out.println("Ouch! " + message);
        out.println(Red.close());
    }

    @Override
    public void endScenario(Scenario scenario) {
    }

    @Override
    public void endStory(Story story) {
    }
}
