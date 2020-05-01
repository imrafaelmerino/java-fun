package com.dslplatform.json.parsers.specs;

import com.dslplatform.json.parsers.types.arrays.JsArrayParser;
import java.util.Objects;

public final  class JsArrayOfObjSpecParser extends JsArrayParser
{
    public JsArrayOfObjSpecParser(final JsObjSpecParser parser)
    {
      super(Objects.requireNonNull(parser));
    }
}
