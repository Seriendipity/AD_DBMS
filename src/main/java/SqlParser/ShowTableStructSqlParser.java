package SqlParser;

public class ShowTableStructSqlParser extends BaseParser{
    public ShowTableStructSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(describe)(.+)(;)","[,]"));
    }
}
