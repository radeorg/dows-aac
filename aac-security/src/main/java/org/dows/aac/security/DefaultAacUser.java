package org.dows.aac.security;

import lombok.Getter;
import lombok.Setter;
import org.dows.aac.api.AacUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class DefaultAacUser extends User implements AacUser, UserDetails {


    private Long accountId;

    private List<Long> roleIds;

    private List<Long> orgRootIds;

    private List<Integer> accountTypes;

    private boolean superAccount;

    private Long orgRootId;

    private Long userId;

    private String avatar;

    private String telephone;

    private String email;

    private String nickname;

    private Integer state;

    private Integer identifierType;


    public DefaultAacUser(Long accountId, String username, String password,
                          Collection<? extends GrantedAuthority> authorities,
                          List<Long> roleIds, boolean superAccount) {
        super(username, password, authorities);
        this.accountId = accountId;
        this.roleIds = roleIds;
        this.superAccount = superAccount;
        this.nickname = username;
    }

}
