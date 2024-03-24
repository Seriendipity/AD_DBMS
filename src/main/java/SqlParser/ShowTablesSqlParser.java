package SqlParser;

public class ShowTablesSqlParser extends BaseParser{
    public ShowTablesSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(show tables)(.+)(;)","[,]"));
    }
}
