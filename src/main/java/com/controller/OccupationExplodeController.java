package com.controller;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.pojo.*;
import com.service.OccupationExplodeService;
import com.utils.Code;
import com.utils.JwtUtils;
import com.utils.RedisUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/explode")

//先写数据库like模糊搜索
public class OccupationExplodeController {
    @Autowired
    private HttpServletRequest req;

    @Autowired
    private OccupationExplodeService occupationExplodeService;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private RedisUtil redisUtil;


    //    获取具体所有信息
    @GetMapping("/{jobId}")
    public Result getOccupation(@PathVariable("jobId") Integer id) {
        OccupationExplode occupation = occupationExplodeService.getOccupationById(id);

        return occupation != null ? Result.success(Code.Occupation_DETAIL_OK, occupation) :
                Result.error(Code.Occupation_DETAIL_ERR, "获取职业信息失败");
    }


//    下面是直接获取数据库内容方法，调试调用


    //   输入的联想词,注意联想词区别于下面的搜索不需要进行历史记录的添加，并且只显示联想
    @PostMapping("/associate")
    public Result getAssociated(@RequestBody Map<String, String> map) {
        String keyword = map.get("keyword");
        List<String> infoList = occupationExplodeService.getOccupation(keyword);
        return infoList != null ? Result.success(Code.ASSOCIATE_OK, infoList)
                : Result.error(Code.ASSOCIATE_ERR, "联想失败");

    }

    //    搜索出来的网页信息
    @PostMapping("/search")
    public Result getPage(@RequestBody Map<String, String> map) {
        String keyword = map.get("keyword");
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        List<OccupationExplode> occupationList = occupationExplodeService.getOccupation(userId, keyword);
        return occupationList != null ? Result.success(Code.SEARCH_KEYWORDS_OK, occupationList) :
                Result.error(Code.SEARCH_KEYWORDS_ERR, "搜索到的内容是null");
    }


    @GetMapping("/history")
    public Result getHistory() {
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);

        List<SearchHistory> searchHistories = occupationExplodeService.historyList(userId);
        return searchHistories != null ? Result.success(Code.SEARCH_HISTORY_OK, searchHistories)
                : Result.error(Code.SEARCH_HISTORY_ERR, "获取历史记录失败");
    }


    //批量获取通过设置匹配条件
    @PostMapping("/searchES")
    public Result getAllByMatch(@RequestBody Map<String, String> map) {
        String keyword = map.get("keyword");
        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);
        List<OccupationExplode> list = occupationExplodeService.getOccupationsByES(id, keyword);
        if (list.size() == 0) {
            Result.error(Code.SEARCH_KEYWORDS_ERR, "搜索到的内容是null");
        }
        return Result.success(Code.SEARCH_KEYWORDS_OK, list);
