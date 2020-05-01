package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;

import java.util.Optional;


class ArraySpec extends AbstractPredicateSpec implements JsArrayPredicate
{

  @Override
  public JsSpec nullable()
  {
    return new ArraySpec(required,
                         true
    );
  }

  @Override
  public JsSpec optional()
  {
    return new ArraySpec(false,
                         nullable
    );
  }

  @Override
  public SpecParser parser()
  {
    return ParserFactory.INSTANCE.ofArrayOfValue(nullable);
  }


  ArraySpec(final boolean required,
            final boolean nullable
           )
  {
    super(required,
          nullable
         );

  }

  @Override
  public boolean isRequired()
  {
    return required;
  }


  @Override
  public Optional<Error> test(final JsValue value)
  {
    return Functions.testArray(required,
                               nullable
                              )
                    .apply(value);
  }
}
