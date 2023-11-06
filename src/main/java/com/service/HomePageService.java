package com.service;

import com.pojo.FriendSettings;
import com.pojo.Images;
import com.pojo.WhiteHomepage;

import java.util.List;

public interface HomePageService {
    List<Images> getImages(Integer userId);

    List<String> getKeywords(Integer userId);

    WhiteHomepage getReportPage(Integer userId);
}