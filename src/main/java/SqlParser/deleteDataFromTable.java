package SqlParser;

public class deleteDataFromTable extends BaseParser{
    public deleteDataFromTable(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(delete from)(.+)(where)","[,]"));
        segments.add(new SqlSegment("(where)(.+)(;)","[,]"));
    }
}
