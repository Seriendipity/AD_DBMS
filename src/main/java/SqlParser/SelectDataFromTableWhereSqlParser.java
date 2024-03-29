package SqlParser;

public class SelectDataFromTableWhereSqlParser extends BaseParser {
    public SelectDataFromTableWhereSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(select)(.+)(from)","[,]"));
        segments.add(new SqlSegment("(from)(.+)(where)","[,]"));
        segments.add(new SqlSegment("(where)(.+)(;)","[,]"));
    }
}
