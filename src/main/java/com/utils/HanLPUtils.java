package com.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * HanLP自然语言处理工具
 */
@Slf4j
@Component
public class HanLPUtils {

    @Value("${hanlp.customDictionaryPath}")
    private String customDictionaryPath;

    @Value("${hanlp.coreStopWordDictionaryPath}")
    private String coreStopWordDictionaryPath;

    @PostConstruct
    public void initialize() {
        HanLP.Config.CustomDictionaryPath = new String[]{customDictionaryPath};
        HanLP.Config.CoreStopWordDictionaryPath = coreStopWordDictionaryPath;

//        log.warn(customDictionaryPath);
//        log.warn(coreStopWordDictionaryPath);
    }

    // 提取文本中指定数量的长度大于两个字的名词
    public Set<String> extractNouns(String text, int count) {
        // 对文本进行分词和词性标注
        List<Term> termList = HanLP.segment(text);
        List<String> nouns = new ArrayList<>();

        // 遍历分词结果，筛选出名词
        for (Term term : termList) {
            // 判断词性是否为名词（n开头为名词），并且长度大于2
            if (term.nature.startsWith("n") && term.word.length() > 2) {
                nouns.add(term.word); // 将满足条件的名词加入到列表中
                // 如果达到指定数量，就结束循环
                if (nouns.size() == count) {
                    break;
                }
            }
        }

        return new HashSet<>(nouns); // 返回提取到的名词列表
    }

    // 排除形容词并提取文本中高频出现的名词
    public Set<String> extractHighFrequencyNouns(String text, int count) {
        // 对文本进行分词和词性标注
        List<Term> termList = HanLP.segment(text);
        Map<String, Integer> wordFreqMap = new HashMap<>();

        // 计算名词词频
        for (Term term : termList) {
            if (term.nature.startsWith("n") && term.word.length() > 2) {
                wordFreqMap.put(term.word, wordFreqMap.getOrDefault(term.word, 0) + 1);
            }
        }

        // 根据名词词频排序
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordFreqMap.entrySet());
        sortedList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // 提取高频名词
        List<String> highFreqNouns = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedList) {
            highFreqNouns.add(entry.getKey());
            if (highFreqNouns.size() == count) {
                break;
            }
        }

        return new HashSet<>(highFreqNouns); // 返回提取到的高频名词列表
    }
}
