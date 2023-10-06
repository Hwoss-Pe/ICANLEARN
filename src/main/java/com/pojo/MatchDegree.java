package com.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MatchDegree {
//    private String mbti;
@JSONField(name = "EINFP")
 @JsonProperty("INFP")
    private  String INFP;
    @JsonProperty("ENFP")
    private  String ENFP;
    @JsonProperty("INFJ")
    private  String INFJ;
    @JsonProperty("ENFJ")
    private  String ENFJ;
    @JsonProperty("INTJ")
    private  String INTJ;
    @JsonProperty("INTP")
    private  String INTP;
    @JsonProperty("ENTP")
    private  String ENTP;
    @JsonProperty("ISFP")
    private  String ISFP;
    @JsonProperty("ISTP")
    private  String ISTP;
    @JsonProperty("ESTP")
    private  String ESTP;
    @JsonProperty("ISFJ")
    private  String ISFJ;
    @JsonProperty("ESFJ")
    private  String ESFJ;
    @JsonProperty("ISTJ")
    private  String ISTJ;
    @JsonProperty("ESTJ")
    private  String ESTJ;
    @JsonProperty("ENTJ")
    private  String ENTJ;
    @JsonProperty("ESFP")
    private  String ESFP;

    @JsonIgnore
    public String getINFP() {
        return INFP;
    }
    @JsonIgnore
    public String getENFP() {
        return ENFP;
    }
    @JsonIgnore
    public String getINFJ() {
        return INFJ;
    }
    @JsonIgnore
    public String getENFJ() {
        return ENFJ;
    }
    @JsonIgnore
    public String getINTJ() {
        return INTJ;
    }
    @JsonIgnore
    public String getINTP() {
        return INTP;
    }
    @JsonIgnore
    public String getENTP() {
        return ENTP;
    }
    @JsonIgnore
    public String getISFP() {
        return ISFP;
    }
    @JsonIgnore
    public String getISTP() {
        return ISTP;
    }
    @JsonIgnore
    public String getESTP() {
        return ESTP;
    }

    public void setINFP(String INFP) {
        this.INFP = INFP;
    }

    public void setENFP(String ENFP) {
        this.ENFP = ENFP;
    }

    public void setINFJ(String INFJ) {
        this.INFJ = INFJ;
    }

    public void setENFJ(String ENFJ) {
        this.ENFJ = ENFJ;
    }

    public void setINTJ(String INTJ) {
        this.INTJ = INTJ;
    }

    public void setINTP(String INTP) {
        this.INTP = INTP;
    }

    public void setENTP(String ENTP) {
        this.ENTP = ENTP;
    }

    public void setISFP(String ISFP) {
        this.ISFP = ISFP;
    }

    public void setISTP(String ISTP) {
        this.ISTP = ISTP;
    }

    public void setESTP(String ESTP) {
        this.ESTP = ESTP;
    }

    public void setISFJ(String ISFJ) {
        this.ISFJ = ISFJ;
    }

    public void setESFJ(String ESFJ) {
        this.ESFJ = ESFJ;
    }

    public void setISTJ(String ISTJ) {
        this.ISTJ = ISTJ;
    }

    public void setESTJ(String ESTJ) {
        this.ESTJ = ESTJ;
    }

    public void setENTJ(String ENTJ) {
        this.ENTJ = ENTJ;
    }

    public void setESFP(String ESFP) {
        this.ESFP = ESFP;
    }
    @JsonIgnore
    public String getISFJ() {
        return ISFJ;
    }
    @JsonIgnore
    public String getESFJ() {
        return ESFJ;
    }
    @JsonIgnore
    public String getISTJ() {
        return ISTJ;
    }
    @JsonIgnore
    public String getESTJ() {
        return ESTJ;
    }
    @JsonIgnore
    public String getENTJ() {
        return ENTJ;
    }
    @JsonIgnore
    public String getESFP() {
        return ESFP;
    }


}
