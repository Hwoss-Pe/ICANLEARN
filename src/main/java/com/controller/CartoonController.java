package com.controller;


import com.pojo.Cartoon;
import com.pojo.Result;
import com.service.CartoonService;
import com.utils.Code;
import com.utils.JwtUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/cartoon")
@RestController
public class CartoonController {
    @Autowired
    HttpServletRequest req;
    @Autowired
    private CartoonService cartoonService;

    @PostMapping("/add")
    public Result addCartoon(@RequestBody Cartoon cartoon){
//        先要解析token
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        cartoon.setUserId(userId);
//添加采用覆盖的方法
        int i = cartoonService.addCartoon(cartoon);
       return i>0?Result.success(Code.CARTOON_ADD_OK):Result.error(Code.CARTOON_ADD_ERR,"保存失败");
    }

    @PostMapping()
    public Result getCartoon(){
        String token = req.getHeader("token");
//        根据id去分别获取信息，最后再进行封装
        Integer userId = JwtUtils.getId(token);
        Cartoon cartoon = cartoonService.getCartoon(userId);
        return cartoon!=null?Result.success(Code.CARTOON_GET_OK,cartoon)
                :Result.error(Code.CARTOON_GET_ERR,"获取失败");
    }

//    修改自己的绘本
    @PutMapping("/update")
    public Result updateCartoon(@RequestBody Cartoon cartoon){
//
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        cartoon.setUserId(userId);
//添加采用覆盖的方法
        int i = cartoonService.addCartoon(cartoon);
        return i>0 ? Result.success(Code.CARTOON_UPDATE_OK,"修改成功")
                :Result.error(Code.CARTOON_UPDATE_ERR,"修改失败");
    }

    @DeleteMapping()
    public Result deleteCartoon(){
        String jwt = req.getHeader("token");
//        直接删除对饮的userid的绘本
        boolean i = cartoonService.deleteCartoon(jwt);
        return i ? Result.success(Code.CARTOON_DELETE_OK,"删除成功"):
                Result.error(Code.CARTOON_DELETE_ERR,"删除失败");
    }
}
