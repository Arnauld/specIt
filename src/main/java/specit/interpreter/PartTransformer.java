package specit.interpreter;

import specit.element.RawPart;

/**
 *
 */
public interface PartTransformer {
    RawPart transform(RawPart rawPart);
}
