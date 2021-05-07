package test.solrcloud;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

public class SolrCloudUtil {
	
	static String zkHost = "127.0.0.1:9983";
	
	public static CloudSolrClient getClient() {
		return new CloudSolrClient(zkHost);
	}

}
