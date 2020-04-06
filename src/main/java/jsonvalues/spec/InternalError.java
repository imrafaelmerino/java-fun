package jsonvalues.spec;



/**
 * represents an internal error of the library. If this exception is thrown, it means there is an error
 * in the source code of the library and something need to be changed.
 *
 */
class InternalError  extends UnsupportedOperationException
  {

    public InternalError(final String message
                        )
    {
      super(message);
    }

    static InternalError typeNotExpectedInMatcher(Object obj, String function){
      return  new InternalError("Element of type not expected in matcher in the function $function: "+obj);
    }

    static InternalError nothingFound(){
      return new InternalError("JsNothing is an element that can not be persisted. If found during iteration, it'is because of a development error.");
    }

    static InternalError longWasExpected(String message) {return new InternalError(message);}
    static InternalError decimalWasExpected(String message){return new InternalError(message);}
    static InternalError integerWasExpected(String message){return new InternalError(message);}
    static InternalError integralWasExpected(String message){return new InternalError(message);}
    static InternalError numberWasExpected(String message){return new InternalError(message);}
    static InternalError stringWasExpected(String message){return new InternalError(message);}
    static InternalError objWasExpected(String message){return new InternalError(message);}
    static InternalError tokenNotFoundParsingStringntoJsObj(String token){return new InternalError("Token "+token +" not expected");}
    static InternalError tokenNotFoundParsingStringIntoJsArray(String token){return new InternalError("Token "+token +" not expected");}
    static InternalError endArrayTokenExpected(){return new InternalError("End array token } expected, but it never took place."); }
    static InternalError jsonValueIdNotConsidered(){ return new InternalError("JsValue.id() not considered. Default branch of a switch statement was executed."); }
  }



