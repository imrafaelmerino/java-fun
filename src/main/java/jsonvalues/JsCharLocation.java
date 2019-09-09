package jsonvalues;

/**
 Represent the position in a Json of a not expected character.
 */
final class JsCharLocation
{
    private final long columnNo;
    private final long lineNo;
    private final long offset;

    JsCharLocation(final long lineNo,
                   final long columnNo,
                   final long streamOffset
                  )
    {
        this.lineNo = lineNo;
        this.columnNo = columnNo;
        this.offset = streamOffset;
    }

    @Override
    public String toString()
    {
        return "(line no=" + lineNo + ", column no=" + columnNo + ", offset=" + offset + ")";
    }
}
