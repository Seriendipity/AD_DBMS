package SqlParser;

public class CreateTableSqlParser extends BaseParser {
    public CreateTableSqlParser(String originalSql){
        super(originalSql);
    }
//create table sc(Sno char(8),Sname char(20));
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(create table)(.+?)([(])","[,]"));
        segments.add(new SqlSegment("([(])(.+?)([)];)","[,]\\s*?"));
    }
}
