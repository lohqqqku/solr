package test.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class LuceneUtil {
	
	static String path = "";
	
	public static void index() {
//		try {
//			FSDirectory dir = FSDirectory.open(Paths.get(new File(path).toURI()));
//			Analyzer defaultAnalyzer = new StandardAnalyzer();
//			
//			new PerFieldAnalyzerWrapper(defaultAnalyzer, fieldAnalyzers)
//			IndexWriterConfig config = new IndexWriterConfig(analyzer);
//			IndexWriter writer = new IndexWriter(dir, config);
//			
//			Document doc = new Document();
//			doc.add(new TextField);
//			writer.addDocument();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
