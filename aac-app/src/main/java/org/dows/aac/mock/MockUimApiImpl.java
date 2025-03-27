package org.dows.aac.mock;

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
public class MockUimApiImpl implements AccountApi {

    @Override
    public Long getAccountWithRegister(String appId, AccountInstanceRequest accountInstanceRequest) {
        return 1L;
    }

    @Override
    public AccountInstanceResponse getAccountInstanceId(String appId, String identifier) {
        /*AccountInstanceResponse accountInstanceResponse = new AccountInstanceResponse();
        accountInstanceResponse.setAccountName("lait");
        accountInstanceResponse.setAccountInstanceId(1L);
        accountInstanceResponse.setSuperAccount(true);
        accountInstanceResponse.setPassword("");

        return accountInstanceResponse;*/
        return null;
    }

    @Override
    public List<Long> getRoleIdsByAccountId(String appId, Long accountId) {
        return List.of(1L, 2L, 3L);
    }

    @Override
    public List<Long> getAllRoleIds(String appId, Long accountId) {
        return List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    }

    @Override
    public List<Long> getOrgIdsByAccountId(String appId, Long accountId, boolean check) {
        return List.of(1L, 2L);
    }


    @Override
    public Long setAccountInstance(AccountInstanceRequest accountInstance) {
        return 1L;
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
