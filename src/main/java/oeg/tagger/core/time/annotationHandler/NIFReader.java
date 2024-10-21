package oeg.tagger.core.time.annotationHandler;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that reads NIF
 * 
 * @author mnavas
 */
public class NIFReader {
    
    private static final Logger LOGGER = Logger.getLogger(TIMEX2JSON.class.getName());

    /**
     * Initializes a instance of the converter
     *
     * @return an instance of the converter
     */
    public NIFReader() {
        init();
    }

    public void init() {

    }

    /**
     * Parses NIF
     *
     * @param input String in NIFformat
     * @return the structure NIFText
     */
    public NIFText readNIF(String input) {
        NIFText nt = new NIFText();
        
        String inp2 = input;
        
        while (!inp2.isEmpty()) {
                String pattern = "(\\s*@[^>]+>\\s*)+";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(inp2);
                StringBuffer sb = new StringBuffer(inp2.length());
                if (m.find()) {
                    nt.header = nt.header + m.group(1);
                    m.appendReplacement(sb, "");
                    m.appendTail(sb);
                    inp2 = sb.toString();
                } else{
                    pattern = "<([^>]*)>((\\s*[^<][^;.]*[;.])\\s*)*";
                    p = Pattern.compile(pattern);
                    m = p.matcher(inp2);
                    sb = new StringBuffer(inp2.length());
                    if (m.find()) {
                        if((m.group().contains("lkg:LynxDocument") || m.group().contains("nif:Context")) && m.group().contains("nif:isString")){
                            String intpart = m.group();
                            String pattern2 = "\\s*nif:isString\\s*\\\"(.*)\\\"";
                            Pattern p2 = Pattern.compile(pattern2);
                            Matcher m2 = p2.matcher(intpart);
                            if (m2.find()) {
                                nt.text = m2.group(1);
                                nt.reference = m.group(1);
                                return nt;
                            }                            
                        }
                        m.appendReplacement(sb, "");
                        m.appendTail(sb);
                        inp2 = sb.toString();
                    } else{
                        return null;
                    }
                }
        }
        
        return nt;
    }
}
