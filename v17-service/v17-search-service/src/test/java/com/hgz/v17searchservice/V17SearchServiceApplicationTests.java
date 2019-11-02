package com.hgz.v17searchservice;

import com.hgz.api.ISearchService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TProduct;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.ClientInfoStatus;
import java.util.List;
import java.util.SortedMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SearchServiceApplicationTests {

	//目标
	//实现对solr的增删改查操作
	//solr--->数据库

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private ISearchService searchService;

	@Test
	public void synAllDataTest(){
		ResultBean resultBean = searchService.synAllData();
		System.out.println(resultBean.getData());
	}

	@Test
	public void synByIdTest(){
		ResultBean resultBean = searchService.synById(13L);
		System.out.println(resultBean.getData());
	}

	@Test
	public void querySolrTest(){
		ResultBean resultBean = searchService.queryByKeywords("初级开发工程师");
		List<TProduct> products = (List<TProduct>) resultBean.getData();
		for (TProduct product : products) {
			System.out.println(product.getName());
		}
	}

	@Test
	public void addOrUpdateTest() throws IOException, SolrServerException {
		//1.创建一个document对象
		SolrInputDocument document = new SolrInputDocument();
		//2.设置相关的属性值
		document.setField("id",2);
		document.setField("product_name","小米MIX5代");
		document.setField("product_price",19999);
		document.setField("product_sale_point","全球最高像素的手机");
		document.setField("product_images","123");
		//3.保存
		solrClient.add(document);
		solrClient.commit();
	}

	@Test
	public void queryTest() throws IOException, SolrServerException {
		//1.组装查询条件
		SolrQuery queryCondition = new SolrQuery();
		//小米MIX4代 会分词
		queryCondition.setQuery("product_name:小米MIX4代");
		//2.执行查询
		QueryResponse response = solrClient.query(queryCondition);
		//3.得到结果
		SolrDocumentList solrDocuments = response.getResults();
		for (SolrDocument document : solrDocuments) {
			System.out.println(document.getFieldValue("product_name")+"->"+document.getFieldValue("product_price"));
		}
	}

	@Test
	public void delTest() throws IOException, SolrServerException {
		//solrClient.deleteByQuery("product_name:小米MIX4代");
		solrClient.deleteById("1");
		solrClient.commit();
	}

}
