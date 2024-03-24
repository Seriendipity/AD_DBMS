package SqlParser;

public class InsertPartSqlParser extends BaseParser{
    public InsertPartSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(insert into)(.+?)([(])","[,]"));
        segments.add(new SqlSegment("([(])(.+)([)][\\s+]values)","[,]"));
        segments.add(new SqlSegment("(values)(.+?)([)];)","[\\((.*)\\)]"));
    }
}
