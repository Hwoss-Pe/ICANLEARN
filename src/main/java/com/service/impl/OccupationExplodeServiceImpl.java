package com.service.impl;
import com.alibaba.fastjson2.JSON;
import com.mapper.OccupationExplodeMapper;
import com.pojo.OccupationExplode;
import com.pojo.SearchHistory;
import com.pojo.ToDo;
import com.service.OccupationExplodeService;
import io.jsonwebtoken.lang.Collections;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OccupationExplodeServiceImpl implements OccupationExplodeService {

    @Autowired
    private OccupationExplodeMapper occupationExplodeMapper;

    @Autowired
    private RestHighLevelClient client;


    //    搜索
    @Override
    public List<OccupationExplode> getOccupation(Integer userId, String keyword) {
        List<OccupationExplode> occupation = occupationExplodeMapper.getOccupation(keyword);
//        这里就要进行一个搜索记录的添加
        occupationExplodeMapper.addHistory(userId, keyword);
        return occupation;
    }


    //    联想词
    @Override
    public List<String> getOccupation(String keyword) {
        List<OccupationExplode> occupation = occupationExplodeMapper.getOccupation(keyword);

        return occupation.stream()
                .map(OccupationExplode::getJob)
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchHistory> historyList(Integer userId) {
        List<SearchHistory> searchHistories = occupationExplodeMapper.historyList(userId);
//        这里需要根据时间进行一个排序，采用最新的在前面
        searchHistories.sort(new Comparator<SearchHistory>() {
            @Override
            public int compare(SearchHistory o1, SearchHistory o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });
        return searchHistories;
    }

    @Override
    public OccupationExplode getOccupationById(Integer id) {
        return occupationExplodeMapper.getOccupationById(id);
    }

    @Override
    public List<OccupationExplode> getOccupations() {
        return occupationExplodeMapper.getOccupations();
    }


    @Override
    public List<OccupationExplode> getOccupationsByES(Integer userId, String keyword) {

        SearchRequest request = new SearchRequest("occupation_explode");
//        这需要修改query的方法，这里的根据输入的关键词去text搜索，是可以拆分的
//        request.source().query(QueryBuilders.multiMatchQuery(keyword,"info","job"));
//      这里面的就是关键词必须出现，这里只需要去调用querybs的方法

        BoolQueryBuilder builder = new BoolQueryBuilder();
//        builder.must(QueryBuilders.termQuery("job",keyword));

        builder.should(QueryBuilders.termQuery("info", keyword))
                .should((QueryBuilders.termQuery("job", keyword)));
        request.source().query(builder);


////        排序和分页，默认和mysql的limit查询差不多，都是和source同级的,这里进行一个预防
//        request.source().sort("salary", SortOrder.DESC);
//        request.source().from(10).size(10);//查询十个后的十个

//        设置高亮显示的数据，可以通过source获取，new构建起后去定义高亮字段和是否参与和匹配一样的字段，这里无脑false
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("job")
                .field("info")
                .requireFieldMatch(false);

        request.source().highlighter(highlightBuilder);

        ArrayList<OccupationExplode> list = new ArrayList<OccupationExplode>();

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            SearchHits hits = response.getHits();
            long totalHits = hits.getTotalHits().value;

            System.out.println("一共有" + totalHits + "条数据");

            SearchHit[] searchHits = hits.getHits();

            for (SearchHit hit : searchHits) {
                String json = hit.getSourceAsString();
//                这里需要去获取高亮的结果，并且覆盖掉原有的文本，传给前端<em>进行css高亮
                OccupationExplode occupationExplode = JSON.parseObject(json, OccupationExplode.class);
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                if (!Collections.isEmpty(highlightFields)) {

                    HighlightField job = highlightFields.get("job");
                    HighlightField highlightField = highlightFields.get("info");

                    if (highlightField != null) {
                        String s = highlightField.getFragments()[0].toString();
                        occupationExplode.setInfo(s);
                    }
                    if (job != null) {
//                        获取高亮值
                        String s = job.getFragments()[0].toString();
                        occupationExplode.setJob(s);
                    }
                }
                list.add(occupationExplode);
                System.out.println(occupationExplode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

//     成功搜索后添加到历史记录
        occupationExplodeMapper.addHistory(userId, keyword);
        return list;
    }

    @Override
    public List<String> getAssociate(String keyword) {

        SearchRequest request = new SearchRequest("occupation_explode");
        request.source().suggest(new SuggestBuilder().addSuggestion(
                "suggestions",
                SuggestBuilders.completionSuggestion("suggestion")
                        .prefix(keyword)
                        .skipDuplicates(true)
                        .size(10)
        ));
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Suggest suggest = response.getSuggest();
//    这个和那一大坨继承关系是等价的
        CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
        List<String> jobList = new ArrayList<>();
        for (CompletionSuggestion.Entry.Option option : suggestions.getOptions()) {
            String text = option.getText().toString();
            jobList.add(text);
        }
        return jobList;
    }


    //    以下的方法都需要进行一个加锁减少并发，采用事务回滚，后面优化加redis或者kafka进行优化
    @Override
    @Transactional
    public void addLike(Integer userId, Integer id) {

        occupationExplodeMapper.addLike(id);
    }

    @Override
    @Transactional
    public void cancelLike(Integer userId, Integer id) {
        occupationExplodeMapper.cancelLike(id);
    }

    @Override
    @Transactional
    public void addCollection(Integer userId, Integer id) {
//        对于收藏后还要进入个人主页，这里先留一part（未实现）
        occupationExplodeMapper.addCollection(id);
    }

    @Override
    @Transactional
    public void cancelCollection(Integer userId, Integer id) {
        occupationExplodeMapper.cancelCollection(id);
    }

    @Override
    public int addPlan(ToDo toDo,Integer userId) {

//        这里还需要去判断该原来有没有计划表
        ToDo plan = occupationExplodeMapper.getPlan(userId, toDo.getStage());
        if(plan!=null){
            return 0;
        }
        String s = Arrays.deepToString(toDo.getDesArray());
        toDo.setDes(s);
        String[][] finnishArray = new String[5][5];


        for (String[] strings : finnishArray) {
            Arrays.fill(strings, "0");
        }
        toDo.setFinish(Arrays.deepToString(finnishArray));

        // 先将字符串转换为一维数组

        return occupationExplodeMapper.addPlan(toDo);
    }

    @Override
    public int updatePlan(Integer userId, String coordinate,Integer stage) {

        String[] split = coordinate.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        if (x >= 5 || y >= 5) {
            return 0;
        }
//        必须根据id先获取里面的数据
        ToDo plan = occupationExplodeMapper.getPlan(userId,stage);
        String finishStr = plan.getFinish();
        String[][] finish = convert(finishStr);
        if(finish[x][y].equals("1")){
            return 0;
        }
        finish[x][y] = "1";
//        更新状态后进行去判断是否连线
        boolean bingo = isBingo(x, y, finish, 5);
        boolean bingoAll = bingoAll(finish);
        String newFinishStr = Arrays.deepToString(finish);
        plan.setFinish(newFinishStr);
        occupationExplodeMapper.updatePlan(plan);
//        1表示添加成功，0添加失败，2表示直线，3表示全部
        if(bingoAll){
            return 3;
        }
        if(bingo){
            return 2;
        }
        return 1;
    }


    @Override
    public ToDo getPlan(Integer userId,Integer stage) {
        ToDo plan = occupationExplodeMapper.getPlan(userId, stage);
        plan.setDesArray(convert(plan.getDes()));
        plan.setFinishArray(convert(plan.getFinish()));
        return plan;
    }

    //    把字符串转成二维数组
    public String[][] convert(String s) {
// 移除首尾的方括号
        String[] innerArrays = s.substring(2, s.length() - 2).split("\\], \\[");

// 构建新的二维数组
        String[][] newArray = new String[innerArrays.length][];
        for (int i = 0; i < innerArrays.length; i++) {
            String[] innerArray = innerArrays[i].split(", ");
            newArray[i] = innerArray;
        }
        return newArray;
    }

    public boolean isBingo(int x,int y ,String[][] finish,int N) {
        int count = 0;

        // 检查横向是否有 N 个相同的棋子连成一条线
        for (int i = 0; i < N ; i++) {
            count = finish[x][i].equals("1") ? count + 1 : 0;
            if (count == N) {
                return true;
            }
        }

        // 检查纵向是否有 N 个相同的棋子连成一条线
        count = 0;
        for (int i = 0; i < N; i++) {
            count = finish[i][y].equals("1") ? count + 1 : 0;
            if (count == N) {
                return true;
            }
        }

        // 检查从左上到右下的对角线是否有 N 个相同的棋子连成一条线
        count = 0;

        for (int i = 1; i < Math.min(x, y); i++) {

            count = finish[x - i][y - i].equals("1") ? count + 1 : 0;
        }
        for (int i = 0; i < N-Math.max(x,y); i++) {
            count = finish[x + i][y + i].equals("1") ? count + 1 : 0;
            if (count == N) {
                return true;
            }
        }

//     / 检查从右上到左下的对角线是否有 N 个相同的棋子连成一条线
                count = 0;
        for (int i = 1; i < Math.min(x + 1, finish.length - y); i++) {
            count = finish[x - i][y + i].equals("1") ? count + 1 : 0;
            if (count == N) {
                return true;
            }
        }
        for (int i = 0; i < N - count && x + i < finish.length && y - i >= 0; i++) {
            count = finish[x + i][y - i].equals("1") ? count + 1 : 0;
            if (count == N) {
                return true;
            }
        }

        // 如果以上都没有返回 true，说明没有连成 N 子棋
        return false;
    }



    public boolean bingoAll(String[][] finish){
        for (String[] strings : finish) {
            for (String string : strings) {
                if (string.equals("0")) {
                    return false;
                }
            }
        }
        return true;
    }
}
