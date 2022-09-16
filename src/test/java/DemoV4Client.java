import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.xsxx.sms.V4Client;
import com.xsxx.sms.model.BatchSubmitResp;
import com.xsxx.sms.model.DeliverResp;
import com.xsxx.sms.model.ReportResp;
import com.xsxx.sms.model.Sms;
import com.xsxx.sms.model.template.SmsSubimtByTemplateReqVo;
import com.xsxx.sms.model.template.SmsTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 四代API提交接口
 * https://thoughts.teambition.com/share/5f1f8ebc865e26001a7536b7
 * 四代接口增加：
 * 1. 加密验证
 * 2. 防重放
 * 3. 推送验证
 *
 * @author momo
 * @date 2020-01-03 15:14:54
 */
public class DemoV4Client {

    /**
     * 并发线程数,取值范围（1-60）
     * 理论速度 = 1000/ping * threadCount
     * 默认10线程发送：理论速度500；
     * 线程数选择需要考虑客户机性能，不是越大越好
     * <p>
     * 更高的性能可以 new 多个ApiClient 协同处理短消息
     */
    private static final int httpWinSize = 20;

    public static void main(String[] args) {
        try {
//            具体【url/spId/spKey/fetchURL/templateURL】参数请找商务或者我司技术支持
            String spId,url,spKey,fetchURL,templateURL;
            V4Client v4Client = new V4Client(url, spId, spKey, httpWinSize, fetchURL, templateURL);
//            添加模板
//            addTemplate(v4Client);
//            修改模板
//            modifyTemplate(v4Client);
//            删除模板
//            deleteTemplate(v4Client);
//            查询模板状态
//            queryTemplateStatus(v4Client);
//            单模板发送
//            submitByTemplateCode(v4Client);
//            批量模板发送
            batchSubmitByTemplateCode(v4Client);
            // 单内容发送
//            submit(v4Client);
//            单内容AES
//            submitAES(v4Client);
//            多内容多号码发送 不推荐
//            batchSms(v4Client);
//            System.out.println("日统计查询：" + JSONUtil.toJsonStr(v4Client.getDailyStats("20220811")));
//            System.out.println("余额查询：" + JSONUtil.toJsonStr(v4Client.getBalance()));
            // 测速
//            testSpeed(v4Client, 1_0000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 单内容多号码发送
     * 最多100个号码，每个手机号保证是11位数字有效手机号
     * Sms sms = new Sms("11000000000","【签名】不带扩展码号");
     * Sms sms = new Sms("11000000000,11000000000","【签名】带扩展码号","666");
     * Sms sms = new Sms("11000000000,11000000000","【签名】带扩展码号,自定义字段","666","customId-whatever-except-emoji");
     * apiClient.submit(sms);
     *
     * @param v4Client
     */
    public static void submit(V4Client v4Client) {
        Sms sms = new Sms("11000000000", "【线上线下submit SDK DEMO】验证码 " + System.currentTimeMillis() + "，5分钟内有效。如非本人操作，请忽略。", "666");
        boolean isSync = v4Client.submit(sms, resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    /**
     * 报备模板
     *
     * @param v4Client
     * @throws Exception
     */
    public static void addTemplate(V4Client v4Client) throws Exception {
        SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setTemplateName("线上线下addTemplate SDK DEMO " + RandomUtil.randomString(10));
        smsTemplate.setTemplateContent("线上线下addTemplate SDK DEMO template content ${" + RandomUtil.randomString(4) + "} template " + UUID.fastUUID().toString());
        smsTemplate.setTemplateType(RandomUtil.randomInt(0, 3));
        smsTemplate.setRemark("线上线下addTemplate SDK DEMO template " + DateTime.now().toString());
        boolean isSync = v4Client.addOrModifyTemplate(true, smsTemplate, resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    /**
     * 修改模板
     *
     * @param v4Client
     * @throws Exception
     */
    public static void modifyTemplate(V4Client v4Client) throws Exception {
        SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setTemplateName("线上线下addTemplate SDK DEMO " + RandomUtil.randomString(10));
        smsTemplate.setTemplateContent("线上线下addTemplate SDK DEMO template content ${" + RandomUtil.randomString(4) + "} template ");
        smsTemplate.setTemplateType(RandomUtil.randomInt(0, 3));
        smsTemplate.setRemark("线上线下addTemplate SDK DEMO template " + DateTime.now().toString());
        smsTemplate.setTemplateCode(-7481228446574445708L);
        boolean isSync = v4Client.addOrModifyTemplate(false, smsTemplate, resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    /**
     * 删除模板
     *
     * @param v4Client
     * @throws Exception
     */
    public static void deleteTemplate(V4Client v4Client) throws Exception {
        boolean isSync = v4Client.deleteTemplate(-7481228446574445708L, resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    /**
     * 查询模板状态
     *
     * @param v4Client
     * @throws Exception
     */
    public static void queryTemplateStatus(V4Client v4Client) throws Exception {
        boolean isSync = v4Client.queryTemplateStatus(CollUtil.newArrayList(
                -7481228446574445708L, -7479127829609579659L), resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    /**
     * 短信模板单条发送
     *
     * @param v4Client
     * @throws Exception
     */
    public static void submitByTemplateCode(V4Client v4Client) throws Exception {
        SmsSubimtByTemplateReqVo smsSubimtByTemplateReqVo = new SmsSubimtByTemplateReqVo();
        smsSubimtByTemplateReqVo.setSignName("线上线下submit SDK DEMO");
        smsSubimtByTemplateReqVo.setMobile("1" + RandomUtil.randomString("0123456789", 10));
        smsSubimtByTemplateReqVo.setExtCode(RandomUtil.randomNumbers(RandomUtil.randomInt(1, 12)));
        smsSubimtByTemplateReqVo.setMsgid(UUID.randomUUID().toString(true));
        smsSubimtByTemplateReqVo.setTemplateCode(-7479043716970054794L);
        smsSubimtByTemplateReqVo.setParams(JSONUtil.toJsonStr(MapUtil.of("9ju9", RandomUtil.randomString(RandomUtil.randomInt(1, 12)))));

        boolean isSync = v4Client.submitByTemplateCode(smsSubimtByTemplateReqVo, resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    /**
     * 短信模板单条发送
     *
     * @param v4Client
     * @throws Exception
     */
    public static void batchSubmitByTemplateCode(V4Client v4Client) throws Exception {
        List<SmsSubimtByTemplateReqVo> reqVoList = new ArrayList<>();
        int count = RandomUtil.randomInt(1, 100);
        for (int i = 0; i < count; i++) {
            SmsSubimtByTemplateReqVo smsSubimtByTemplateReqVo = new SmsSubimtByTemplateReqVo();
            smsSubimtByTemplateReqVo.setSignName("线上线下submit SDK DEMO");
            smsSubimtByTemplateReqVo.setMobile("1" + RandomUtil.randomString("0123456789", 10));
            smsSubimtByTemplateReqVo.setExtCode(RandomUtil.randomNumbers(RandomUtil.randomInt(1, 12)));
            smsSubimtByTemplateReqVo.setMsgid(UUID.randomUUID().toString(true));
            smsSubimtByTemplateReqVo.setTemplateCode(RandomUtil.randomEle(CollUtil.newArrayList(
                    -7479043716970054794L, -7479127829609579659L)));
            smsSubimtByTemplateReqVo.setParams(JSONUtil.
                    toJsonStr(MapUtil.of(Pair.of("9ju9", RandomUtil.randomString(RandomUtil.randomInt(1, 12)))
                            , Pair.of("f8xk", RandomUtil.randomString(RandomUtil.randomInt(1, 12))))));
            reqVoList.add(smsSubimtByTemplateReqVo);

        }

        boolean isSync = v4Client.batchSubmitByTemplateCode(reqVoList, resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    public static void submitAES(V4Client v4Client) throws Exception {
        Sms sms = new Sms("13800000001", "【线上线下submit SDK DEMO】验证码 " + System.currentTimeMillis() + "，5分钟内有效。如非本人操作，请忽略。", "666");
        boolean isSync = v4Client.submitByAes(sms, resp -> {
            System.out.println(JSONUtil.toJsonStr(resp));
        });
    }

    /**
     * 速度测试，注意修改内容，否则会驳回
     *
     * @param v4Client
     * @param amount   测试数量
     */
    public static void testSpeed(V4Client v4Client, int amount) {
        Long phone = 11000000000l;
        long diff = 0;
        // 同步计数
        int syncCount = 0;
        Long start = System.currentTimeMillis();
        for (int i = 1; i <= amount; i++) {
            if (i % 1000 == 0) {
                diff = System.currentTimeMillis() - start;
                System.out.println(i * 100 / amount + "%\t" + (i * 1000 / diff) + "条/秒");
            }
            phone++;
            Sms sms = new Sms(phone.toString(), "【线上线下testSpeed SDK DEMO】带扩展码号" + i, String.valueOf(i), String.valueOf(i));
            boolean isSync = v4Client.submit(sms, resp -> {
                if (resp.getStatus() != 0) {
                    System.out.println(sms.getMobile() + "\t" + resp.getStatus() + "\t" + resp.getMsg());
                }
            });
            if (isSync) {
                syncCount++;
            }
        }
        diff = System.currentTimeMillis() - start;
        System.out.println("共" + diff + "秒\t " + (amount * 1000 / diff) + "条/秒");
        System.out.println("同步占比：" + syncCount * 100 / amount + "%");
    }

    /**
     * 主动获取未读短信状态
     * 成功为DELIVRD，无特殊要求一次最多返回500条，可以用msgId来匹配返回的状态
     * 可提供回调URL自动推送(推荐)
     *
     * @param v4Client
     */
    @Deprecated
    public static void getReport(V4Client v4Client) {
        ReportResp report = v4Client.getReport();
        System.out.println(JSONUtil.toJsonStr(report));
    }

    /**
     * 主动获取未读上行
     * 无特殊要求一次最多返回500条
     * 可提供回调URL自动推送(推荐)
     *
     * @param v4Client
     */
    @Deprecated
    public static void getDeliver(V4Client v4Client) {
        DeliverResp deliver = v4Client.getDeliver();
        System.out.println(JSONUtil.toJsonStr(deliver));
    }

    /**
     * 多内容打包发送接口（同步模式）最多100个内容，
     * msgId和extCode可以为空
     */
    @Deprecated
    public static void batchSms(V4Client v4Client) {
        //多内容打包数组，！！每个内容只支持1个手机号码
        List<Sms> smsContents = new ArrayList<>();
        Long phone = 18888888888L;
        for (int i = 0; i < 100; i++) {
            smsContents.add(new Sms(phone.toString(), "【线上线下batchSms SDK DEMO】内容不确定-V4 batchSms-" + i, "666", "sid-it's me"));
            phone++;
        }
        BatchSubmitResp batchSubmitResp = v4Client.submit(smsContents);
        System.out.println(JSONUtil.toJsonStr(batchSubmitResp));
    }
}
