package SqlParser;

public class SelectAllFromTableWhereSqlParser extends BaseParser{
    public SelectAllFromTableWhereSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(select\\s*\\*)(.+)(from)","[,]"));
        segments.add(new SqlSegment("(from)(.+)(where)","[,]"));
        segments.add(new SqlSegment("(where)(.+)(;)","[,]"));
    }
}
