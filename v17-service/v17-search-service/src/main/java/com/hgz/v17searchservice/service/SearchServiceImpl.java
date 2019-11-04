package com.hgz.v17searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ISearchService;
import com.hgz.commons.pojo.PageResultBean;
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
import java.util.Map;

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

        //product_keywords
        //name:华为
        //name:一亿像素的手机 sale_point:华为品质保证
        //ADD1 添加高亮的设置
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        //queryCondition.addHighlightField("product_sale_point");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //2.获取结果，并将结果转换为List
        List<TProduct> list = null;
        try {
            QueryResponse response = solrClient.query(queryCondition);
            SolrDocumentList results = response.getResults();
            //results -> List
            list = new ArrayList<>(results.size());

            //ADD2 获取高亮的信息
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //华为
            //1001-华为1 1002-华为2
            //外层Map key（String）--1001
            //外层Map value---1001记录对应的高亮信息(包含多个字段的高亮信息)

            //为什么里面又是一个map？
            //里层Map key（String）---字段名（product_name）
            //里层Map value----字段对应的高亮信息
            for (SolrDocument document : results) {
                //document -> product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setImages(document.getFieldValue("product_images").toString());
                //TODO 传递product对象不合适，因为页面展示只需要4个字段

                //设置product_name高亮信息
                //1.获取当前记录的高亮信息
                Map<String, List<String>> higlight = highlighting.get(document.getFieldValue("id").toString());
                //2.获取字段对应的高亮信息
                List<String> productNameHiglight = higlight.get("product_name");
                //3.判断该字段是否有高亮信息
                if(productNameHiglight != null && productNameHiglight.size() > 0){
                    //高亮信息
                    product.setName(productNameHiglight.get(0));
                }else {
                    //普通的文本信息
                    product.setName(document.getFieldValue("product_name").toString());
                }

                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("500",null);
        }
        return new ResultBean("200",list);
    }

    @Override
    public ResultBean queryByKeywords(String keywords, Integer pageIndex, Integer pageSize) {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        if(keywords == null || "".equals(keywords.trim())){
            queryCondition.setQuery("product_name:华为");
        }else{
            queryCondition.setQuery("product_name:"+keywords);
        }

        //product_keywords
        //name:华为
        //name:一亿像素的手机 sale_point:华为品质保证
        //ADD1 添加高亮的设置
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        //queryCondition.addHighlightField("product_sale_point");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //添加分页的设置
        //从哪个下标开始
        queryCondition.setStart((pageIndex-1)*pageSize);
        queryCondition.setRows(pageSize);

        //2.获取结果，并将结果转换为List
        List<TProduct> list = null;

        //List--> pageResultBean
        PageResultBean<TProduct> pageResultBean = new PageResultBean<>();
        //
        long total = 0L;

        try {
            QueryResponse response = solrClient.query(queryCondition);
            SolrDocumentList results = response.getResults();
            //results -> List
            list = new ArrayList<>(results.size());

            //ADD2 获取高亮的信息
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //华为
            //1001-华为1 1002-华为2
            //外层Map key（String）--1001
            //外层Map value---1001记录对应的高亮信息(包含多个字段的高亮信息)

            //为什么里面又是一个map？
            //里层Map key（String）---字段名（product_name）
            //里层Map value----字段对应的高亮信息
            total = results.getNumFound();
            //
            for (SolrDocument document : results) {
                //document -> product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setImages(document.getFieldValue("product_images").toString());
                //TODO 传递product对象不合适，因为页面展示只需要4个字段

                //设置product_name高亮信息
                //1.获取当前记录的高亮信息
                Map<String, List<String>> higlight = highlighting.get(document.getFieldValue("id").toString());
                //2.获取字段对应的高亮信息
                List<String> productNameHiglight = higlight.get("product_name");
                //3.判断该字段是否有高亮信息
                if(productNameHiglight != null && productNameHiglight.size() > 0){
                    //高亮信息
                    product.setName(productNameHiglight.get(0));
                }else {
                    //普通的文本信息
                    product.setName(document.getFieldValue("product_name").toString());
                }

                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("500",null);
        }
        //给pageResultBean赋值
        pageResultBean.setList(list);
        pageResultBean.setPageNum(pageIndex);
        pageResultBean.setPageSize(pageSize);
        pageResultBean.setTotal(total);
        pageResultBean.setPages((int) (total%pageSize==0?total/pageSize:(total/pageSize)+1));
        pageResultBean.setPageNum(3);

        return new ResultBean("200",pageResultBean);
    }
}
