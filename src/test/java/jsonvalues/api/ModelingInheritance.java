package jsonvalues.api;

import fun.gen.Combinators;
import fun.gen.Gen;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.gen.*;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static jsonvalues.spec.JsSpecs.oneSpecOf;


public class ModelingInheritance {


    private static final String NAME_FIELD = "name";
    private static final String BUTTON_COUNT_FIELD = "buttonCount";
    private static final String WHEEL_COUNT_FIELD = "wheelCount";
    private static final String TRACKING_TYPE_FIELD = "trackingType";
    private static final String KEY_COUNT_FIELD = "keyCount";
    private static final String MEDIA_BUTTONS_FIELD = "mediaButtons";
    private static final String CONNECTED_DEVICES_FIELD = "connectedDevices";
    private static final String PERIPHERAL_FIELD = "peripheral";

    private static final List<String> TRACKING_TYPE_ENUM = List.of("ball", "optical");

    @Test
    public void test() {


        JsObjSpec nameSpec = JsObjSpec.of(NAME_FIELD, JsSpecs.str());
        JsObjGen nameGen = JsObjGen.of(NAME_FIELD, JsStrGen.alphabetic());

        JsObjSpec mouseSpec =
                JsObjSpec.of(BUTTON_COUNT_FIELD, JsSpecs.integer(),
                             WHEEL_COUNT_FIELD, JsSpecs.integer(),
                             TRACKING_TYPE_FIELD, JsSpecs.oneStringOf(TRACKING_TYPE_ENUM)
                            )
                         .concat(nameSpec);

        var mouseGen =
                JsObjGen.of(BUTTON_COUNT_FIELD, JsIntGen.arbitrary(0, 10),
                            WHEEL_COUNT_FIELD, JsIntGen.arbitrary(0, 10),
                            TRACKING_TYPE_FIELD, Combinators.oneOf(TRACKING_TYPE_ENUM).map(JsStr::of)
                           )
                        .concat(nameGen);

        JsObjSpec keyboardSpec =
                JsObjSpec.of(KEY_COUNT_FIELD, JsSpecs.integer(),
                             MEDIA_BUTTONS_FIELD, JsSpecs.bool()
                            )
                         .concat(nameSpec);

        JsObjGen keyboardGen =
                JsObjGen.of(KEY_COUNT_FIELD, JsIntGen.arbitrary(0, 10),
                            MEDIA_BUTTONS_FIELD, JsBoolGen.arbitrary()
                           )
                        .concat(nameGen);


        JsObjSpec usbHubSpec =
                JsObjSpec.of(CONNECTED_DEVICES_FIELD, JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec(PERIPHERAL_FIELD)))
                         .withOptKeys(CONNECTED_DEVICES_FIELD)
                         .concat(nameSpec);

        JsObjGen usbHubGen = JsObjGen.of(CONNECTED_DEVICES_FIELD, JsArrayGen.biased(Gen.ofNamedGen(PERIPHERAL_FIELD), 2,10))
                                     .withOptKeys(CONNECTED_DEVICES_FIELD)
                                     .concat(nameGen);


        JsSpec peripheralSpec = JsSpecs.ofNamedSpec(PERIPHERAL_FIELD,
                                                    oneSpecOf(mouseSpec, keyboardSpec, usbHubSpec));

        Gen<JsObj> peripheralGen = Gen.ofNamedGen(PERIPHERAL_FIELD, Combinators.oneOf(mouseGen, keyboardGen, usbHubGen));


        JsObjSpecParser parser = JsObjSpecParser.of(peripheralSpec);


        peripheralGen.sample(10).peek(System.out::println)
                     .forEach(obj -> {
                                  Assertions.assertEquals(obj,
                                                          parser.parse(obj.toString())
                                                         );

                                  Assertions.assertTrue(peripheralSpec.test(obj).isEmpty());
                              }
                             );


    }


}