package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PersonalProgress {
    private Integer id;
    private Integer userId;
    private Integer progress;
    private List<String> valuesList;
    private String explodeValues;

    public void convertValuesToValuesList() {
        if (explodeValues != null && !explodeValues.isEmpty()) {
            valuesList = new ArrayList<>();
            String[] valuesArray = explodeValues.split(",");
            valuesList.addAll(Arrays.asList(valuesArray));
        }
    }

    public void convertValuesListToValues() {
        if (valuesList != null && !valuesList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String s : valuesList) {
                sb.append(s).append(",");
            }
            explodeValues = sb.deleteCharAt(sb.length() - 1).toString();
        }
    }
}