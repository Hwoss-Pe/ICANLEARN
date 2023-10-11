package com.utils;

import com.pojo.MBTIQuestion;
import com.pojo.TypeCount;

import java.util.*;

public class ExtractQuestionsUtils {

    public static List<MBTIQuestion> extractQuestions(List<MBTIQuestion> list, List<TypeCount> typeCounts, Integer num) {
        //存放最后题目的数组
        List<MBTIQuestion> newList = new ArrayList<>();
        //计算总题目数
        Integer total = 0;
        for (TypeCount typeCount : typeCounts) {
            total += typeCount.getCount();
        }
        //若请求题目数与总题目数相等则直接返回
        if (total.equals(num)) {
            Collections.shuffle(list);
            return list;
        }
        //数据库中每个类型题目的个数
        Map<String, Integer> typeCountMap = createTypeCountMap(typeCounts);
//        这里就是num/4
        int count = num / typeCountMap.size();
        int index = 0;//遍历的开始索引
//
        for (Map.Entry<String, Integer> entry : typeCountMap.entrySet()) {
            Integer value = entry.getValue();
            List<MBTIQuestion> subset = getSubset(list, index, index + value -1, count);
            newList.addAll(subset);
            index += value - 1;//更新开始索引
        }


        Collections.shuffle(newList);


        return newList;
    }


//    采用一个新List对原来的List乱序后返回切割后的List
    private static List<MBTIQuestion> getSubset(List<MBTIQuestion> originalList, int startIndex, int endIndex, int count) {
        // 创建一个用于随机排序的子列表
        List<MBTIQuestion> subList = originalList.subList(startIndex, endIndex);
        List<MBTIQuestion> shuffledList = new ArrayList<>(subList);

        // 使用Collections类的shuffle方法随机排序子列表
        Collections.shuffle(shuffledList);

        // 取出指定数量的元素
        return shuffledList.subList(0, count);
    }


//    创建题目类型和数量从对象里面获取后到Map里面
    private static Map<String, Integer> createTypeCountMap(List<TypeCount> typeCounts) {
        Map<String, Integer> map = new HashMap<>();
        for (TypeCount typeCount : typeCounts) {
            map.put(typeCount.getType(), typeCount.getCount());
        }
        return map;
    }


}