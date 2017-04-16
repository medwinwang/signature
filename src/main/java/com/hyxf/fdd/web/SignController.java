package com.hyxf.fdd.web;

import com.hyxf.fdd.model.DynamicTable;
import com.hyxf.fdd.model.DynamicTables;
import com.hyxf.fdd.service.SignService;
import com.hyxf.fdd.service.impl.SignServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by medwin on 2017/4/11.
 */
@RestController
@RequestMapping(value="/fdd")
public class SignController {

    @Autowired
    private SignService signService;


    @Value("${spring.http.multipart.location}")
    private String fileLocation;


    @RequestMapping(value="/signSimple")
    public String signSimple(String customer_name, String mobile, String id_card, String ident_type, String email,
                             String contract_id, String template_id, String doc_title, String font_size, String font_type,
                             String parameter_map, String dynamic_tables, String coustomer_sign_keyword,
                             String company_sign_keyword, String source) throws IOException {

        return signService.signSimple(customer_name, mobile, id_card, ident_type, email, contract_id, template_id,
                doc_title, font_size, font_type, parameter_map, dynamic_tables, coustomer_sign_keyword, company_sign_keyword,
                source);
    }



    @RequestMapping(value="/syncPersonAuto")
    public String syncPersonAuto(String customer_name, String mobile, String id_card, String ident_type, String email){

        return signService.syncPersonAuto(customer_name, mobile, id_card, ident_type, email);
    }

    @RequestMapping(value="/uploadDocs")
    public String uploadDocs(String contract_id, String doc_title, @RequestParam("file")MultipartFile multipartFile) throws IOException {

        File file = new File(fileLocation + File.separator + UUID.randomUUID() + ".pdf");
        multipartFile.transferTo(file);
        return signService.uploadDocs(contract_id, doc_title, file, null,".pdf");
    }

    @RequestMapping(value="/uploadTemplate")
    public String uploadTemplate(String template_id, @RequestParam("file")MultipartFile multipartFile) throws IOException {

        File file = new File(fileLocation + File.separator + UUID.randomUUID() + ".pdf");
        multipartFile.transferTo(file);
        return signService.uploadTemplate(template_id, file, null);
    }


    @RequestMapping(value="/generateContract")
    public String generateContract(String contract_id, String template_id, String doc_title, String font_size, String font_type,
                                   Map<String, String> map, JSONArray dynamic_tables){

        return signService.generateContract(contract_id, template_id, doc_title, font_size, font_type, map, dynamic_tables);
    }

    @RequestMapping(value="/extSignAuto")
    public String extSignAuto(String transaction_id, String customer_id, String client_role, String contract_id,
                              String doc_title, String sign_keyword, String notify_url){

        return signService.extSignAuto(transaction_id, customer_id, client_role, contract_id, doc_title, sign_keyword, notify_url);
    }

    @RequestMapping(value="/querySignStatus")
    public String querySignStatus(String contract_id, String customer_id){

        return signService.querySignStatus(contract_id, customer_id);
    }

    @RequestMapping(value="/contractFilling")
    public String contractFilling(String contract_id){

        return signService.contractFilling(contract_id);
    }

    @RequestMapping(value="/infoChange")
    public String infoChange(String customer_id, String email, String mobile){

        return signService.infoChange(customer_id, email, mobile);
    }


    @RequestMapping(value = "/downLoadContract", method = RequestMethod.GET)
    public Object downLoadContract(String contract_id)
            throws IOException {

        String filePath = signService.downLoadContract(contract_id);
        if(filePath.indexOf(SignServiceImpl.RESULT_KEY) != -1) {
            return filePath;
        }
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
                .body(new InputStreamResource(file.getInputStream()));
    }

}
