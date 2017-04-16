package com.hyxf.fdd.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by medwin on 2017/4/11.
 */
@Entity
@Table(name = "fdd_log")
public class FddLog  implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "function_name")
    private String functionName;

    @Column(name = "customer_name")
    @Length(min=1,max=32,message="客户姓名长度范围1到32")
    private String customer_name;

    @Column(name = "email")
    @Length(max=64,message="email长度不能超过64")
    private String email;

    @Column(name = "mobile")
    @Length(min=1,max=11,message="mobile长度长度范围1到11")
    private String mobile;

    @Column(name = "id_card")
    @Length(min=1,max=20,message="证件号码长度范围1到20")
    private String id_card;

    @Column(name = "ident_type")
    @Length(max=1,message="证件类型长度不能超过1")
    private String ident_type;

    @Column(name = "contract_id")
    @Length(max=32,message="合同编号长度不能超过32")
    private String contract_id;

    @Column(name = "template_id")
    @Length(min = 1,max=32,message="模板编号长度范围1到32")
    private String template_id;

    @Column(name = "doc_title")
    @Length(min = 1, max=64,message="文档标题长度范围1到64")
    private String doc_title;

    @Column(name = "font_size")
    @Length(max=2,message="字体大小长度不能超过2")
    private String font_size;

    @Column(name = "font_type")
    @Length(max=1,message="字体类型长度不能超过1")
    private String font_type;

    @Column(name = "parameter_map")
    private String parameter_map;

    @Column(name = "dynamic_tables")
    private String dynamic_tables;

    @Column(name = "coustomer_sign_keyword")
    @Length(min=1,max=32,message="客户签定位关键字长度范围1到32")
    private String coustomer_sign_keyword;

    @Column(name = "company_sign_keyword")
    @Length(min=1,max=32,message="公司签定位关键字长度范围1到32")
    private String company_sign_keyword;

    //1 萨摩耶 2 app
    @Column(name = "source")
    @Length(min=1,max=2,message="source长度范围1到2")
    private String source;

    @Column(name = "response")
    private String response;

    @Column(name = "request_time")
    private Date requestTime;

    @Column(name = "responese_time")
    private Date responeseTime;

    @Column(name = "ip")
    private String ip;

    public static final String SOURCE_SMY = "1";
    public static final String SOURCE_APP = "2";



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponeseTime() {
        return responeseTime;
    }

    public void setResponeseTime(Date responeseTime) {
        this.responeseTime = responeseTime;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getIdent_type() {
        return ident_type;
    }

    public void setIdent_type(String ident_type) {
        this.ident_type = ident_type;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getDoc_title() {
        return doc_title;
    }

    public void setDoc_title(String doc_title) {
        this.doc_title = doc_title;
    }

    public String getFont_size() {
        return font_size;
    }

    public void setFont_size(String font_size) {
        this.font_size = font_size;
    }

    public String getFont_type() {
        return font_type;
    }

    public void setFont_type(String font_type) {
        this.font_type = font_type;
    }

    public String getParameter_map() {
        return parameter_map;
    }

    public void setParameter_map(String parameter_map) {
        this.parameter_map = parameter_map;
    }

    public String getDynamic_tables() {
        return dynamic_tables;
    }

    public void setDynamic_tables(String dynamic_tables) {
        this.dynamic_tables = dynamic_tables;
    }

    public String getCoustomer_sign_keyword() {
        return coustomer_sign_keyword;
    }

    public void setCoustomer_sign_keyword(String coustomer_sign_keyword) {
        this.coustomer_sign_keyword = coustomer_sign_keyword;
    }

    public String getCompany_sign_keyword() {
        return company_sign_keyword;
    }

    public void setCompany_sign_keyword(String company_sign_keyword) {
        this.company_sign_keyword = company_sign_keyword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
