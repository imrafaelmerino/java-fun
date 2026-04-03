package fun.gen;

/**
 * Action to apply when a CSV data row fails to parse.
 */
public enum CsvRowErrorAction {
    /**
     * Propagate the parsing error.
     */
    THROW,
    /**
     * Skip the malformed row and continue reading.
     */
    SKIP
}
