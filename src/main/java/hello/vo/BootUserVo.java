/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.vo;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.google.common.collect.Sets;

import hello.domain.BootUser;
import hello.domain.BootUser.Gender;
import hello.domain.Role;

/**
 * @author jianglibo@gmail.com
 *         2015年9月29日
 *
 */
public class BootUserVo extends User{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final long id;

    private final String email;
    private final String mobile;
    private final String avatar;
    private final String displayName;

    private final Gender gender;

    private final String openId;

    private final boolean emailVerified;
    private final boolean mobileVerified;

    private final Set<ThirdPartLoginVo> thirdConns;

    public BootUserVo() {
        super("-1", "", Sets.newHashSet());
        this.id = 0;
        this.email = null;
        this.mobile = null;
        this.avatar = null;
        this.displayName = null;
        this.emailVerified = false;
        this.mobileVerified = false;
        this.openId = "";
        this.gender = Gender.FEMALE;
        this.thirdConns = Sets.newHashSet();
    }

    public BootUserVo(BootUser person) {
        this(person, Sets.newHashSet());
    }

    public BootUserVo(BootUser person, Set<Role> roles) {
        this(person.getName(), person.getDisplayName(), person.getEmail(), person.getMobile(), person.getPassword(), person.isEnabled(), person
                .isAccountNonExpired(), person.isCredentialsNonExpired(), person.isAccountNonLocked(), person.getAvatar(), roles, person.isEmailVerified(),
                person.isMobileVerified(), person.getGender(), ThirdPartLoginVo.toVos(person.getThirdConns()), person.getId(), person.getOpenId());
    }

    public BootUserVo(String name, String displayName, String email, String mobile, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialNonExpired, boolean accountNonLocked, String avatar, Collection<? extends GrantedAuthority> authorities, boolean emailVerified,
            boolean mobileVerified, Gender gender, Set<ThirdPartLoginVo> thirdConns, long id, String openId) {
        super(name, password, enabled, accountNonExpired, credentialNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.mobile = mobile;
        this.avatar = avatar;
        this.emailVerified = emailVerified;
        this.mobileVerified = mobileVerified;
        if (openId == null || openId.isEmpty()) {
            this.openId = UUID.randomUUID().toString().replaceAll("-", "");
        } else {
            this.openId = openId;
        }
        this.gender = gender;
        this.thirdConns = thirdConns;
    }

    public BootUserVo(String name, String displayName, String email, String mobile, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialNonExpired, boolean accountNonLocked, String avatar, Collection<? extends GrantedAuthority> authorities, boolean emailVerified,
            boolean mobileVerified) {
        this(name, displayName, email, mobile, password, enabled, accountNonExpired, credentialNonExpired, accountNonLocked, avatar, authorities,
                emailVerified, mobileVerified, Gender.FEMALE, Sets.newHashSet(), 0, null);
    }

    public BootUserVo(String name, String displayName, String email, String mobile, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialNonExpired, boolean accountNonLocked, String avatar, Collection<GrantedAuthority> authorities) {
        this(name, displayName, email, mobile, password, enabled, accountNonExpired, credentialNonExpired, accountNonLocked, avatar, authorities, false, false,
                Gender.FEMALE, Sets.newHashSet(), 0, null);
    }

    public BootUserVo(String name, String displayName, String email, String mobile, String password, String avatar, Collection<GrantedAuthority> authorities) {
        this(name, displayName, email, mobile, password, true, true, true, true, avatar, authorities);
    }

    public BootUserVo(String name, String email, String mobile, String password) {
        this(name, name, email, mobile, password, true, true, true, true, null, Sets.newHashSet());
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getId() {
        return id;
    }

    public String getOpenId() {
        return openId;
    }

    public Gender getGender() {
        return gender;
    }

    public Set<ThirdPartLoginVo> getThirdConns() {
        return thirdConns;
    }

}
