package test.solrcloud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class Test1 extends BaseSolrCloudTest {
	
	/**
	 * 删除collection
	 */
	@Test
	public void test1() {
		try {
			CollectionAdminRequest.Delete request = new CollectionAdminRequest.Delete();
			request.setCollectionName("test2");
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
			request.setCollectionName("test2");
			
//			request.setNumShards(4);
//			request.setReplicationFactor(3);
//			request.setAutoAddReplicas(true);
//			request.setMaxShardsPerNode(6);
			
			/**
			 * 路由的两种方式
			 * implicit：指定shard
			 * compositeId：哈希算法分布（默认）
			 */
			request.setRouterName("implicit");
			request.setRouterField("_router_");
			request.setShards("a,b,c,d");
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
			File file = new File("C:\\Users\\Administrator\\Desktop\\SolrCloudConfig\\data_driven_schema_configs\\conf");
			client.uploadConfig(Paths.get(file.toURI()), "test2");
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
			request.setCollectionName("test2");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 给collection增加shard，只支持implicit的路由模式
	 */
	@Test
	public void test6() {
		try {
			CollectionAdminRequest.CreateShard request = new CollectionAdminRequest.CreateShard();
			request.setCollectionName("test2");
			request.setShardName("temp");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加备份分片
	 * 添加时会验证maxShardsOfNode
	 * 支持两种路由
	 */
	@Test
	public void test7() {
		try {
			CollectionAdminRequest.AddReplica request = new CollectionAdminRequest.AddReplica();
			request.setCollectionName("test1");
			request.setShardName("shard1_1");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * split shard分片的分裂，专为compositeId类型路由的分片进行增加分片的处理
	 * 将原有分片的数据平均分成两个分片，之后就可以删除原有的分片
	 */
	@Test
	public void test8() {
		try {
			CollectionAdminRequest.SplitShard request = new CollectionAdminRequest.SplitShard();
			request.setCollectionName("test1");
			request.setShardName("shard1");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除分片
	 */
	@Test
	public void test9() {
		try {
			CollectionAdminRequest.DeleteShard request = new CollectionAdminRequest.DeleteShard();
			request.setCollectionName("test1");
			request.setShardName("shard1");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除备份 replica
	 */
	@Test
	public void test10() {
		try {
			CollectionAdminRequest.DeleteReplica request = new CollectionAdminRequest.DeleteReplica();
			request.setCollectionName("test1");
			request.setShardName("shard4");
			request.setReplica("core_node9");
			NamedList<Object> nl = client.request(request);
			System.out.println(nl);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查看集群情况
	 */
	@Test
	public void test11() {
		try {
			CollectionAdminRequest.ClusterStatus request = new CollectionAdminRequest.ClusterStatus();
			request.setCollectionName("test1");
			NamedList<Object> nl = client.request(request);
			String str = JSON.toJSONString(nl.asMap(Integer.MAX_VALUE), true);
			System.err.println(str);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
