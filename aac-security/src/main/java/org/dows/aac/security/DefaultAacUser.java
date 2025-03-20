package org.dows.aac.security;

import lombok.Getter;
import lombok.Setter;
import org.dows.aac.api.AacUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class DefaultAacUser extends User implements AacUser, UserDetails {

    @Getter
    @Setter
    private Long accountId;
    @Getter
    @Setter
    private List<Long> roleIds;
    @Getter
    @Setter
    private boolean superAccount;

    public DefaultAacUser(Long accountId, String username, String password,
                          Collection<? extends GrantedAuthority> authorities,
                          List<Long> roleIds, boolean superAccount) {
        super(username, password, authorities);
        this.accountId = accountId;
        this.roleIds = roleIds;
        this.superAccount = superAccount;
    }


}
