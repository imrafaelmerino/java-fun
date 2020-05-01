package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class ObjSpec extends AbstractPredicateSpec implements JsObjPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ObjSpec(required,
                       true);
  }

  @Override
  public JsSpec optional()
  {
    return new ObjSpec(false,
                       nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofObj(nullable);
  }

  @Override
  public boolean isRequired()
  {
    return required;
  }

  ObjSpec(final boolean required,
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
    return Functions.testElem(JsValue::isObj,
                              OBJ_EXPECTED,
                              required,
                              nullable
                             )
                    .apply(value);

  }
}
