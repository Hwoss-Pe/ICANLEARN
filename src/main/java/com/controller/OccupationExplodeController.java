package com.controller;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.pojo.OccupationExplode;
import com.pojo.Result;
import com.pojo.SearchHistory;
import com.pojo.ToDo;
import com.service.OccupationExplodeService;
import com.utils.Code;
import com.utils.JwtUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
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

    //添加计划表
    @PutMapping("/plan")
    public Result addPlan(@RequestBody ToDo toDo) {
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
//        获取具体的二维数组,前端直接传数组就行
        toDo.setUserId(userId);
        occupationExplodeService.addPlan(toDo);
        return Result.success();
    }

    @PostMapping("/finish")
    public Result finishPlan(@RequestBody Map<String, String> map){
        String coordinate = map.get("coordinate");
        String token = req.getHeader("token");
        Integer userId = JwtUtils.getId(token);
        occupationExplodeService.updatePlan(userId,coordinate);
        System.out.println(coordinate);
     return Result.success();
    }
//        这里要去判断有没有五个一线
}

