package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OccupationCollection {
    private Integer id;
    private Integer userId;
    private Integer explodeId;
    private Date createTime;
    private Integer status;
}