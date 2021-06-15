package test.lucene;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class DataImport {
	
	static String url = "jdbc:mysql://192.168.5.33/licai?characterEncoding=UTF-8";
	static String username = "licai";
	static String password = "123456";
	static int pageSize = 1000;
	
	static String path = "";
	
	public static void main(String[] args) {
		URL url = DataImport.class.getResource("");
		System.out.println(url.toString());
	}
	
	public static void main2(String[] args) {
		DruidPlugin dp = new DruidPlugin(url, username, password);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		arp.setDevMode(true);
		arp.setShowSql(true);
		dp.start();
		arp.start();
		
		List<Record> list = Db.find("select * from lc_article limit 10");
		
		IndexWriter writer = null;
		try {
			FSDirectory dir = FSDirectory.open(Paths.get(new File(path).toURI()));
			Analyzer defaultAnalyzer = new SimpleAnalyzer();
			Analyzer ikAnalyzer = new IKAnalyzer();
			Map<String, Analyzer> fieldAnalyzers = new HashMap<>();
			fieldAnalyzers.put("title", ikAnalyzer);
			fieldAnalyzers.put("text", ikAnalyzer);
			fieldAnalyzers.put("keywords", ikAnalyzer);
			fieldAnalyzers.put("intro", ikAnalyzer);
			Analyzer wrapperAnalyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, fieldAnalyzers);
			IndexWriterConfig config = new IndexWriterConfig(wrapperAnalyzer);
			writer = new IndexWriter(dir, config);
			
			List<Document> docs = new ArrayList<>();
			
			list.forEach(new Consumer<Record>() {
				@Override
				public void accept(Record t) {
					int id = t.getInt("id");
					int channel_id = t.getInt("channel_id");
					int click_count = t.getInt("click_count");
					int creater = t.getInt("creater");
					int updater = t.getInt("updater");
					int tag_id = t.getInt("channel_id");
					
					String author = t.getStr("author");
					
					String title = t.getStr("title");
					String text = delHTMLTag(t.getStr("text"));
					String keywords = t.getStr("keywords");
					String intro = t.getStr("intro");
					
					Date ctime = t.getDate("ctime");
					
					Document doc = new Document();
					doc.add(new TextField("title", title, Store.YES));
					doc.add(new TextField("text", text, Store.YES));
					doc.add(new TextField("keywords", keywords, Store.YES));
					doc.add(new TextField("intro", intro, Store.YES));
					
					doc.add(new IntField("id", id, Store.YES));
					doc.add(new IntField("channel_id", channel_id, Store.YES));
					doc.add(new IntField("click_count", click_count, Store.YES));
					doc.add(new IntField("creater", creater, Store.YES));
					doc.add(new IntField("updater", updater, Store.YES));
					doc.add(new IntField("tag_id", tag_id, Store.YES));
					
					doc.add(new StringField("author", author, Store.YES));
					
					doc.add(new StringField("ctime", DateTools.dateToString(ctime, Resolution.SECOND), Store.YES));
					docs.add(doc);
				}
			});
			
			writer.addDocuments(docs);
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String delHTMLTag(String htmlStr){ 
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    } 
	
}
