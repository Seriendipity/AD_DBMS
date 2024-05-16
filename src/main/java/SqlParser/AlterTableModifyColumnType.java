package SqlParser;

public class AlterTableModifyColumnType extends BaseParser{
    public AlterTableModifyColumnType(String originalSql){
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(alter table)(.+)(modify)","[,]"));
        segments.add(new SqlSegment("(modify)(.+)(;)","[\\s]"));
    }
}
