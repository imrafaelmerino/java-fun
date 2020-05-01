package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class StrSpec extends AbstractPredicateSpec implements JsStrPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new StrSpec(required,
                       true);
  }

  @Override
  public JsSpec optional()
  {
    return new StrSpec(false,
                       nullable);
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofStr(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  StrSpec(final boolean required,
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
    return Functions.testElem(JsValue::isStr,
                              STRING_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);
  }
}
