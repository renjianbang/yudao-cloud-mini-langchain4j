//package cn.iocoder.yudao.module.travel.controller.admin.init;
//
//import cn.iocoder.yudao.framework.common.pojo.CommonResult;
//import cn.iocoder.yudao.module.travel.util.DatabaseInitUtil;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
//
//@Tag(name = "管理后台 - 数据库初始化")
//@RestController
//@RequestMapping("/admin-api/travel/init")
//@Slf4j
//public class DatabaseInitController {
//
//    @Autowired
//    private DatabaseInitUtil databaseInitUtil;
//
//    @PostMapping("/database")
//    @Operation(summary = "初始化数据库")
//    public CommonResult<String> initDatabase() {
//        try {
//            databaseInitUtil.initDatabase();
//            return success("数据库初始化成功");
//        } catch (Exception e) {
//            log.error("数据库初始化失败", e);
////            return CommonResult.error("数据库初始化失败: " + e.getMessage());
//        }
//    }
//}