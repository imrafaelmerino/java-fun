package jsonvalues;

final class MyJsCharLocation
{
    private final long columnNo;
    private final long lineNo;
    private final long offset;

    MyJsCharLocation(long lineNo,
                     long columnNo,
                     long streamOffset
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
