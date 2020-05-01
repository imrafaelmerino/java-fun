package jsonvalues.spec;

import com.dslplatform.json.parsers.specs.SpecParser;
import jsonvalues.JsValue;


import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.*;

class ArrayOfObjSpec extends AbstractPredicateSpec implements JsArrayPredicate
{
  @Override
  public JsSpec nullable()
  {
    return new ArrayOfObjSpec(required, true);
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfObjSpec(false, nullable);
  }

  @Override
  public SpecParser parser()
  {
    return  ParserFactory.INSTANCE.ofArrayOfObj(nullable);
  }


  @Override
  public boolean isRequired()
  {
    return required;
  }
   ArrayOfObjSpec(final boolean required,
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
