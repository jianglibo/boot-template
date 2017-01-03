package hello.util;

import com.google.common.collect.Sets;

import hello.domain.UcToken;
import hello.vo.BootUserVo;

public class SignupConfirmTemplate extends SendCloudTemplate {
    
    public static final String VURL = "vurl";
    
    private BootUserVo personVo;

    public SignupConfirmTemplate(BootUserVo personVo, String host, UcToken uctk) {
        super("regist_confirm", Sets.newHashSet(VURL));
        setSubjectTpl("请激活您的账号，%s �?");
        this.personVo = personVo;
        withVar(VURL, host + "/email-verify?uctk=" + uctk.getTk());
    }

    @Override
    protected String getSubject() {
        return String.format(getSubjectTpl(), personVo.getUsername());
    }
}
