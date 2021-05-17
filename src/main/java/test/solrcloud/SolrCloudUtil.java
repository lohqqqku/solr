package test.solrcloud;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

public class SolrCloudUtil {
	
	static String zkHost = "127.0.0.1:2181";
	
	public static CloudSolrClient getClient() {
		Properties props = System.getProperties();
		Enumeration<?> en = props.propertyNames();
		while(en.hasMoreElements()) {
			Object key = en.nextElement();
			Object value = props.get(key);
			System.out.println(key + "===>" + value);
		}
		return new CloudSolrClient(zkHost);
	}

}
