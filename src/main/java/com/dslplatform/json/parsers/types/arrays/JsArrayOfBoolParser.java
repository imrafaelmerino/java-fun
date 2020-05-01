package com.dslplatform.json.parsers.types.arrays;

import com.dslplatform.json.parsers.types.JsBoolParser;

import java.util.Objects;

public final class JsArrayOfBoolParser extends JsArrayParser
{
    public JsArrayOfBoolParser(final JsBoolParser parser)
    {
        super(Objects.requireNonNull(parser));
    }
}
