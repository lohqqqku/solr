package test.solrcloud;

import java.io.IOException;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.junit.After;
import org.junit.Before;

public class BaseSolrCloudTest {
	
	protected CloudSolrClient client;
	
	@Before
	public void before() {
		client = SolrCloudUtil.getClient(); 
	}
	
	@After
	public void after() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
