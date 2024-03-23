package SqlParser;

public class CreateDatabaseSqlParser extends BaseParser {
    public CreateDatabaseSqlParser(String originalSql){
        super(originalSql);
    }

    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(create database)(.+)(END)","[,]"));
    }
}
