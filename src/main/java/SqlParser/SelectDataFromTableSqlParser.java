package SqlParser;

public class SelectDataFromTableSqlParser extends BaseParser{
    public SelectDataFromTableSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(select)(.+)(from)","[,]"));
        segments.add(new SqlSegment("(from)(.+)(;)","[,]"));
    }
}
