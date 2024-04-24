package SqlParser;

public class UseUserSqlParser extends BaseParser{
    public UseUserSqlParser(String originalSql){
        super(originalSql);
    }

    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(use user)(.+)(;)","[,]"));
    }

}
