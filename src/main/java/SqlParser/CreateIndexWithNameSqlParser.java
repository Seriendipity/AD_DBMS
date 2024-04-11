package SqlParser;

public class CreateIndexWithNameSqlParser extends BaseParser{
    public CreateIndexWithNameSqlParser(String originalSql){
        super(originalSql);
    }

    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(create index)(.+?)(on)","[,]"));
        segments.add(new SqlSegment("(on)(.+?)([(])","[,]"));
        segments.add(new SqlSegment("([(])(.+?)([)];)","[,]"));
    }

}
