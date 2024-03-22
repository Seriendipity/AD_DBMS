package SqlParser;

public class CreateDatabaseSqlParser extends BaseParser {
    public CreateDatabaseSqlParser(String originalSql){
        super(originalSql);
    }

    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(create database)(.+)(END)","[,]"));
    }//以逗号作为分隔，寻找包含create database的子句
}
