package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HLDTestReport {
    private String typeName;//测试类型名
    private String type;//测试类型
    private String commonCharacter;//典型特征
    private String typicalJob;//普遍工作
}
