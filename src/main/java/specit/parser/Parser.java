package specit.parser;

import specit.element.Alias;
import specit.element.Conf;
import specit.element.Keyword;
import specit.element.RawPart;
import specit.util.CharIterator;
import specit.util.CharIterators;
import specit.util.CharSequences;
import specit.util.NotThreadSafe;

@NotThreadSafe
public class Parser {
    
    private Conf conf;

    public Parser() {
        this(Conf.getDefault());
    }

    public Parser(Conf conf) {
        this.conf = conf;
    }

    public void scan(CharSequence content, Listener listener) {
        scan(CharIterators.createFrom(content), 0, listener);
    }

    public void scan(CharIterator it, int baseOffset, Listener listener) {

        // temporary state kept during a scan.
        Alias lastAlias = null;

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
                Alias entry = line.startingAliasEntry();
                if(entry!=null) {
                    block.emitTo(listener, aliasOrDefault(lastAlias), keywordOrDefault(lastAlias));
                    block.reset(line.offset);
                    lastAlias = entry;
                }
                line.emitTo(block);
                // line is reset without any char, offset must be the next one
                line.reset(offset+1);
            }
            offset++;
        }

        // remaining
        Alias entry = line.startingAliasEntry();
        if(entry!=null) {
            block.emitTo(listener, aliasOrDefault(lastAlias), keywordOrDefault(lastAlias));
            block.reset(line.offset);
            lastAlias = entry;
        }
        line.emitTo(block);
        block.emitTo(listener, aliasOrDefault(lastAlias), keywordOrDefault(lastAlias));
    }

    private static Keyword keywordOrDefault(Alias alias) {
        if(alias==null)
            return Keyword.Unknown;
        return alias.getKeyword();
    }

    private static String aliasOrDefault(Alias alias) {
        if(alias==null)
            return null;
        return alias.getKeywordAlias();
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
                RawPart rawPart = new RawPart(offset, keyword, content, aliasUsed);
                listener.on(rawPart);
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
        public Alias startingAliasEntry() {
            for(Alias alias : conf.aliases()) {
                if(CharSequences.startsWithIgnoringChars(buffer, alias.getKeywordAlias(), conf.ignoredCharactersOnPartStart())) {
                    return alias;
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
