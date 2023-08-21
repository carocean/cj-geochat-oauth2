package cj.geochat.oauth2.server.verifycode;

import cj.geochat.ability.oauth.server.entrypoint.verifycode.IVerifyCodeProvider;
import cj.geochat.ability.oauth.server.entrypoint.verifycode.VerifyCodeRequest;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.util.Random;
@Slf4j
@Service
public class SmsVerifyCodeProvider implements IVerifyCodeProvider {
    @Override
    public String generate(VerifyCodeRequest verifyCodeRequest) {
        if (!"sms_code".equals(verifyCodeRequest.getVerifyType())) {
            return null;
        }

        String code = sendSmsCode(verifyCodeRequest);
        log.info(String.format("验证码：%s 已发送到手机：%s", code, verifyCodeRequest.getPrincipal()));
        return code;
    }
    //未来接入到第三方发送邮箱验证码平台，此处模拟一下
    private String sendSmsCode(VerifyCodeRequest verifyCodeRequest) {
        Random random = new Random();
        String code = String.format("%s%s%s%s%s%s",
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10));
        return code;
    }
}
