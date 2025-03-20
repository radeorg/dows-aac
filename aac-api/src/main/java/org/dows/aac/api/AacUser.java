package org.dows.aac.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public interface AacUser /*extends User implements UserDetails*/ {

    Long getAccountId();

    List<Long> getRoleIds();

    boolean isSuperAccount();

}
