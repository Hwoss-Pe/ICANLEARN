package com.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * 论坛帖子
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPost implements Forum {

    private Integer id;//主键id
    private String title;//帖子标题
    private String words;//文字内容

    private List<String> labelsList;//帖子标签
    private String labels;//帖子标签JSON字符串

    private Integer publisherId;//发布人id
    private Integer visibleScope;//帖子可见范围 1为所有人可见 2为好友可见 -1为仅自己可见
    private List<ForumPostComment> comments;//帖子的评论

    private Timestamp createTime;//创建时间

    private Integer likeNum;//点赞数量
    private Integer collectNum;//收藏数量

    public void setLabels(String labels) {
        this.labels = labels;
        this.labelsList = JSONObject.parseObject(labels, new TypeReference<List<String>>() {
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
        labelsList = JSONObject.parseObject(labels, new TypeReference<List<String>>() {
        });
        return labelsList;
    }

    public void setLabelsList(List<String> labelsList) {
        this.labelsList = labelsList;
        this.labels = JSONObject.toJSONString(labelsList);
    }

    public void corrections() {
        if (this.labelsList == null && this.labels != null) {
            this.labelsList = JSONObject.parseObject(this.labels, new TypeReference<List<String>>() {
            });
        }

        if (this.labelsList != null && this.labels == null) {
            this.labels = JSONObject.toJSONString(this.labelsList);
        }
    }


}