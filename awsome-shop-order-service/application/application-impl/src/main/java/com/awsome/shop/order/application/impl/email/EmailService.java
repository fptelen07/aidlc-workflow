package com.awsome.shop.order.application.impl.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AWS SES 邮件发送服务
 */
@Slf4j
@Service
public class EmailService {

    @Value("${email.from:no-reply@awsome-shop.com}")
    private String fromAddress;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    private SesClient sesClient;

    @PostConstruct
    public void init() {
        this.sesClient = SesClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    @PreDestroy
    public void destroy() {
        if (sesClient != null) {
            sesClient.close();
        }
    }

    /**
     * 发送兑换码邮件
     */
    public void sendRedemptionEmail(String toEmail, String productName,
                                     String redemptionCode, LocalDateTime orderTime) {
        String subject = "AWSome Shop - 您的兑换码已生成";
        String htmlBody = buildHtmlBody(productName, redemptionCode, orderTime);

        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromAddress)
                    .destination(Destination.builder().toAddresses(toEmail).build())
                    .message(Message.builder()
                            .subject(Content.builder().charset("UTF-8").data(subject).build())
                            .body(Body.builder()
                                    .html(Content.builder().charset("UTF-8").data(htmlBody).build())
                                    .build())
                            .build())
                    .build();

            sesClient.sendEmail(request);
            log.info("兑换码邮件发送成功 to={} code={}", toEmail, redemptionCode);
        } catch (Exception e) {
            log.error("兑换码邮件发送失败 to={}", toEmail, e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    private String buildHtmlBody(String productName, String redemptionCode, LocalDateTime orderTime) {
        String formattedTime = orderTime != null
                ? orderTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "N/A";

        return "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head>"
                + "<body style='margin:0;padding:0;background-color:#f4f7fa;font-family:Arial,sans-serif;'>"
                + "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f4f7fa;padding:40px 0;'>"
                + "<tr><td align='center'>"
                + "<table width='600' cellpadding='0' cellspacing='0' style='background-color:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.1);'>"
                // Header
                + "<tr><td style='background:linear-gradient(135deg,#232f3e,#37475a);padding:30px 40px;text-align:center;'>"
                + "<h1 style='color:#ff9900;margin:0;font-size:28px;'>AWSome Shop</h1>"
                + "<p style='color:#d5dbdb;margin:8px 0 0;font-size:14px;'>您的兑换码已生成</p>"
                + "</td></tr>"
                // Body
                + "<tr><td style='padding:40px;'>"
                + "<p style='color:#333;font-size:16px;margin:0 0 20px;'>尊敬的用户，您好！</p>"
                + "<p style='color:#555;font-size:14px;line-height:1.6;margin:0 0 24px;'>您兑换的商品订单已确认，以下是您的兑换码信息：</p>"
                // Product info
                + "<table width='100%' style='background-color:#f8f9fa;border-radius:6px;padding:20px;margin-bottom:24px;' cellpadding='8'>"
                + "<tr><td style='color:#666;font-size:13px;width:80px;'>商品名称</td>"
                + "<td style='color:#232f3e;font-size:14px;font-weight:bold;'>" + escapeHtml(productName) + "</td></tr>"
                + "<tr><td style='color:#666;font-size:13px;'>订单时间</td>"
                + "<td style='color:#232f3e;font-size:14px;'>" + formattedTime + "</td></tr>"
                + "</table>"
                // Redemption code box
                + "<div style='text-align:center;background:linear-gradient(135deg,#232f3e,#37475a);border-radius:8px;padding:24px;margin-bottom:24px;'>"
                + "<p style='color:#d5dbdb;font-size:12px;margin:0 0 8px;text-transform:uppercase;letter-spacing:2px;'>兑换码</p>"
                + "<p style='color:#ff9900;font-size:32px;font-weight:bold;margin:0;letter-spacing:4px;font-family:Courier New,monospace;'>"
                + escapeHtml(redemptionCode) + "</p>"
                + "</div>"
                + "<p style='color:#888;font-size:12px;line-height:1.6;margin:0;'>请妥善保管您的兑换码，如有任何问题请联系客服。</p>"
                + "</td></tr>"
                // Footer
                + "<tr><td style='background-color:#f8f9fa;padding:20px 40px;text-align:center;border-top:1px solid #eee;'>"
                + "<p style='color:#999;font-size:12px;margin:0;'>感谢您选择 AWSome Shop！</p>"
                + "</td></tr>"
                + "</table></td></tr></table></body></html>";
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
