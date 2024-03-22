package SqlParser;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseParser {
    protected String originalSql;//原始的sql语句

    protected List<SqlSegment> segments;

    public BaseParser(String originalSql){
        this.originalSql = originalSql;
        segments = new ArrayList<SqlSegment>();
        initializeSegments();
    }

    //强制子类实现
    protected abstract void initializeSegments();

    //将originalSql拆分成一个一个片段
    public List<List<String>> splitSqlSegment(){
        List<List<String>> list = new ArrayList<List<String>>();
        for(SqlSegment sqlParts : segments){
            sqlParts.parse(originalSql);
            list.add(sqlParts.getBodyParts());
        }
        return list;
    }

    //得到解析完的sql语句

    public String getParsedSql() {
        StringBuffer sb = new StringBuffer();
        for(SqlSegment sqlPart : segments){
            sb.append(sqlPart.getParsedSqlSegment() + "n");
        }
        String result = sb.toString().replaceAll("n+","n");
        return result;
    }
}
