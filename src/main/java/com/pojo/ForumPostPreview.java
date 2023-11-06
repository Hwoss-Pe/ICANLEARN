package com.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumPostPreview implements Forum{
    private Integer id;
    private String title;
    private String labels;
    private List<String> labelsList;
    private Integer publisherId;
    private Timestamp createTime;

    public void setLabels(String labels) {
        this.labels = labels;
        this.labelsList = JSONObject.parseObject(labels, new TypeReference<>() {
        });
    }

    public String getLabels() {
        if (this.labelsList == null || this.labels != null) {
            return labels;
        }
        labels = JSONObject.toJSONString(this.labelsList);
        return labels;
    }

    public List<String> getLabelsList() {
        if (this.labels == null || this.labelsList != null) {
            return labelsList;
        }
        labelsList = JSONObject.parseObject(labels, new TypeReference<>() {
        });
        return labelsList;
    }

    public void setLabelsList(List<String> labelsList) {
        this.labelsList = labelsList;
        this.labels = JSONObject.toJSONString(labelsList);
    }
}
