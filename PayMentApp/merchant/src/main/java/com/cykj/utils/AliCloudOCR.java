package com.cykj.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class AliCloudOCR {
    /**
     * <b>description</b> :
     * <p>使用AK&amp;SK初始化账号Client</p>
     * @return Client
     *
     * @throws Exception
     */
    public static com.aliyun.teaopenapi.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(AliCloud.ak)
                .setAccessKeySecret(AliCloud.sk);
        config.endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
        return new com.aliyun.teaopenapi.Client(config);
    }

    /**
     * <b>description</b> :
     * <p>API 相关</p>
     *
     * @param path string Path parameters
     * @return OpenApi.Params
     */
    public static com.aliyun.teaopenapi.models.Params createApiInfo() throws Exception {
        com.aliyun.teaopenapi.models.Params params = new com.aliyun.teaopenapi.models.Params()
                // 接口名称
                .setAction("RecognizeIdcard")
                // 接口版本
                .setVersion("2021-07-07")
                // 接口协议
                .setProtocol("HTTPS")
                // 接口 HTTP 方法
                .setMethod("POST")
                .setAuthType("AK")
                .setStyle("V3")
                // 接口 PATH
                .setPathname("/")
                // 接口请求体内容格式
                .setReqBodyType("json")
                // 接口响应体内容格式
                .setBodyType("json");
        return params;
    }

    public static int ocr(File file) {
//        File file1 = new File("D:\\images\\"+UUID.randomUUID().toString() + ".jpg");
        com.aliyun.teaopenapi.Client client = null;
        com.aliyun.teaopenapi.models.Params params = null;
        com.aliyun.teautil.models.RuntimeOptions runtime = null;
        com.aliyun.teaopenapi.models.OpenApiRequest request = null;
        try{
//            file.transferTo(file1);
            client = AliCloudOCR.createClient();
            params = AliCloudOCR.createApiInfo();
            java.util.Map<String, Object> queries = new java.util.HashMap<>();
            queries.put("OutputFigure", true);
            queries.put("OutputQualityInfo", true);
            java.io.InputStream body = com.aliyun.darabonba.stream.Client.readFromFilePath(file.getAbsolutePath());
            // 运行时参数
            runtime = new com.aliyun.teautil.models.RuntimeOptions();
            //  请求参数
            request = new com.aliyun.teaopenapi.models.OpenApiRequest()
                    .setQuery(com.aliyun.openapiutil.Client.query(queries))
                    .setStream(body);
        }catch (Exception e){
            e.printStackTrace();
//            file1.delete();
            return 0;
        }

        // 返回值实际为 Map 类型，可从 Map 中获得三类数据：响应体 body、响应头 headers、HTTP 返回的状态码 statusCode。
        try {
            Object resp = client.callApi(params, request, runtime);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(resp));
            System.out.println(jsonObject.get("statusCode"));
            com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(resp));
            return 1;
        }catch (Exception e){
            System.out.println("识别结果为非身份证件");
            e.printStackTrace();
            return 2;
        }
//        finally {
//            file1.delete();
//        }
    }
}
