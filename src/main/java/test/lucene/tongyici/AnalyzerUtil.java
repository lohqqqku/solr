package test.lucene.tongyici;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

/** 
 * 此类描述的是：
 * @author yax 2015-1-28 下午8:42:24 
 * @version v1.0 
 */
public class AnalyzerUtil {
	
	/**
	 * 
	     * 此方法描述的是：进行中文拆分
	 */
	public static String analyzeChinese(String input, boolean userSmart) throws IOException{
		StringBuffer sb = new StringBuffer();
        StringReader reader = new StringReader(input.trim());
        IKSegmenter ikSeg = new IKSegmenter(reader, userSmart);// true　用智能分词　，false细粒度
        for (Lexeme lexeme = ikSeg.next(); lexeme != null; lexeme = ikSeg.next()) {
        	sb.append(lexeme.getLexemeText()).append(" ");
        }
        return sb.toString();
	}
	
	/**
	 * 
	     * 此方法描述的是：针对上面方法拆分后的词组进行同义词匹配，返回TokenStream
	 */
	public static TokenStream convertSynonym(String input) throws IOException{
//        Version ver = Version.LUCENE_4_10_3;
        Map<String, String> filterArgs = new HashMap<String, String>();
//        filterArgs.put("luceneMatchVersion", ver.toString());
        filterArgs.put("synonyms", "config/synonyms.txt");
        filterArgs.put("expand", "true");
        SynonymFilterFactory factory = new SynonymFilterFactory(filterArgs);
        factory.inform(new ClasspathResourceLoader());
//        Analyzer whitespaceAnalyzer = new WhitespaceAnalyzer();
//        TokenStream ts = factory.create(whitespaceAnalyzer.tokenStream("someField", input));
//        whitespaceAnalyzer.close();
        Analyzer analyzer = new IKAnalyzer();
        TokenStream ts = factory.create(analyzer.tokenStream("somefield", input));
        analyzer.close();
        return ts;
	}
	
	/**
	 * 
	     * 此方法描述的是：将tokenstream拼成一个特地格式的字符串，交给IndexSearcher来处理
	 */
    public static String displayTokens(TokenStream ts) throws IOException
    {
    	StringBuffer sb = new StringBuffer();
        CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken())
        {
            String token = termAttr.toString();
            sb.append(token).append(" ");
            System.out.print(token+"|");
//            System.out.print(offsetAttribute.startOffset() + "-" + offsetAttribute.endOffset() + "[" + token + "] ");
        }
        System.out.println();
        ts.end();
        ts.close();
        return sb.toString();
    }
    
    public static void main(String[] args) {
//    	String indexPath = "D:\\search\\test";
    	String input = "我有一台电脑";
    	System.out.println("**********************");
		try {
			System.out.println(analyzeChinese(input, true));
//			String result = displayTokens(convertSynonym(analyzeChinese(input, true)));
			String result = displayTokens(convertSynonym(input));
			System.out.println(result);
//			MyIndexer.createIndex(indexPath);
//			List<String> docs = MySearcher.searchIndex(result, indexPath);
//			for (String string : docs) {
//				System.out.println(string);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}