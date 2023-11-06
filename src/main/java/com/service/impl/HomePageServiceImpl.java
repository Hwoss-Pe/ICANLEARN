package com.service.impl;

import com.mapper.HomePageMapper;
import com.pojo.*;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service

public class HomePageServiceImpl implements HomePageService {
    @Autowired
    private HomePageMapper homePageMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private MBTITestService mbtiTestService;
    @Autowired
    private DiscService discService;
    @Autowired
    private HLDService hldService;
    @Override
    public List<Images> getImages(Integer userId) {
        return homePageMapper.getImages(userId);
    }

    @Override
    public List<String> getKeywords(Integer userId) {
        List<FriendSettings> keywords = homePageMapper.getKeywords(userId);
        List<String> keywordsList = new ArrayList<String>();
        for (FriendSettings keyword : keywords) {
            keywordsList.add(keyword.getContent());
        }
        return keywordsList;
    }

    @Override
    public WhiteHomepage getReportPage(Integer userId) {

        User user = userService.getUserById(userId);

        String mbti = user.getMbti();
        String disc = user.getDisc();
        String hld = user.getHld();
        WhiteHomepage result = new WhiteHomepage();
        if (disc != null&&!disc.equals("")) {
            Integer resultId = discService.getResultId(disc);
            DiscReport discReport = discService.getDiscReport(resultId);
            String description = discReport.getDescription();
            String[] split = description.split("、");
            List<String> DISCKeywords = Arrays.asList(split);
            result.setDISCKeywords(DISCKeywords);
        }
        if(hld != null&&!hld.equals("")) {
            List<String> HLDList = new ArrayList<String>();
            for (int i = 0; i < hld.length(); i++) {
                String value = String.valueOf(hld.charAt(i));
                HLDList.add(value);
            }
            List<String> HLDKeywords = new ArrayList<String>();
            List<HLDTestReport> hldTestReport = hldService.getHLDTestReport(HLDList);
            for (HLDTestReport hldReport : hldTestReport) {
                HLDKeywords.add(hldReport.getTypeName());
            }
            result.setHLDKeywords(HLDKeywords);
        }
        if(mbti!=null&&!mbti.equals("")) {
            List<String> MBTIKeywords = mbtiTestService.getKeywords(mbti);
            result.setMBTIKeywords(MBTIKeywords);
        }
//      画板
        List<Images> imagesList = homePageMapper.getImages(userId);
        for (Images images : imagesList) {
            if(images!=null){
                String characterKeywords = images.getCharacterKeywords();
                if (characterKeywords != null) {
                    characterKeywords = characterKeywords.replaceAll("\\\\", "");
                    images.setCharacterKeywords(characterKeywords);
                }
                String jobKeywords = images.getJobKeywords();
                if (jobKeywords != null) {
                    jobKeywords = jobKeywords.replaceAll("\\\\", "");
                }
                images.setJobKeywords(jobKeywords);

            }
        }
//获取他人关键词
        List<String> keywords = getKeywords(userId);
        result.setUser(user);
        result.setWhiteList(imagesList);
        result.setKeywords(keywords);

        return result;
    }
}
