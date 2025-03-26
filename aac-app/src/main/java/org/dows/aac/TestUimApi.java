package org.dows.aac;

import lombok.extern.slf4j.Slf4j;
import org.dows.uim.api.AccountApi;
import org.dows.uim.api.request.AccountInstanceRequest;
import org.dows.uim.api.request.FindAccountIdentifierRequest;
import org.dows.uim.api.response.AccountIdentifierResponse;
import org.dows.uim.api.response.AccountInstanceResponse;
import org.dows.uim.api.response.AccountOrgIdsResponse;
import org.dows.uim.api.response.AccountRoleRelationResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TestUimApi implements AccountApi {

    @Override
    public Long setAccountInstance(AccountInstanceRequest accountInstance) {
        return 0L;
    }

    @Override
    public AccountInstanceResponse getAccountInstanceByAccountName(String accountName, String appId) {
        return null;
    }

    @Override
    public AccountIdentifierResponse getAccountIdentifier(FindAccountIdentifierRequest findAccountIdentifierRequest) {
        return null;
    }

    @Override
    public AccountInstanceResponse getAccountInstanceById(Long accountIdentifier) {
        return null;
    }

    @Override
    public AccountOrgIdsResponse getOrgIdsByAccountId(Long accountInstanceId, boolean check, String appId) {
        return null;
    }

    @Override
    public List<AccountRoleRelationResponse> getRoleByAccountInstanceId(List<Long> principals, String appId) {
        return List.of();
    }
}
