package com.lamp.lantern.plugins.auth.thrid;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliPayThridAuthService extends AbstractAuthService {

	private DefaultAlipayClient alipayClient;

	@Override
	public void doInitialization() {
		alipayClient = new DefaultAlipayClient(config.getServiceAddress(), config.getAppId(), config.getPrivateKey(),
				config.getFormat(), config.getCharset(), config.getPublicKey(), config.getSignType());
	}

	@Override
	public AuthResultObject auth(UserInfo userInfo) {
		AuthResultObject authResultObject = AuthResultObject.create();
		try {
			AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
			request.setCode(userInfo.getToken());
			request.setGrantType("authorization_code");
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
			if (oauthTokenResponse.isSuccess()) {

			} else {
				authResultObject.setErrorMessage("");
			}
		} catch (Exception e) {
			authResultObject.setErrorMessage("");
			log.error(e.getMessage(), e);
		}
		return authResultObject;
	}

	@Override
	public AuthResultObject getUserInfo(UserInfo userInfo) {
		AuthResultObject authResultObject = AuthResultObject.create();
		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
		try {
			AlipayUserInfoShareResponse response = alipayClient.execute(request, userInfo.getToken());
			if (response.isSuccess()) {
				UserInfo userInfoEntity = new UserInfo();
			} else {
				authResultObject.setErrorMessage("根据 access_token获取用户信息失败!");
			}
		} catch (Exception e) {
			authResultObject.setErrorMessage("根据 access_token获取用户信息抛出异常！");
		}
		return authResultObject;
	}

}
