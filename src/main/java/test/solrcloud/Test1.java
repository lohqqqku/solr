package test.solrcloud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

public class Test1 extends BaseSolrCloudTest {
	
	/**
	 * 删除collection
	 */
	@Test
	public void test1() {
		try {
			CollectionAdminRequest.Delete request = new CollectionAdminRequest.Delete();
			request.setCollectionName("test1");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建collection
	 * ReplationFactor=1时不备份，=2时备份一次，=3时备份两次，以此类推
	 * 备份也算一个Shard  MaxShardPerNode>=NumberShards*ReplationFactor/Nodes
	 */
	@Test
	public void test2() {
		try {
			CollectionAdminRequest.Create request = new CollectionAdminRequest.Create();
			request.setCollectionName("test1");
			request.setNumShards(4);
			request.setReplicationFactor(3);
//			request.setAutoAddReplicas(true);
			request.setMaxShardsPerNode(6);
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载collection的配置
	 */
	@Test
	public void test3() {
		try {
			File file = new File("C:\\Users\\Administrator\\Desktop\\SolrCloudConfig");
			client.downloadConfig("test1", Paths.get(file.toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传collection配置
	 */
	@Test
	public void test4() {
		try {
			File file = new File("C:\\Users\\Administrator\\Desktop\\SolrCloudConfig");
			client.uploadConfig(Paths.get(file.toURI()), "test1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reload collection
	 * 
	 * collection的配置可以reload，jetty的东西变了需要restart
	 * 
	 */
	@Test
	public void test5() {
		try {
			CollectionAdminRequest.Reload request = new CollectionAdminRequest.Reload();
			request.setCollectionName("test1");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
