package specit.element;

import java.util.*;

/**
 *
 *
 */
public class Table implements Iterable<Table.Row> {

    public static Table empty() {
        return new Table(new String[0], Collections.<Map<String, String>>emptyList());
    }

    private final String[] headers;
    private final List<Map<String,String>> rows;

    public Table(String[] headers, List<Map<String, String>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public String[] getHeaders() {
        return headers;
    }

    public Row getRow(int rowIndex) {
        return new Row(rowIndex);
    }

    public int getRowCount() {
        return rows.size();
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    @Override
    public Iterator<Row> iterator() {
        return new Iterator<Row>() {
            private int rowIndex = 0;
            @Override
            public boolean hasNext() {
                return rowIndex<rows.size();
            }

            @Override
            public Row next() {
                return getRow(rowIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public class Row {
        private final int index;

        public Row(int index) {
            this.index = index;
        }

        public int getRowIndex() {
            return index;
        }

        public String getValue(String columnName) {
            return rows.get(index).get(columnName);
        }

        public Map<String,String> asMap() {
            return rows.get(index);
        }
    }
}
