package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhiteHomepage {
    private User user;
    List<String> keywords;
    List<String> MBTIKeywords;
    List<String> DISCKeywords;
    List<String> HLDKeywords;
    List<Images> whiteList;
}