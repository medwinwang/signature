package com.hyxf.fdd.service.impl;

import com.fadada.sample.client.FddClientBase;
import com.fadada.sample.client.FddClientExtra;

import com.hyxf.fdd.model.FddLog;
import com.hyxf.fdd.service.SignService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by medwin on 2017/4/11.
 */
@Service
public class SignServiceImpl implements SignService {

    private static final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    public static final String RESULT_SUCCESS = "success";
    public static final String CODE_SUCCESS = "1000";
    public static final String RESULT_KEY = "result";
    public static final String CODE_KEY = "code";

    @Override
    public String syncPersonAuto(String customer_name, String mobile, String id_card, String ident_type, String email) {
        FddClientBase client = new FddClientBase(appId, appSecret, version, url);
        String response = client.invokeSyncPersonAuto(customer_name, email, id_card, ident_type, mobile);
        return response;
    }

    @Override
    public String uploadDocs(String contract_id, String doc_title, File file, String doc_url, String doc_type) {
        FddClientBase client = new FddClientBase(appId, appSecret, version, url);
        String response = client.invokeUploadDocs(contract_id, doc_title, file, doc_url, doc_type);
        return response;
    }

    @Override
    public String uploadTemplate(String template_id, File file, String doc_url) {
        FddClientBase client = new FddClientBase(appId, appSecret, version, url);
        String response = client.invokeUploadTemplate(template_id, file, doc_url);
        return response;
    }

    @Override
    public String generateContract(String contract_id, String template_id, String doc_title, String font_size,
                                   String font_type, Map<String, String> map, JSONArray dynamic_tables) {
        FddClientBase client = new FddClientBase(appId, appSecret, version, url);
        String response = client.invokeGenerateContract(contract_id, template_id, doc_title, font_size, font_type, map,
                dynamic_tables.toString());
        return response;
    }

    @Override
    public String extSignAuto(String transaction_id, String customer_id, String client_role, String contract_id, String doc_title,
                              String sign_keyword, String notify_url) {
        FddClientBase client = new FddClientBase(appId, appSecret, version, url);
        if("1".equals(client_role) && StringUtils.isEmpty(customer_id)) {
            customer_id = this.companyCustomerId;
        }
        String response = client.invokeExtSignAuto(transaction_id, customer_id, client_role, contract_id,
                doc_title, sign_keyword, notify_url);
        JSONObject json = JSONObject.fromObject(response);
        if(isSave && RESULT_SUCCESS.equals(json.getString(RESULT_KEY)) && CODE_SUCCESS.equals(json.getString(CODE_KEY))){
            String download_url = json.getString("download_url");
            if(StringUtils.isNotEmpty(download_url)){

                String fileName = transaction_id + ".pdf";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String path = fileLocation + File.separator + sdf.format(new Date()) + File.separator + fileName;
                FddClientExtra fddClientExtra = new FddClientExtra(appId, appSecret, version, url);
                fddClientExtra.invokeDownloadPdf(path, contract_id);
                json.put("local_download_url", path);
            }
        }
        return json.toString();
    }

    @Override
    public String querySignStatus(String contract_id, String customer_id) {
        FddClientBase client = new FddClientBase(appId, appSecret, version, url);
        String response = client.invokeQuerySignStatus(contract_id, customer_id);
        return response;
    }

    @Override
    public String contractFilling(String contract_id) {
        FddClientBase client = new FddClientBase(appId, appSecret, version, url);
        String response = client.invokeContractFilling(contract_id);
        return response;
    }

    @Override
    public String infoChange(String customer_id, String email, String mobile) {
        FddClientExtra client = new FddClientExtra(appId, appSecret, version, url);
        String response = client.invokeInfoChange(email, mobile, customer_id);
        return response;
    }

