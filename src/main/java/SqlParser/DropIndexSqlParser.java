package SqlParser;

public class DropIndexSqlParser extends BaseParser{
    public DropIndexSqlParser(String originalSql){
        super(originalSql);
    }

    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(drop index)(.+)(;)","[,]"));
    }

}
