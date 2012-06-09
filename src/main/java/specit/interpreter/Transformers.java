package specit.interpreter;

import specit.element.Conf;
import specit.element.RawPart;

import java.util.Map;

/**
 *
 */
public class Transformers {

    public static PartTransformer resolveVariables(final Conf conf, final Map<String,String> variables) {
        return new PartTransformer() {
            @Override
            public RawPart transform(RawPart rawPart) {
                String rawContent = rawPart.getRawContent();
                String resolved = conf.templateEngine().resolve(rawContent, variables).toString();
                return rawPart.withRawContent(resolved);
            }
        };
    }
}
