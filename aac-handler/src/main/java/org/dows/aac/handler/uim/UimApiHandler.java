package org.dows.aac.handler.uim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacUser;
import org.dows.aac.request.LoginRequest;
import org.dows.uim.response.RootOrgResponse;
import org.dows.aac.weixin.GetTelephoneResponse;
import org.dows.uim.api.AccountApi;
import org.dows.uim.api.AccountTypeRequest;
import org.dows.uim.api.AccountTypeResponse;
import org.dows.uim.api.OrgApi;
import org.dows.uim.request.AccountInstanceRequest;
import org.dows.uim.request.BindingAccountRequest;
import org.dows.uim.response.AccountInstanceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class UimApiHandler {

    private final AccountApi accountApi;
    private final OrgApi orgApi;


    public void claimCurrentAacUser(AacUser aacUser, GetTelephoneResponse getTelephoneResponse) {


    }

    /**
     * 绑定当前用户其他信息到UIM账号
     *
     * @param aacUser     当前用户
     * @param phoneNumber 手机号码
     */
    public void bindingCurrentAacUser(AacUser aacUser, GetTelephoneResponse phoneNumber) {

        Long accountId = aacUser.getAccountId();
        String purePhoneNumber = phoneNumber.getPhone_info().getPurePhoneNumber();
        String countryCode = phoneNumber.getPhone_info().getCountryCode();
        BindingAccountRequest bindingAccountRequest = new BindingAccountRequest();
        bindingAccountRequest.setAccountInstanceId(accountId);
        bindingAccountRequest.setTelephone(purePhoneNumber);
        bindingAccountRequest.setZoneNo(countryCode);
        accountApi.bindingAccount(bindingAccountRequest);
    }


    public AccountInstanceResponse getAccountInstanceByIdentifier(String appId, String accountName) {
        return accountApi.getAccountInstanceByIdentifier(appId, accountName);
    }

    public void newRegister(String name, String encode, LoginRequest loginRequest) {
        AccountInstanceRequest accountInstanceRequest = new AccountInstanceRequest();
        accountInstanceRequest.setPassword(encode);
        accountInstanceRequest.setIdentifier(name);
        accountInstanceRequest.setIdentifierType(loginRequest.getIdentifierType().getType());
        accountInstanceRequest.setAppId(loginRequest.getAppId());
        accountInstanceRequest.setZoneNo(loginRequest.getZoneNo());
        accountInstanceRequest.setPhone(name);
        accountInstanceRequest.setAvatar(loginRequest.getAvator());
        accountInstanceRequest.setSource(loginRequest.getSource());
        accountInstanceRequest.setReferralsNo(loginRequest.getReferralsNo());
        accountApi.getAccountWithRegister(accountInstanceRequest);
    }

    public List<Long> getAllRoleIds(String appId, Long accountInstanceId) {
        return accountApi.getAllRoleIds(appId, accountInstanceId);
    }


    public List<Integer> getAccountTypes(Long accountInstanceId) {
        AccountTypeRequest accountTypeRequest = new AccountTypeRequest();
        accountTypeRequest.setAccountInstanceId(accountInstanceId);
        List<AccountTypeResponse> accountType = accountApi.getAccountType(accountTypeRequest);
        if (Objects.isNull(accountType)) {
            return null;
        }
        return accountType.stream().map(AccountTypeResponse::getAccountType).toList();
    }

    public List<RootOrgResponse> getOrgRootId(Long accountInstanceId) {
        // TODO 根据账号ID查询账号所在组织根节信息
        return orgApi.getRootOrgListByAccountInstanceId(accountInstanceId);
    }
}
