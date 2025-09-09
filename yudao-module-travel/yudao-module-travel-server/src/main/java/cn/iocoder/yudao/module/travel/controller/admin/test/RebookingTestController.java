//package cn.iocoder.yudao.module.travel.controller.admin.test;
//
//import cn.iocoder.yudao.framework.common.pojo.CommonResult;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.web.bind.annotation.*;
//
//import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
//
//@Tag(name = "管理后台 - 改签测试")
//@RestController
//@RequestMapping("/admin-api/studio/test")
//public class RebookingTestController {
//
//    @GetMapping("/ping")
//    @Operation(summary = "测试接口连通性")
//    public CommonResult<String> ping() {
//        return success("改签模块接口正常");
//    }
//
//    @PostMapping("/database-status")
//    @Operation(summary = "检查数据库表状态")
//    public CommonResult<String> checkDatabaseStatus() {
//        try {
//            // 这里可以添加检查数据库表是否存在的逻辑
//            return success("数据库表检查正常");
//        } catch (Exception e) {
////            return CommonResult.error("数据库表检查失败: " + e.getMessage());
//        }
//    }
//
//}