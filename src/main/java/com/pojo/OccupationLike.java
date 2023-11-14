package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupationLike {
    private Integer id;
    private Integer userId;
    private Integer explodeId;
    private  Date createTime;
    private Integer status;
    private OccupationExplode occupationExplode;
}
