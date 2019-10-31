package com.hgz.v17center;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17CenterApplicationTests {

	@Autowired
	private FastFileStorageClient client;

	@Test
	public void uploadTest() throws FileNotFoundException {
		File file = new File("D:\\dev2\\v17\\v17-web\\v17-center\\1.png");
		FileInputStream fileInputStream = new FileInputStream(file);
		StorePath storePath =
				client.uploadImageAndCrtThumbImage(fileInputStream, file.length(), "png", null);
		System.out.println(storePath.getFullPath());
		System.out.println(storePath.getGroup());
		System.out.println(storePath.getPath());
	}

	@Test
	public void deleteTest(){
		client.deleteFile("group1/M00/00/00/CiQICF26lIyAGMDbAABR_7NB1m0058.png");
		System.out.println("ok");
	}

}
