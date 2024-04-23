package SqlParser;

public class AlterTableAddColumnSqlParser extends BaseParser {
    public AlterTableAddColumnSqlParser(String originalSql){
        super(originalSql);
    }
    //alter table XX add column Grade int;
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(alter table)(.+)(add column)","[,]"));
        segments.add(new SqlSegment("(add column)(.+)(;)","[,]"));
    }
}
