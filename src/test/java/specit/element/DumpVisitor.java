package specit.element;

import java.io.PrintStream;

/**
 *
 *
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
    public boolean beginBackground(Background background) {
        indent++;
        out.println(indent() + "Background");
        return true;
    }

    @Override
    public void endBackground(Background background) {
        indent--;
    }

    @Override
    public boolean beginExample(Example example) {
        indent++;
        out.println(indent() + "Example");
        printPart(example);
        return true;
    }

    @Override
    public void endExample(Example example) {
        indent--;
    }

    @Override
    public boolean beginNarrative(Narrative narrative) {
        indent++;
        out.println(indent() + "Narrative");
        printPart(narrative);
        return true;
    }

    @Override
    public void endNarrative(Narrative narrative) {
        indent--;
    }

    @Override
    public boolean beginRequire(Require require) {
        indent++;
        out.println(indent() + "Require");
        printPart(require);
        return true;
    }

    @Override
    public void endRequire(Require require) {
        indent--;
    }

    @Override
    public boolean beginScenario(Scenario scenario) {
        indent++;
        out.println(indent() + "Scenario");
        printPart(scenario);
        return true;
    }

    @Override
    public void endScenario(Scenario scenario) {
        indent--;
    }

    @Override
    public boolean beginDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        indent++;
        out.println(indent() + "DefaultExecutablePart");
        return true;
    }

    @Override
    public void endDefaultExecutablePart(DefaultExecutablePart defaultExecutablePart) {
        indent--;
    }

    @Override
    public boolean beginStep(Step step) {
        indent++;
        out.println(indent() + "Step");
        printPart(step);
        return true;
    }

    @Override
    public void endStep(Step step) {
        indent--;
    }

    @Override
    public boolean beginStory(Story story) {
        indent++;
        out.println(indent() + "Story");
        return true;
    }

    @Override
    public void endStory(Story story) {
        indent--;
    }

    @Override
    public boolean beginFragment(Fragment fragment) {
        indent++;
        out.println(indent() + "Fragment");
        printPart(fragment);
        return true;
    }

    @Override
    public void endFragment(Fragment fragment) {
        indent--;
    }

    @Override
    public boolean beginRepeat(Repeat repeat) {
        indent++;
        out.println(indent() + "Repeat");
        printPart(repeat);
        return true;
    }

    @Override
    public void endRepeat(Repeat repeat) {
        indent--;
    }

    private void printPart(Element element) {
        indent++;
        out.println(indent()+element.getRawPart().getRawContent().replace("\n", "\n" + indent()));
        indent--;
    }



}
