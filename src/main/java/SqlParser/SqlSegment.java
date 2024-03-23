package SqlParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlSegment {
    private static final String Crlf = "|";

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getBodySplit() {
        return bodySplit;
    }

    public void setBodySplit(String bodySplit) {
        this.bodySplit = bodySplit;
    }

    public String getSegmentRegExp() {
        return segmentRegExp;
    }

    public void setSegmentRegExp(String segmentRegExp) {
        this.segmentRegExp = segmentRegExp;
    }

    public List<String> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(List<String> bodyParts) {
        this.bodyParts = bodyParts;
    }

    private String start;//sql语句的group(1)
    private String body;//sql语句的group(2)
    private String end;//sql语句的group(3)

    private String bodySplit;//对body进行切割
    private String segmentRegExp;//片段的正则表达式
    private List<String> bodyParts;

    //segmentRegExp表示sql片段的正则表达式，bodySplit用于分割body的正则表达式
    public SqlSegment(String segmentRegExp,String bodySplit){
        start = "";
        body = "";
        end = "";
        this.segmentRegExp = segmentRegExp;
        this.bodySplit = bodySplit;
        this.bodyParts = new ArrayList<String>();
    }
    //从sql中查找符合segmentRegExp的部分
    public void parse(String sql){
        Pattern pattern = Pattern.compile(segmentRegExp,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        boolean result = matcher.find();
        while(result){
            start = matcher.group(1);
            body = matcher.group(2);
            end = matcher.group(3);
            parseBody();
            result = matcher.find();
        }
    }
    //解析body
    private void parseBody(){
        List<String> list = new ArrayList<String>();
        body = body.trim();
        Pattern pattern = Pattern.compile(bodySplit,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);//在body中查找segmentRegExp的部分
        StringBuilder sb = new StringBuilder();
        boolean result = matcher.find();
        while(result){
            matcher.appendReplacement(sb,Crlf);
            result = matcher.find();
        }
        matcher.appendTail(sb);

        list.add(start);
        String[] bodyPieces = sb.toString().split("[|]");
        list.addAll(Arrays.asList(bodyPieces));
        bodyParts = list;
    }

    public String getParsedSqlSegment(){
        StringBuffer sb = new StringBuffer();
        sb.append(start + Crlf);
        for(String piece : bodyParts){
            sb.append(piece + Crlf);
        }
        return  sb.toString();
    }
}