//
    }


    //    自动补全，前段监视输入变化
    @PostMapping("/associateES")
    public Result suggestion(@RequestBody Map<String, String> map) throws IOException {
        String keyword = map.get("keyword");

        List<String> jobList = occupationExplodeService.getAssociate(keyword);
        return jobList != null ? Result.success(Code.ASSOCIATE_ERR, jobList)
                : Result.error(Code.ASSOCIATE_ERR, "获取数据为0");
    }

    //   从数据库批量加入es
    @GetMapping("/add_all")
    public void addAll() throws IOException {
        HttpHost host = HttpHost.create("http://localhost:9200");
        RestClientBuilder builder = RestClient.builder(host);
        client = new RestHighLevelClient(builder);
        List<OccupationExplode> occupations = occupationExplodeService.getOccupations();
        BulkRequest request = new BulkRequest();

        for (OccupationExplode occupation : occupations) {
            occupation.setSuggestion();
            String jsonString = JSON.toJSONString(occupation);

            request.add(new IndexRequest("occupation_explode")
                                .id(occupation.getId().toString())
                                .source(jsonString, XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }


    //    点赞，取消点赞
    @PutMapping("/add_like")
    public Result addLike(@RequestBody Map<String, Integer> map) {
        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);
        Integer occupationId = map.get("occupationId");
        try {
            occupationExplodeService.addLike(id, occupationId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.SEARCH_LIKE_ERR, "点赞失败");
        }

        return Result.success(Code.SEARCH_LIKE_OK);
    }

    @PutMapping("/add_collection")
    public Result addCollection(@RequestBody Map<String, Integer> map) {
        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);
        Integer occupationId = map.get("occupationId");
        try {
            occupationExplodeService.addCollection(id, occupationId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.SEARCH_COLLECTION_ERR, "收藏失败");
        }

        return Result.success(Code.SEARCH_COLLECTION_OK);
    }

    //    收藏，取消收藏
    @PutMapping("/cancel_like")
    public Result cancelLike(@RequestBody Map<String, Integer> map) {
        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);
        Integer occupationId = map.get("occupationId");
        try {
            occupationExplodeService.cancelLike(id, occupationId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.SEARCH_CANCEL_LIKE_ERR, "取消点赞失败");
        }

        return Result.success(Code.SEARCH_CANCEL_LIKE_OK);
    }

    @PutMapping("/cancel_collection")
    public Result cancelCollection(@RequestBody Map<String, Integer> map) {
        String jwt = req.getHeader("token");
        Integer id = JwtUtils.getId(jwt);
        Integer occupationId = map.get("occupationId");
        try {
            occupationExplodeService.cancelCollection(id, occupationId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.SEARCH_COLLECTION_CANCEL_ERR, "取消收藏失败");
        }

        return Result.success(Code.SEARCH_COLLECTION_CANCEL_OK);

    }



    @PostMapping("/finish")
    public Result finishPlan(@RequestBody Map<String, String> map){
        String coordinate = map.get("coordinate");
        String stageStr = map.get("stage");
        int stage = Integer.parseInt(stageStr);
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
        Object bingo = occupationExplodeService.updatePlan(userId, coordinate,stage);
        if (bingo instanceof Integer) {
            int result = (int) bingo;
            switch (result) {
                case 0:
                    return Result.error(Code.FINISH_PLAN_ERR, "添加失败");
                case 1:
                    return Result.success(Code.FINISH_PLAN_OK, "添加成功");
                case 3:
                    return Result.success(Code.FINISH_PLAN_OK, 2);
                default:
                    return Result.error(Code.FINISH_PLAN_ERR, "完成任务出错");
            }
        } else if (bingo instanceof List) {
            List<int[]> resultList = (List<int[]>) bingo;
            // 根据 resultList 进行相应的处理
            return Result.success(Code.FINISH_PLAN_OK, resultList);
        } else {
            return Result.error(Code.FINISH_PLAN_ERR, "返回值类型错误");
        }
    }
//  获取计划
    @GetMapping("/my-plan/{stage}")
    public Result getPlan(@PathVariable Integer stage ){

        Integer userId = JwtUtils.getId(req.getHeader("token"));
        ToDo plan = occupationExplodeService.getPlan(userId,stage);
        return plan!=null ? Result.success(Code.GET_PLAN_OK,plan) : Result.error(Code.GET_PLAN_ERR,null);
    }


    //添加计划表
    @PutMapping("/plan")
    public Result addPlan(@RequestBody ToDo toDo) {
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
//        获取具体的二维数组,前端直接传数组就行
        toDo.setUserId(userId);
        int i = occupationExplodeService.addPlan(toDo, userId);
        //        如果原来有计划表就紧就进行更新2的操作，没有就是添加1
            if(i==1){
                return Result.success(Code.SET_PLAN_OK,"新增成功");
            }else if(i==2){
             return   Result.success(Code.UPDATE_PLAN_OK,"更新成功");
            }
        return Result.error(Code.SET_PLAN_ERR,"操作失败");
    }

////    更新计划内容
//    @PutMapping("/update")
//    public Result updatePlanDes(@RequestBody ToDo toDo){
//        String token = req.getHeader("token");
//        Integer id = JwtUtils.getId(token);
//        int i = occupationExplodeService.updatePlanDes(toDo,id);
//        return i>0?Result.success(Code.UPDATE_PLAN_OK,"更新成功"):
//                Result.error(Code.UPDATE_PLAN_ERR,"更新失败");
//    }

