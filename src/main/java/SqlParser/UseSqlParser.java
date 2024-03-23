package SqlParser;

public class UseSqlParser extends BaseParser{
    public UseSqlParser(String originalSql){
        super(originalSql);
    }

    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(use)(.+)(;)","[,]"));
    }
}
