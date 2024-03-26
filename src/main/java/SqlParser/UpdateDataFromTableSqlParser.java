package SqlParser;

public class UpdateDataFromTableSqlParser extends BaseParser {
    public UpdateDataFromTableSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(update table)(.+)(set)","[,]"));
        segments.add(new SqlSegment("(set)(.+)(where)","[,]"));
        segments.add(new SqlSegment("(where)(.+)(;)","[,]"));
    }
}
