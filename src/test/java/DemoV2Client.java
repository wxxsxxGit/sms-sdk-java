import com.alibaba.fastjson.JSON;
import com.xsxx.sms.V2Client;
import com.xsxx.sms.model.BatchSubmitResp;
import com.xsxx.sms.model.DeliverResp;
import com.xsxx.sms.model.ReportResp;
import com.xsxx.sms.model.Sms;

import java.util.ArrayList;
import java.util.List;

/**
 * 二代API提交接口
 * http://doc.wxxsxx.com/wiki/api/single_send.html
 *
 * @author momo
 * @date 2020-01-03 15:14:54
 */
public class DemoV2Client {

    /**
     * 并发线程数,取值范围（1-60）
     * 理论速度 = 1000/ping * threadCount
     * 默认10线程发送：理论速度500；
     * 线程数选择需要考虑客户机性能，不是越大越好
     * <p>
     * 更高的性能可以 new 多个SmsClient 协同处理短消息
     */
    private static final int httpWinSize = 20;

    public static void main(String[] args) {
        try {
            // http://report-t.onmsg.cn/
            // com.xsxx.sms.SmsClient
            V2Client v2Client = new V2Client("http://api-t.onmsg.cn/", "xiashuaihttp", "123456", httpWinSize);

            // 单内容发送
            submit(v2Client);
            // 测速
//            testSpeed(v2Client, 1_0000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 单内容多号码发送
     * 最多100个号码，每个手机号保证是11位数字有效手机号
     * Sms sms = new Sms("19900000000","【签名】不带扩展码号");
     * Sms sms = new Sms("19900000000,199000000001","【签名】带扩展码号","666");
     * smsClient.submit(sms);
     *
     * @param v2Client
     */
    public static void submit(V2Client v2Client) {
        Sms sms = new Sms("11000000000", "【签名】验证码 " + System.currentTimeMillis() + "，5分钟内有效。如非本人操作，请忽略。", String.valueOf(System.currentTimeMillis()));
        boolean isSync = v2Client.submit(sms, resp -> {
            System.out.println(JSON.toJSONString(resp));
        });
    }

    /**
     * 速度测试，注意修改内容，否则会驳回
     *
     * @param v2Client
     * @param amount   测试数量
     */
    public static void testSpeed(V2Client v2Client, int amount) {
        Long phone = 13921350591L;
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
            Sms sms = new Sms(phone.toString(), "【天猫3】带扩展码号" + i, String.valueOf(i));
            boolean isSync = v2Client.submit(sms, resp -> {
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
     * 可提供回掉URL自动推送
     *
     * @param v2Client
     */
    @Deprecated
    public static void getReport(V2Client v2Client) {
        ReportResp report = v2Client.getReport();
        System.out.println(JSON.toJSON(report));
    }

    /**
     * 主动获取未读上行
     * 无特殊要求一次最多返回500条
     * 可提供回掉URL自动推送
     *
     * @param v2Client
     */
    @Deprecated
    public static void getDeliver(V2Client v2Client) {
        DeliverResp deliver = v2Client.getDeliver();
        System.out.println(JSON.toJSON(deliver));
    }

    /**
     * 多内容打包发送接口（同步模式）最多100个内容，
     * msgId和extCode可以为空
     */
    @Deprecated
    public static void batchSms(V2Client v2Client) {
        //多内容打包数组，！！每个内容只支持1个手机号码
        List<Sms> smsContents = new ArrayList<>();
        Long phone = 18888888888L;
        for (int i = 0; i < 10; i++) {
            smsContents.add(new Sms(phone.toString(), "【线上线下】内容不确定-v2 batchSms-" + i));
            phone++;
        }
        BatchSubmitResp batchSubmitResp = v2Client.submit(smsContents);
        System.out.println(JSON.toJSON(batchSubmitResp));
    }
}
