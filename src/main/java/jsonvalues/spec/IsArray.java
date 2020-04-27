package jsonvalues.spec;

import com.dslplatform.json.derializers.specs.SpecDeserializer;
import jsonvalues.JsPath;
import jsonvalues.JsValue;


import java.util.Optional;
import java.util.Set;


class IsArray extends AbstractPredicate implements JsArrayPredicate
{


  @Override
  public JsSpec nullable()
  {
    return new IsArray(required,true);
  }

  @Override
  public JsSpec optional()
  {
    return new IsArray(false,nullable);
  }

  @Override
  public SpecDeserializer deserializer()
  {
    return DeserializersFactory.INSTANCE.ofArrayOfValue(nullable);
  }



  IsArray(final boolean required,
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
                               nullable)
                    .apply(value);
  }
}
