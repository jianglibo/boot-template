package hello.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginAttemptForm {

	@NotNull
	@Size(min=6,max=64, message="validate.login.username")
    private String username;

	@NotNull
	@Size(min=6,max=64, message="validate.login.password")
    private String password;

    private String captcha;

    public LoginAttemptForm() {
    }

    public LoginAttemptForm(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
