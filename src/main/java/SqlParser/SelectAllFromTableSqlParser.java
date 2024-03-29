package SqlParser;

public class SelectAllFromTableSqlParser extends BaseParser{
    public SelectAllFromTableSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(select\\s*\\*)(.+)(from)","[,]"));
        segments.add(new SqlSegment("(from)(.+)(;)","[,]"));
    }
}
