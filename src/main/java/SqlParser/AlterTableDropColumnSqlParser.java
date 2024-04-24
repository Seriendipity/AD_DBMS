package SqlParser;

import java.nio.file.attribute.UserPrincipalLookupService;

public class AlterTableDropColumnSqlParser extends BaseParser{
    public AlterTableDropColumnSqlParser(String originalSql){
        super(originalSql);
    }
//alter table XX drop column XX;
    @Override
    protected void initializeSegments() {
        segments.add(new SqlSegment("(alter table)(.+)(drop column)","[,]"));
        segments.add(new SqlSegment("(drop column)(.+)(;)","[,]"));
    }

}
