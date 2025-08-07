package com.atguigu.study.controller;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @auther zzyybs@126.com
 * @Date 2025-05-30 11:57
 * @Description: TODO
 */
@RestController
@Slf4j
public class WanxImageModelController
{
    @Autowired
    private WanxImageModel wanxImageModel;

    // http://localhost:9006/image/create2
    @GetMapping(value = "/image/create2")
    public String createImageContent2() throws IOException
    {
        System.out.println(wanxImageModel);
        Response<Image> imageResponse = wanxImageModel.generate("美女");

        System.out.println(imageResponse.content().url());

        return imageResponse.content().url().toString();

    }




    @GetMapping(value = "/image/create3")
    public String createImageContent3() throws IOException
    {

        String prompt = "近景镜头，18岁的中国女孩，古代服饰，圆脸，正面看着镜头，" +
                "民族优雅的服装，商业摄影，室外，电影级光照，半身特写，精致的淡妆，锐利的边缘。";
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                            .apiKey(System.getenv("aliQwen-api"))
                            .model(ImageSynthesis.Models.WANX_V1)
                            .prompt(prompt)
                            .style("<watercolor>")
                            .n(1)
                            .size("1024*1024")
                        .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;

        try {
            System.out.println("---sync call, please wait a moment----");
            result = imageSynthesis.call(param);
        } catch (ApiException | NoApiKeyException e){
            throw new RuntimeException(e.getMessage());
        }


        System.out.println(JsonUtils.toJson(result));

        return JsonUtils.toJson(result);
    }
}
