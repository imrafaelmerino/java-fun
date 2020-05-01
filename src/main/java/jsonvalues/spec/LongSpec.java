package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

 class LongSpec extends AbstractPredicateSpec implements JsLongPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new LongSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new LongSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofLong(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }
   LongSpec(final boolean required,
            final boolean nullable
           )
  {
    super(required,
          nullable
         );
  }

  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testElem(JsValue::isLong,
                              LONG_EXPECTED,
                              required,
                              nullable).apply(value);
  }
}
