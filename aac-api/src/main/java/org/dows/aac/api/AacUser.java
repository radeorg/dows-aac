package org.dows.aac.api;

import java.util.List;


public interface AacUser /*extends User implements UserDetails*/ {

    Long getAccountId();

    List<Long> getRoleIds();

    boolean isSuperAccount();

}
