package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class JsArrayOfObjSpec extends AbstractPredicateSpec implements JsValuePredicate,JsArraySpec
{
  @Override
  public JsSpec nullable()
  {
    return new JsArrayOfObjSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new JsArrayOfObjSpec(false, nullable);
  }

  @Override
  public JsSpecParser parser()
  {
    return  JsSpecParsers.INSTANCE.ofArrayOfObj(nullable);
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
   JsArrayOfObjSpec(final boolean required,
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
    return Functions.testArrayOfTestedValue(v-> {
      if(v.isObj())return Optional.empty();
      else return Optional.of(new Error(v,OBJ_EXPECTED));
    }, required, nullable).apply(value);
  }
}