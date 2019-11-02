package com.hgz.v17productservice;

import com.github.pagehelper.PageInfo;
import com.hgz.api.IProductService;
import com.hgz.api.IProductTypeService;
import com.hgz.entity.TProduct;
import com.hgz.entity.TProductType;
import com.hgz.vo.ProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17ProductServiceApplicationTests {

	@Autowired
	private IProductService productService;

	@Autowired
	private IProductTypeService productTypeService;

	@Autowired
	private DataSource dataSource;


	@Test
	public void poolTest() throws SQLException {
		System.out.println(dataSource.getConnection());
	}

	@Test
	public void listTypeTest(){
		List<TProductType> list = productTypeService.list();
		for (TProductType productType : list) {
			System.out.println(productType.getName());
		}
	}

	@Test
	public void contextLoads() {
		TProduct product = productService.selectByPrimaryKey(1L);
		System.out.println(product.getName());
	}

	@Test
	public void listTest(){
		List<TProduct> list = productService.list();
		for (TProduct tProduct : list) {
			System.out.println(tProduct.getName());
		}
		Assert.assertNotNull(list);
	}

	@Test
	public void pageTest(){
		PageInfo<TProduct> page = productService.page(1, 1);
		System.out.println(page.getList().size());
		Assert.assertEquals(page.getList().size(),1);
	}

	@Test
	public void addVoTest(){
		TProduct product = new TProduct();
		product.setName("格力手机");
		product.setPrice(3999L);
		product.setSalePoint("好用");
		product.setSalePrice(3666L);
		product.setImages("123");
		product.setTypeId(1);
		product.setTypeName("电子数码");
		ProductVO vo = new ProductVO();
		vo.setProduct(product);
		vo.setProductDesc("怎么摔都没问题");

		System.out.println(productService.add(vo));
	}

}