//价值观探索，把得到的数据进行缓存里面一小时就行，超过一小时后再把数据写进数据库
// ，这样在数据库可以保证下次进来的时候就可以保存关卡进度
//    @PostMapping("/next")
//    public Result nextProgress(@RequestBody PersonalProgress progress){
//        String token = req.getHeader("token");
//        Integer userId = JwtUtils.getId(token);
//        progress.setUserId(userId);
//    }










    @PostMapping("/result")
    public Result getDetails(@RequestBody PersonalProgress progress){
//        结果分析
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
        progress.setUserId(userId);
        List<String> values = progress.getValuesList();
        List<OccupationValues> occupationValues = occupationExplodeService.getOccupationValues(values);
        occupationExplodeService.saveProgress(progress);
        return occupationValues!=null?
                Result.success(Code.VALUE_RESULT_OK,occupationValues):
                Result.error(Code.VALUE_RESULT_ERR,"获取详细信息失败");
    }


//    点击下一关后进行保存，让下次关闭后可以进行
@PostMapping("/save")
    public Result saveProgress(@RequestBody PersonalProgress progress){
    String token = req.getHeader("token");
    Integer userId = JwtUtils.getId(token);
    progress.setUserId(userId);
    occupationExplodeService.saveProgress(progress);
        return Result.success(Code.VALUE_SAVE_OK,"保存成功");
    }
//    点击后去查看有没有内容，有的话读值，没有就不返回
    @GetMapping("/progress")
    public Result getProgress(){
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
        List<PersonalProgress> progresses = occupationExplodeService.getProgress(userId);
        if (progresses == null){
           return Result.success(Code.VALUE_GET_OK, null);
        }else {
            return Result.success(Code.VALUE_GET_OK,progresses);
        }
    }




//    //获取单个
//    @GetMapping("/1")
//    public  String GetOne() throws IOException {
//
//        GetRequest request = new GetRequest("occupation_explode");
//        request.id("2");
//        GetResponse documentFields = client.get(request, RequestOptions.DEFAULT);
//        String sourceAsString = documentFields.getSourceAsString();
//        OccupationExplode occupationExplode = JSON.parseObject(sourceAsString, OccupationExplode.class);
//        System.out.println(occupationExplode);
////        client.close();
//        return "1";
//    }
//
//    //    批量加入
//    @GetMapping("/3")
//    public  void addAllL() throws IOException {
//        HttpHost host = HttpHost.create("http://8.134.211.237:9200");
////        HttpHost host = HttpHost.create("http://localhost:9200");
//        RestClientBuilder builder = RestClient.builder(host);
//        client = new RestHighLevelClient(builder);
//        List<OccupationExplode> occupations = occupationExplodeService.getOccupations();
//        BulkRequest request = new BulkRequest();
//
//        for (OccupationExplode occupation : occupations) {
//            occupation.setSuggestion();
//            String jsonString = JSON.toJSONString(occupation);
//            request.add(new IndexRequest("occupation_explode")
//                                .id(occupation.getId().toString())
//                                .source(jsonString,XContentType.JSON));
//        }
//        client.bulk(request, RequestOptions.DEFAULT);
////        client.close();
//    }
//    //批量获取
//    @GetMapping("/4")
//    public String getAll(){
//        SearchRequest request = new SearchRequest("occupation_explode");
//        request.source().query(QueryBuilders.matchAllQuery());
//        try {
//            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
////            获取到的hit会有两种，一种是总数，一种是一个字符串数组，这
////            里方法不同
//            SearchHits hits = response.getHits();
//            //记得去获取value
//            long totalHits = hits.getTotalHits().value;
//            System.out.println("一共有"+totalHits+"条数据");
////          这里就是相当于调用了两次getHits的方法
//            SearchHit[] searchHits = hits.getHits();
//            for (SearchHit hit : searchHits) {
//                String json = hit.getSourceAsString();
//                OccupationExplode occupationExplode = JSON.parseObject(json, OccupationExplode.class);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "1";
//    }
//    //单个加入
//    @GetMapping("/2")
//    public  String addOne() throws IOException {
//        RestHighLevelClient client;
//        HttpHost host = HttpHost.create("http://localhost:9200");
//        RestClientBuilder builder = RestClient.builder(host);
//        client = new RestHighLevelClient(builder);
//        List<OccupationExplode> occupations = occupationExplodeService.getOccupations();
//        IndexRequest request = new IndexRequest("occupation_explode").id("1");
//        OccupationExplode test = occupations.get(0);
//        String s = JSON.toJSONString(test);
//        request.source(s, XContentType.JSON);
//        client.index(request, RequestOptions.DEFAULT);
////    client.close();
//        return "1";
//    }

}

