package jsonvalues.spec;

import jsonvalues.JsInstant;
import jsonvalues.JsParserException;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

final class JsInstantReader extends AbstractReader {
    @Override
    JsInstant value(final JsReader reader) throws IOException {
        try {
            return JsInstant.of(Instant.from(ISO_INSTANT.parse(reader.readString())));
        } catch (DateTimeParseException e) {
            throw JsParserException.reasonAt(e.getMessage(),
                                             reader.getPositionInStream()
                                            );
        }

    }

}
