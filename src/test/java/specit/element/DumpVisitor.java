package specit.element;

import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 * User: arnauld
 * Date: 08/06/12
 * Time: 20:29
 * To change this template use File | Settings | File Templates.
 */
public class DumpVisitor extends ElementVisitor {

    private int indent;
    private PrintStream out;

    public DumpVisitor() {
        out = System.out;
    }

    private String indent() {
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<indent;i++)
            builder.append("  ");
        return builder.toString();
    }

    @Override
    public void beginBackground(Background background) {
        indent++;
        out.println(indent() + "Background");
    }

    @Override
    public void endBackground(Background background) {
        indent--;
    }

    @Override
    public void beginExample(Example example) {
        indent++;
        out.println(indent() + "Example");
        printPart(example);
    }

    @Override
    public void endExample(Example example) {
        indent--;
    }

    private void printPart(Element element) {
        indent++;
        out.println(indent()+element.getRawPart().getRawContent().replace("\n", "\n" + indent()));
        indent--;
    }


    @Override
    public void beginNarrative(Narrative narrative) {
        indent++;
        out.println(indent() + "Narrative");
        printPart(narrative);
    }

    @Override
    public void endNarrative(Narrative narrative) {
        indent--;
    }

    @Override
    public void beginRequire(Require require) {
        indent++;
        out.println(indent() + "Require");
        printPart(require);
    }

    @Override
    public void endRequire(Require require) {
        indent--;
    }

    @Override
    public void beginScenario(Scenario scenario) {
        indent++;
        out.println(indent() + "Scenario");
        printPart(scenario);
    }

    @Override
    public void endScenario(Scenario scenario) {
        indent--;
    }

    @Override
    public void beginDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        indent++;
        out.println(indent() + "DefaultExecutablePart");
    }

    @Override
    public void endDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        indent--;
    }

    @Override
    public void beginStep(Step step) {
        indent++;
        out.println(indent() + "Step");
        printPart(step);
    }

    @Override
    public void endStep(Step step) {
        indent--;
    }

    @Override
    public void beginStory(Story story) {
        indent++;
        out.println(indent() + "Story");
    }

    @Override
    public void endStory(Story story) {
        indent--;
    }

    @Override
    public void beginFragment(Fragment fragment) {
        indent++;
        out.println(indent() + "Fragment");
        printPart(fragment);
    }

    @Override
    public void endFragment(Fragment fragment) {
        indent--;
    }

    @Override
    public void beginRepeat(Repeat repeat) {
        indent++;
        out.println(indent() + "Repeat");
        printPart(repeat);
    }

    @Override
    public void endRepeat(Repeat repeat) {
        indent--;
    }
}
