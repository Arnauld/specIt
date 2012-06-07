package specit.parser;

import java.util.HashMap;
import java.util.Map;

import specit.Keyword;
import specit.Part;
import specit.util.CharIterator;
import specit.util.CharIterators;
import specit.util.CharSequences;

public class Parser {
    
    private Map<String, Keyword> aliases = new HashMap<String, Keyword>();

    public Parser withAlias(Keyword kw, String value) {
        aliases.put(value, kw);
        return this;
    }

    public void scan(CharSequence content, Listener listener) {
        scan(CharIterators.createFrom(content), 0, listener);
    }

    private Map.Entry<String,Keyword> lastEntry;

    public void scan(CharIterator it, int baseOffset, Listener listener) {
        int offset = baseOffset;
        Line line = new Line();
        Block block = new Block();
        block.reset(offset);
        line.reset(offset);
        while(true) {
            int read = it.read();
            if(read==CharIterator.EOF)
                break;

            line.append((char)read);
            if(isNewlineCharacter(read)) {
                Map.Entry<String,Keyword> entry = line.startingAliasEntry();
                if(entry!=null) {
                    block.emitTo(listener, lastEntryKeyOrDefault(), lastEntryValueOrDefault());
                    block.reset(line.offset);
                    lastEntry = entry;
                }
                line.emitTo(block);
                // line is reset without any char, offset must be the next one
                line.reset(offset+1);
            }
            offset++;
        }

        // remaining
        Map.Entry<String,Keyword> entry = line.startingAliasEntry();
        if(entry!=null) {
            block.emitTo(listener, lastEntryKeyOrDefault(), lastEntryValueOrDefault());
            block.reset(line.offset);
            lastEntry = entry;
        }
        line.emitTo(block);
        block.emitTo(listener, lastEntryKeyOrDefault(), lastEntryValueOrDefault());
    }

    private Keyword lastEntryValueOrDefault() {
        if(lastEntry==null)
            return Keyword.Unknown;
        return lastEntry.getValue();
    }

    private String lastEntryKeyOrDefault() {
        if(lastEntry==null)
            return null;
        return lastEntry.getKey();
    }

    private class Block {
        private StringBuilder buffer = new StringBuilder();
        private int offset;
        public void reset(int offset) {
            this.offset = offset;
            this.buffer.setLength(0);
        }
        public void emitTo(Listener listener, String aliasUsed, Keyword keyword) {
            if(buffer.length()>0) {
                String content = buffer.toString();
                Part part = new Part(offset, keyword, content, aliasUsed);
                listener.on(part);
            }
        }
    }

    private class Line {
        private StringBuilder buffer = new StringBuilder();
        private int offset;
        public void append(char c) {
            buffer.append(c);
        }
        public void reset(int offset) {
            this.offset = offset;
            this.buffer.setLength(0);
        }
        public Map.Entry<String,Keyword> startingAliasEntry () {
            for(Map.Entry<String,Keyword> entry : aliases.entrySet()) {
                if(CharSequences.startsWithIgnoringChars(buffer, entry.getKey(), "\t ")) {
                    return entry;
                }
            }
            return null;
        }
        public void emitTo(Block block) {
            block.buffer.append(buffer);
        }
    }

    private static boolean isNewlineCharacter(int read) {
        return read=='\r' || read=='\n';
    }
}