    @Override
    public String downLoadContract(String contract_id) {
        FddClientExtra fddClientExtra = new FddClientExtra(appId, appSecret, version, url);
        String fileName = contract_id + ".pdf";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String path = fileLocation + File.separator + sdf.format(new Date()) ;
        File dir = new File(path);
        if(!dir.exists()) {
            mkDir(new File(path));
        }
        path +=  File.separator + fileName;
        File file = new File(path);
        if(!file.exists()) {
            String response = fddClientExtra.invokeDownloadPdf(path, contract_id);
            if(StringUtils.isNotEmpty(response)) {
                return response;
            }
        }
        return path;
    }

    @Override
    public String signSimple(String customer_name, String mobile, String id_card, String ident_type, String email, String contract_id,
                             String template_id, String doc_title, String font_size, String font_type, String parameter_map, String dynamic_tables,
                             String coustomer_sign_keyword, String company_sign_keyword, String source) throws IOException {

        FddClientBase client = new FddClientBase(appId, appSecret, version, url);

        String response = client.invokeSyncPersonAuto(customer_name, email, id_card, ident_type, mobile);
        JSONObject json = JSONObject.fromObject(response);
        if(RESULT_SUCCESS.equals(json.getString(RESULT_KEY)) && CODE_SUCCESS.equals(json.getString(CODE_KEY))) {
            String coustomer_id = json.getString("customer_id");
            Resource resource = new ClassPathResource("pdf/" + template_id + ".pdf");
            File file = resource.getFile();
            if(!file.exists()) {
                JSONObject resultJson = new JSONObject();
                resultJson.put(RESULT_KEY, "error");
                resultJson.put(CODE_KEY, "2005");
                resultJson.put("msg", "合同模板不存在");
                return resultJson.toString();
            }
            //模板上传
            response = client.invokeUploadTemplate(template_id, file, null);
            json = JSONObject.fromObject(response);
            if((RESULT_SUCCESS.equals(json.getString(RESULT_KEY)) && "1".equals(json.getString(CODE_KEY)))
                || ("error".equals(json.getString(RESULT_KEY)) && "2002".equals(json.getString(CODE_KEY))) ) {

                response = client.invokeGenerateContract(contract_id, template_id, doc_title, font_size, font_type,
                        parameter_map != null ? JSONObject.fromObject(parameter_map) : null, dynamic_tables);
                json = JSONObject.fromObject(response);
                if(RESULT_SUCCESS.equals(json.getString(RESULT_KEY)) && CODE_SUCCESS.equals(json.getString(CODE_KEY))) {

                    //个人签
                    response = this.extSignAuto(UUID.randomUUID().toString().replaceAll("-", ""), coustomer_id,
                            "4", contract_id, doc_title, coustomer_sign_keyword, null);
                    json = JSONObject.fromObject(response);
                    if(RESULT_SUCCESS.equals(json.getString(RESULT_KEY)) && CODE_SUCCESS.equals(json.getString(CODE_KEY))) {

                        //公司签
                        response = this.extSignAuto(UUID.randomUUID().toString().replaceAll("-", ""), null,
                                "1", contract_id, doc_title, company_sign_keyword, null);
                        json = JSONObject.fromObject(response);
                        if(RESULT_SUCCESS.equals(json.getString(RESULT_KEY)) && CODE_SUCCESS.equals(json.getString(CODE_KEY))) {

                            //归档
                            response = client.invokeContractFilling(contract_id);
                            json = JSONObject.fromObject(response);
                            json.put("contract_id", contract_id);
                            response = json.toString();
                        }
                    }
                }
            }
        }
        return response;
    }

    public static void mkDir(File file){
        if(file.getParentFile().exists()){
            file.mkdir();
        }else{
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }

    private String version = "2.0";

    @Value("${fdd.url}")
    private String url;

    @Value("${fdd.app_id}")
    private String appId;

    @Value("${fdd.app_secret}")
    private String appSecret;

    @Value("${fdd.company_name}")
    private String companyName;

    @Value("${fdd.customer_id}")
    private String companyCustomerId;

    @Value("${fdd.local.path}")
    private String fileLocation;

    @Value("${fdd.local.save}")
    private boolean isSave;

    @Value("${fdd.smy.contract_id.base}")
    private String smyBase;
}
