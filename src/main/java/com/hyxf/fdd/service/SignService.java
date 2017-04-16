package com.hyxf.fdd.service;

import com.hyxf.fdd.model.DynamicTable;
import com.hyxf.fdd.model.DynamicTables;
import net.sf.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by medwin on 2017/4/11.
 * 法大大接口
 */
public interface SignService {

    /**
     * 个人 CA 申请接口
     * @param customer_name 必选 客户姓名
     * @param mobile 必选 手机号码
     * @param id_card 必选 证件号码
     * @param ident_type 可选 证件类型 0-身份证（默认值）； 1-护照； 2-军人身份证； 6-社会保障卡； A-武装警察身份证件；
     *                  B-港澳通行证； C-台湾居民来往大陆通行证； E-户口簿； F-临时居民身份证； F-临时居民身份证； P-外国人永久居留证
     * @param email 可选 电子邮箱
     * @return {result:返回结果, code:结果代码,msg:描述,customer_id:客户编号}
     */
    String syncPersonAuto(String customer_name, String mobile, String id_card, String ident_type, String email);

    /**
     * 文档传输接口
     * @param contract_id 必选 合同编号
     * @param doc_title 必选 合同标题
     * @param file 可选 PDF 文档 File 文件 doc_url 和 file 两个参数必选一
     * @param doc_url 可选 文档地址 文档地址 doc_url 和 file 两个参数必选一
     * @param doc_type 必选 文档类型 .pdf
     * @return {result:返回结果, code:结果代码,msg:描述}
     */
    String uploadDocs(String contract_id, String doc_title, File file, String doc_url, String doc_type);

    /**
     * 合同模板传输接口
     * @param template_id 必选 模板编号
     * @param file 可选 PDF 文档 File 文件 doc_url 和 file 两个参数必选一
     * @param doc_url 可选 文档地址 文档地址 doc_url 和 file 两个参数必选一
     * @return {result:返回结果, code:结果代码,msg:描述}
     */
    String uploadTemplate(String template_id, File file, String doc_url);


    /**
     * 合同生成接口
     * @param contract_id 必选 合同编号
     * @param template_id 必选 模板编号
     * @param doc_title 必选 文档标题
     * @param font_size 可选 字体大小
     * @param font_type 可选 字体类型
     * @param map 必选 填充内容
     * @param dynamic_tables 可选 动态表格
     * @return {result:返回结果, code:结果代码,msg:描述,download_url:下载地址,viewpdf_url:查看地址 }
     */
    String generateContract(String contract_id, String template_id, String doc_title, String font_size, String font_type,
                            Map<String, String> map, JSONArray dynamic_tables);

    /**
     * 文档签署接口（自动签）
     * @param customer_id 必选 客户编号
     * @param client_role 必选 客户角色
     * @param contract_id 必选  合同编号
     * @param doc_title 必选 文档标题
     * @param sign_keyword 必选 定位关键字
     * @param notify_url 可选 签署结果异步通 知 URL
     * @return {result:返回结果, code:结果代码,msg:描述,download_url:下载地址,viewpdf_url:查看地址 }
     */
    String extSignAuto(String transaction_id, String customer_id, String client_role, String contract_id,
                       String doc_title, String sign_keyword, String notify_url);

    /**
     * 合同签署状态查询接口
     * @param contract_id 必选 合同编号
     * @param customer_id 必选 客户编号
     * @return {result:返回结果, code:结果代码,msg:描述,download_url:下载地址,viewpdf_url:查看地 址,
     * transaction_id:签署交易号,sign_status:签署状态码,sign_status_des:签署状态说明 }
     */
    String querySignStatus(String contract_id, String customer_id);

    /**
     * 合同归档接口
     * @param contract_id 必选 合同编号
     * @return {result:返回结果, code:结果代码,msg:描述}
     */
    String contractFilling(String contract_id);

    /**
     * 客户信息修改接口
     * @param customer_id
     * @param email
     * @param mobile
     * @return  {result:返回结果, code:结果代码,msg:描述}
     */
    String infoChange(String customer_id, String email, String mobile);

    /**
     * 下载已签署接口
     * @param contract_id
     * @return
     */
    String downLoadContract(String contract_id);

    /**
     * 签章
     * @param customer_name
     * @param mobile
     * @param id_card
     * @param ident_type
     * @param email
     * @param contract_id
     * @param template_id
     * @param doc_title
     * @param font_size
     * @param font_type
     * @param map
     * @param dynamic_tables
     * @param coustomer_sign_keyword
     * @param company_sign_keyword
     * @return
     */
    String signSimple(String customer_name, String mobile, String id_card, String ident_type, String email, String contract_id,
                      String template_id, String doc_title, String font_size, String font_type, String map,
                      String dynamic_tables, String coustomer_sign_keyword, String company_sign_keyword,
                      String source) throws IOException;
}
