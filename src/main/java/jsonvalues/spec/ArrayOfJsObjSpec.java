package jsonvalues.spec;
import com.dslplatform.json.derializers.specs.SpecDeserializer;
import io.vavr.Tuple2;
import jsonvalues.*;
import java.util.HashSet;
import java.util.Set;
import static jsonvalues.spec.ERROR_CODE.*;

public class ArrayOfJsObjSpec implements JsSpec
{

  @Override
  public JsSpec nullable()
  {
    return new ArrayOfJsObjSpec(true,
                                required,
                                spec
    );
  }

  @Override
  public JsSpec optional()
  {
    return new ArrayOfJsObjSpec(nullable,
                                false,
                                spec
    );
  }

  @Override
  public SpecDeserializer deserializer()
  {

    return DeserializersFactory.INSTANCE.ofArrayOfObjSpec(spec.bindings.filter((k,s)->s.isRequired()).map(it-> it._1).toVector(),
                                                          spec.bindings.map((k,s)-> new Tuple2<>(k,s.deserializer())),
                                                          nullable,
                                                          spec.strict
                                                         );

  }




  @Override
  public boolean isRequired()
  {
    return required;
  }

  private final boolean nullable;
  private final boolean required;
  private final JsObjSpec spec;

  ArrayOfJsObjSpec(final boolean nullable,
                   final boolean required,
                   final JsObjSpec jsObjSpec
                  )
  {
    this.nullable = nullable;
    this.required = required;
    this.spec = jsObjSpec;

  }


  @Override
  public Set<JsErrorPair> test(final JsPath parentPath,
                               final JsValue value
                              )
  {
    Set<JsErrorPair> errors = new HashSet<>();
    if(value.isNull() && nullable)return errors;
    if(!value.isArray()) {
      errors.add(JsErrorPair.of(parentPath,new Error(value,ARRAY_EXPECTED)));
      return errors;
    }
    return apply(parentPath.index(-1),
          value.toJsArray()
         );
  }




  public Set<JsErrorPair> apply(final JsPath path,
                                final JsArray array
                               )
  {
    Set<JsErrorPair> result = new HashSet<>();
    if (array.isEmpty()) return result;
    final JsPath currentPath = path.inc();
    for (JsValue value : array)
    {
        result.addAll(spec.test(currentPath,value));
    }
    return result;
  }


}


