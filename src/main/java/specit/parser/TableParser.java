package specit.parser;

import specit.element.Table;
import specit.util.New;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 */
public class TableParser {

    private static final String ROW_SEPARATOR = "[\n\r]+";

    private final ParserConf conf;

    public TableParser(ParserConf conf) {
        this.conf = conf;
    }

    public Table parse(String content) {

        List<Map<String, String>> variablesRows = New.arrayList();

        boolean firstRow = true;
        String[] headers = null;
        String[] rows = splitInRows(content);
        for (int row=0;row<rows.length;row++) {
            String rowContent = rows[row].trim();
            if(!rowContent.startsWith(conf.cellSeparator())) {
                continue;
            }

            String[] cells = splitInCells(rowContent);
            if(firstRow) {
                firstRow = false;
                headers = new String[cells.length-1];
                // index 0 is the trailing characters before the first |
                // they don't belongs to the cell
                for (int i = 1; i < cells.length; i++) {
                    headers[i-1] = cleanUpHeader(cells[i]);
                }
            }
            else {
                Map<String,String> variablesRow = New.hashMap();
                // index 0 is the trailing characters before the first |
                // they don't belongs to the cell
                for(int k=1;k<cells.length;k++) {
                    String cell = cleanUpValue(cells[k]);
                    variablesRow.put(headers[k-1], cell);
                }
                variablesRows.add(variablesRow);
            }
        }

        return new Table(headers, variablesRows);
    }

    private String cleanUpValue(String value) {
        return value.trim();
    }

    private String cleanUpHeader(String header) {
        String cleaned = header.trim();
        int beg = 0;
        int end = cleaned.length();
        if(cleaned.startsWith("<")) {
            beg++;
        }
        if(cleaned.endsWith(">")) {
            end--;
        }
        return cleaned.substring(beg, end);
    }

    private String[] splitInCells(String row) {
        return row.split(Pattern.quote(conf.cellSeparator()));
    }

    private String[] splitInRows(String content) {
        return content.split(ROW_SEPARATOR);
    }
}
