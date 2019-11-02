package com.hgz.v17searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ISearchService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TProduct;
import com.hgz.mapper.TProductMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangguizhao
 */
@Service
public class SearchServiceImpl implements ISearchService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean synAllData() {
        //1.查询源数据  合适吗？
        //TODO 只查询需要的数据 25
        List<TProduct> list = productMapper.list();
        //2.MySQL-》solr
        for (TProduct product : list) {
            //product -> docuement
            SolrInputDocument document = new SolrInputDocument();
            //2.设置相关的属性值
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_price",product.getPrice());
            document.setField("product_sale_point",product.getSalePoint());
            document.setField("product_images",product.getImages());
            //3.保存
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                //TODO log
                return new ResultBean("500","同步数据失败！");
            }
        }
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean("500","同步数据失败！");
        }
        return new ResultBean("200","同步数据成功！");
    }

    @Override
    public ResultBean synById(Long id) {
        //1.查询源数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        //2.MySQL-》solr
        //product -> docuement
        SolrInputDocument document = new SolrInputDocument();
        //2.设置相关的属性值
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_sale_point",product.getSalePoint());
        document.setField("product_images",product.getImages());
        //3.保存
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean("500","同步数据失败！");
        }
        return new ResultBean("200","同步数据成功！");
    }

    @Override
    public ResultBean delById(Long id) {
        try {
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean("500","同步数据失败！");
        }
        return new ResultBean("200","同步数据成功！");
    }

    @Override
    public ResultBean queryByKeywords(String keywords) {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        if(keywords == null || "".equals(keywords.trim())){
            queryCondition.setQuery("product_name:华为");
        }else{
            queryCondition.setQuery("product_name:"+keywords);
        }
        //2.获取结果，并将结果转换为List
        List<TProduct> list = null;
        try {
            QueryResponse response = solrClient.query(queryCondition);
            SolrDocumentList results = response.getResults();
            //results -> List
            list = new ArrayList<>(results.size());
            for (SolrDocument document : results) {
                //document -> product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setImages(document.getFieldValue("product_images").toString());
                //TODO 传递product对象不合适，因为页面展示只需要4个字段
                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("500",null);
        }
        return new ResultBean("200",list);
    }
}
