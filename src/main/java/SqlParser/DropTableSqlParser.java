package SqlParser;

public class DropTableSqlParser extends BaseParser {

    public DropTableSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(drop table)(.+)(END)","[,]"));
    }
}
