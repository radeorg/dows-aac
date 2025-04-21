package org.dows.aac.handler.uim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacUser;
import org.dows.aac.request.LoginRequest;
import org.dows.aac.request.ThirdPartyPreRegisterRequest;
import org.dows.aac.weixin.GetTelephoneResponse;
import org.dows.aac.weixin.OpenidResponse;
import org.dows.rade.constant.IdentifierType;
import org.dows.uim.api.AccountApi;
import org.dows.uim.api.AccountTypeRequest;
import org.dows.uim.api.AccountTypeResponse;
import org.dows.uim.api.OrgApi;
import org.dows.uim.request.AccountInstanceRequest;
import org.dows.uim.request.BindingAccountRequest;
import org.dows.uim.request.FindAccountIdentifierRequest;
import org.dows.uim.request.RelevancyAccountInstanceIdForOpenidByTelephoneRequest;
import org.dows.uim.response.AccountIdentifierResponse;
import org.dows.uim.response.AccountInstanceResponse;
import org.dows.uim.response.RootOrgResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class UimApiHandler {

    private final AccountApi accountApi;
    private final OrgApi orgApi;

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

    public AccountIdentifierResponse getAccountIdentifierWithOpenid(OpenidResponse thirdPartyOpenidForBindingAccount,
                                                                    ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest) {
        if (Objects.nonNull(thirdPartyOpenidForBindingAccount)) {
            String openid = thirdPartyOpenidForBindingAccount.getOpenid();
            return accountApi.saveAccountIdentifier(openid, thirdPartyPreRegisterRequest.getIdentifierType());
        }
        return null;
    }

    /**
     * 关联openid和accountInstanceId，根据手机号码查询accountInstanceId，并绑定到当前用户openid账号标识
     *
     * @param accountIdentifierResponse
     * @param getTelephoneResponse
     * @param thirdPartyPreRegisterRequest
     */
    public void relevancyAccountInstanceIdForOpenid(AccountIdentifierResponse accountIdentifierResponse, GetTelephoneResponse getTelephoneResponse,
                                                    ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest) {

        if (Objects.nonNull(getTelephoneResponse)) {
            FindAccountIdentifierRequest findAccountIdentifierRequest = new FindAccountIdentifierRequest();
            findAccountIdentifierRequest.setIdentifierType(IdentifierType.PHONE);
            findAccountIdentifierRequest.setIdentifier(getTelephoneResponse.getPhone_info().getPurePhoneNumber());
            AccountIdentifierResponse accountIdentifier = accountApi.getAccountIdentifier(findAccountIdentifierRequest);
            Long accountInstanceId = null;
            // 如果账号不存在，创建账号实例及账号标识
            if (accountIdentifier == null) {
                AccountInstanceRequest accountInstanceRequest = new AccountInstanceRequest();
                accountInstanceRequest.setIdentifierType(IdentifierType.PHONE.getType());
                accountInstanceRequest.setIdentifier(getTelephoneResponse.getPhone_info().getPurePhoneNumber());
                accountInstanceId = accountApi.getAccountWithRegister(accountInstanceRequest);
            } else {
                accountInstanceId = accountIdentifier.getAccountInstanceId();
            }
            RelevancyAccountInstanceIdForOpenidByTelephoneRequest relevancyAccountInstanceIdByTelephoneRequest =
                    new RelevancyAccountInstanceIdForOpenidByTelephoneRequest();
            relevancyAccountInstanceIdByTelephoneRequest.setAccountIdentifierId(accountIdentifierResponse.getAccountIdentifierId());
            relevancyAccountInstanceIdByTelephoneRequest.setAccountInstanceId(accountInstanceId);
            relevancyAccountInstanceIdByTelephoneRequest.setIdentifier(accountIdentifierResponse.getIdentifier());
            relevancyAccountInstanceIdByTelephoneRequest.setIdentifierType(thirdPartyPreRegisterRequest.getIdentifierType());
            accountApi.relevancyAccountInstanceIdForOpenidByTelephone(relevancyAccountInstanceIdByTelephoneRequest);

        }

    }

    public AccountIdentifierResponse getAccountIdentifier(String appId, String identifier, IdentifierType identifierType) {
        FindAccountIdentifierRequest findAccountIdentifierRequest = new FindAccountIdentifierRequest();
        findAccountIdentifierRequest.setIdentifierType(identifierType);
        findAccountIdentifierRequest.setIdentifier(identifier);
        findAccountIdentifierRequest.setAppId(appId);
        //accountApi.getAccountByTelephone()
        return accountApi.getAccountIdentifier(findAccountIdentifierRequest);
    }

    public AccountInstanceResponse getAccountInstanceById(Long accountInstanceId) {
        return accountApi.getAccountInstanceById(accountInstanceId);
    }
}
