package test.solrcloud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.schema.AnalyzerDefinition;
import org.apache.solr.client.solrj.request.schema.FieldTypeDefinition;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Test2 extends BaseSolrCloudTest {
	
	/**
	 * 查看所有的field
	 */
	@Test
	public void test1() {
		try {
			SchemaRequest.Fields request = new SchemaRequest.Fields();
			NamedList<Object> nl = client.request(request, "test2");
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增field
	 */
	@Test
	public void test2() {
		/**
		 * indexed	
		 * 		If true, the value of the field can be used in queries to retrieve matching documents
		 * 		true or false	
		 * 		true
		 * stored
		 * 		If true, the actual value of the field can be retrieved by queries
		 * 		true or false
		 * 		true
		 * docValues
		 * 		If true, the value of the field will be put in a column-oriented DocValues structure
		 * 		true or false
		 * 		false
		 * sortMissingFirst,sortMissingLast
		 * 		Control the placement of documents when a sort field is not present. As of Solr 3.5, these work for all numeric fields, including Trie and date fields.
		 * 		true or false
		 * 		false
		 * multiValued
		 * 		If true, indicates that a single document might contain multiple values for this field type
		 * 		true or false
		 * 		false
		 * omitNorms
		 * 		If true, omits the norms associated with this field (this disables length normalization and index-time boosting for the field, and saves some memory). Defaults to true for all primitive (non-analyzed) field types, such as int, float, data, bool, and string. Only full-text fields or fields that need an index-time boost need norms.
		 * 		true or false
		 * 		*
		 * omitTermFreqAndPositions
		 * 		If true, omits term frequency, positions, and payloads from postings for this field. This can be a performance boost for fields that don't require that information. It also reduces the storage space required for the index. Queries that rely on position that are issued on a field with this option will silently fail to find documents. This property defaults to true for all field types that are not text fields.
		 * 		true or false
		 * 		*
		 * omitPositions
		 * 		Similar to omitTermFreqAndPositions but preserves term frequency information
		 * 		true or false
		 * 		*
		 * termVectors,termPositions,termOffsets,termPayloads
		 * 		These options instruct Solr to maintain full term vectors for each document, optionally including position, offset and payload information for each term occurrence in those vectors. These can be used to accelerate highlighting and other ancillary functionality, but impose a substantial cost in terms of index size. They are not necessary for typical uses of Solr.
		 * 		true or false
		 * 		false
		 * required	
		 * 		Instructs Solr to reject any attempts to add a document which does not have a value for this field. This property defaults to false.	
		 * 		true or false	
		 * 		false
		 */
		try {
			Map<String, Object> fieldAttrMap = new HashMap<>();
			fieldAttrMap.put("name", "name");
			fieldAttrMap.put("type", "text_ik");
			
//			fieldAttrMap.put("indexed", true);
//			fieldAttrMap.put("stored", true);
			
			SchemaRequest.AddField request = new SchemaRequest.AddField(fieldAttrMap);
			NamedList<Object> nl = client.request(request, "test2");
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建fieldType
	 */
	@Test
	public void test3() {
		try {
			AnalyzerDefinition indexAnalyzer = new AnalyzerDefinition();
			indexAnalyzer.setTokenizer(ImmutableMap.of("class"
					, "org.apache.lucene.analysis.ik.IKTokenizerFactory"
//					, "useSmart", "true"
					));
			
			AnalyzerDefinition queryAnalyzer = new AnalyzerDefinition();
			queryAnalyzer.setTokenizer(ImmutableMap.of("class"
					, "org.apache.lucene.analysis.ik.IKTokenizerFactory"
//					, "useSmart", "true"
					));
			
			List<Map<String, Object>> filters = new ArrayList<>();
			filters.add(ImmutableMap.of("class", "solr.SynonymFilterFactory"
					, "synonyms", "synonyms.txt", "ignoreCase", true));
			queryAnalyzer.setFilters(filters);
			/**
			 * 同义词过滤器
			 * <filter class="solr.SynonymFilterFactory" expand="true" ignoreCase="true" synonyms="synonyms.txt"/>
			 */
			
			
			FieldTypeDefinition fieldType = new FieldTypeDefinition();
			fieldType.setIndexAnalyzer(indexAnalyzer);
			fieldType.setQueryAnalyzer(queryAnalyzer);
			fieldType.setAttributes(ImmutableMap.of("name", "text_ik", "class", "solr.TextField"));
			SchemaRequest.AddFieldType request = new SchemaRequest.AddFieldType(fieldType);
			NamedList<Object> nl = client.request(request, "test2");
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查看所有的fieldType
	 */
	@Test
	public void test4() {
		try {
			SchemaRequest.FieldTypes request = new SchemaRequest.FieldTypes();
			NamedList<Object> nl = client.request(request, "test2");
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除fieldType
	 */
	@Test
	public void test5() {
		try {
			SchemaRequest.DeleteFieldType request = new SchemaRequest.DeleteFieldType("text_ik");
			NamedList<Object> nl = client.request(request, "test2");
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除field
	 */
	@Test
	public void test6() {
		try {
			SchemaRequest.DeleteField request = new SchemaRequest.DeleteField("content");
			NamedList<Object> nl = client.request(request, "test2");
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除copyField
	 */
	@Test
	public void test6_1() {
		try {
			SchemaRequest.DeleteCopyField request = new SchemaRequest.DeleteCopyField("title", ImmutableList.of("content"));
			NamedList<Object> nl = client.request(request, "test2");
			System.out.println(nl);
			
			SchemaRequest.DeleteCopyField request2 = new SchemaRequest.DeleteCopyField("name", ImmutableList.of("content"));
			NamedList<Object> nl2 = client.request(request2, "test2");
			System.out.println(nl2);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增copyField
	 */
	@Test
	public void test7() {
		try {
			Map<String, Object> fieldAttributes1 = new HashMap<>();
			fieldAttributes1.put("name", "content");
			fieldAttributes1.put("type", "text_ik");
			fieldAttributes1.put("multiValued", true);
			SchemaRequest.AddField request1 = new SchemaRequest.AddField(fieldAttributes1);
			NamedList<Object> nl1 = client.request(request1, "test2");
			System.out.println(nl1);
			Map<String, Object> fieldAttributes2 = new HashMap<>();
			fieldAttributes2.put("name", "title");
			fieldAttributes2.put("type", "text_ik");
			SchemaRequest.AddField request2 = new SchemaRequest.AddField(fieldAttributes2);
			NamedList<Object> nl2 = client.request(request2, "test2");
			System.out.println(nl2);
			
			
			SchemaRequest.AddCopyField request3 = new SchemaRequest.AddCopyField("title", ImmutableList.of("content"));
			NamedList<Object> nl3 = client.request(request3, "test2");
			System.out.println(nl3);
			
			SchemaRequest.AddCopyField request4 = new SchemaRequest.AddCopyField("name", ImmutableList.of("content"));
			NamedList<Object> nl4 = client.request(request4, "test2");
			System.out.println(nl4);
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//----------------------------------------动态field的分词测试--------------------------------------------//
	
	@Test
	public void test70() {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", 1);
			doc.addField("name", "我有一台电脑");
			doc.addField("title", "南无观世音菩萨");
			doc.addField("_router_", "a");
			client.add("test2", doc);
			client.commit("test2");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test80() {
		try {
			QueryResponse qr = client.query("test2", new SolrQuery("name:计算机"));
			System.out.println(qr);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test90() {
		try {
			UpdateResponse ur = client.deleteByQuery("test2", "*:*");
			client.commit("test2");
			System.out.println(ur);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
