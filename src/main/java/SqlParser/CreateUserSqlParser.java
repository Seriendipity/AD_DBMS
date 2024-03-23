package SqlParser;

public class CreateUserSqlParser extends BaseParser {

    public CreateUserSqlParser(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(create user)(.+)(;)","[,]"));
    }
}
