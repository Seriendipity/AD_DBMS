package SqlParser;

public class InsertAllValuesSqlParser extends BaseParser {
    public InsertAllValuesSqlParser(String originalSql){
        super(originalSql);
    }
    //insert into sc values("S1",1,2),("S2",4,5);
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(insert into)(.+?)(values)","[,]"));
        segments.add(new SqlSegment("(values)(.+?)(;)","[\\((.*)\\)]"));
    }
}
