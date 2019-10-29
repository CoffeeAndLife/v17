package com.hgz.v17productservice;

import com.github.pagehelper.PageInfo;
import com.hgz.api.IProductService;
import com.hgz.entity.TProduct;
import com.hgz.vo.ProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17ProductServiceApplicationTests {

	@Autowired
	private IProductService productService;

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
