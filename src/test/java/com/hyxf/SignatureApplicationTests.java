package com.hyxf;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignatureApplicationTests {

	private String path = "http://localhost:8080/fdd";

	private String customer_id = "C688923F17E5886BCB9FF6A7F638CBB5";

	private String company_name = "杭银消费金融股份有限公司";


	@Test
	public void signSimple() throws IOException {
		RestTemplate rest = new RestTemplate();

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("customer_name","汪汪"); //客户姓名
		param.add("mobile", "15824436258"); //手机号
		param.add("id_card", "429006199003054916"); //身份证号
//		param.add("contract_id", "test005");//合同编号
		param.add("template_id", "test");//模板编号
		param.add("doc_title", "授权模板");//合同名称
		param.add("parameter_map", "{\"borrower\":\"小明\",\"platformName\":\"法大大\"}"); //模板参数
		//表格参数
		param.add("dynamic_tables", "[{\"pageBegin\":2,\"headers\":[\"学号\",\"姓名\",\"班级\"],\"datas\":[[\"1\",\"小明 1\",\"三年 1 班 \"]]},{\"pageBegin\":2,\"headers\":[\" 序号\",\" 借 款人\",\"贷款人\",\"金额\"],\"datas\":[[\"1\",\"小 明 1\",\"小风 1\",\"1000\"],[\"2\",\"小明 2\",\"小风 2\",\"2000\"]]}]");
		//客户盖章位置
		param.add("coustomer_sign_keyword", "委托方签字（或盖章）：");
		//公司盖章位置
		param.add("company_sign_keyword", "受托方签字（或盖章）：");
		param.add("source", "1");


		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);
		String string = rest.postForObject( path + "/signSimple", httpEntity, String.class);
		System.out.println(string);
	}

	@Test
	public void syncPersonAuto() throws IOException {
		RestTemplate rest = new RestTemplate();

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("customer_name","汪汪2222222");
		param.add("mobile", "15824436251");
		param.add("id_card", "420902198311015979");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);

		String string = rest.postForObject( path + "/syncPersonAuto", httpEntity, String.class);
		//5A6377635CF1CE48CB9FF6A7F638CBB5
		System.out.println(string);
	}

	@Test
	public void uploadDocs() throws IOException {
		RestTemplate rest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/pdf; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Content-Type","application/pdf; charset=UTF-8");


		Resource resource = new ClassPathResource("test.pdf");
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("file", new FileSystemResource(resource.getFile()));
		param.add("contract_id", "test001");
		param.add("doc_title", "授权模板");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);

		String string = rest.postForObject( path + "/uploadDocs", httpEntity, String.class);
		System.out.println(string);
	}


	@Test
	public void extSignAuto() throws IOException {
		//个人签
		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("transaction_id", UUID.randomUUID().toString().replaceAll("-", ""));
		param.add("customer_id", "5A6377635CF1CE48CB9FF6A7F638CBB5");
		param.add("client_role", "4");
		param.add("contract_id", "test001");
		param.add("doc_title", "授权模板");
		param.add("sign_keyword", "委托方签字（或盖章）：");
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);
		String string = rest.postForObject(path + "/extSignAuto", httpEntity, String.class);
		System.out.println(string);

		//公司签
		param = new LinkedMultiValueMap<>();
		param.add("transaction_id", UUID.randomUUID().toString().replaceAll("-", ""));
		param.add("customer_id", customer_id);
		param.add("client_role", "1");
		param.add("contract_id", "test001");
		param.add("doc_title", "授权模板");
		param.add("sign_keyword", "受托方签字（或盖章）：");
		httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);
		string = rest.postForObject(path + "/extSignAuto", httpEntity, String.class);
		System.out.println(string);
	}


	@Test
	public void contractFilling() throws IOException {
		RestTemplate rest = new RestTemplate();

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("contract_id", "test001");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);

		String string = rest.postForObject( path + "/contractFilling", httpEntity, String.class);
		System.out.println(string);
	}

	@Test
	public void downLoadContract() throws IOException {
		ArrayList params = new ArrayList();
		params.add(new BasicNameValuePair("contract_id", "test001"));
//		HttpsUtil.doPostDownload(path, path + "/downLoadContract", params);

	}
}
