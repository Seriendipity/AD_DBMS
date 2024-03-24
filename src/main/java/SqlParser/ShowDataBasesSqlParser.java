package SqlParser;

import SqlFunction.ShowDatabases;

public class ShowDataBasesSqlParser extends BaseParser{
    public ShowDataBasesSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(show databases)(.+)(;)","[,]"));
    }
}
