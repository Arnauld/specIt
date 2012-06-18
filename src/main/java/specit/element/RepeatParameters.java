package specit.element;

/**
 *
 */
public class RepeatParameters {
    private final Integer loopCount;
    private final Table withTable;
    private final String reference;

    public RepeatParameters(String reference, int loopCount) {
        this.reference = reference;
        this.loopCount = loopCount;
        this.withTable = null;
    }

    public RepeatParameters(String reference, Table withTable) {
        this.reference = reference;
        this.loopCount = null;
        this.withTable = withTable;
    }

    public String getReference() {
        return reference;
    }

    public boolean hasLoopCount () {
        return loopCount!=null;
    }

    public Integer getLoopCount() {
        return loopCount;
    }

    public boolean hasWithTable() {
        return withTable!=null;
    }

    public Table getWithTable() {
        return withTable;
    }

    @Override
    public String toString() {
        return "RepeatParameters{" +
                "reference='" + reference + '\'' +
                ", withTable='" + withTable + '\'' +
                ", loopCount='" + loopCount + '\'' +
                '}';
    }
}
