package SqlParser;

import SqlFunction.DropDatabase;

public class DropDatabaseSqlParser extends BaseParser{

    public DropDatabaseSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(drop database)(.+?)(;)","[,]"));
    }
}
