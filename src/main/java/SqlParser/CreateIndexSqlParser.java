package SqlParser;

public class CreateIndexSqlParser extends BaseParser{

    public CreateIndexSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(create index on)(.+?)([(])","[,]"));
        segments.add(new SqlSegment("([(])(.*?)([)];)","[,]"));
    }
}
