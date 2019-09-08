package com.hp.roam.api;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.roam.http.BaseResponse;
import com.hp.roam.http.EmsRequest;
import com.hp.roam.http.PublicContentType;
import com.hp.roam.util.HttpUtils;

/**
 * @author ck
 * @date 2019年5月22日 上午11:39:18
 */
@Component
public class ApiService {

	@Value("${api.ems.baseApiUrl}")
	private String EMS_BASE_URL;

	@Value("${api.ems.AppId}")
	private String AppId;

	@Value("${api.ems.AppSecurity}")
	private String AppSecurity;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PublicContentType publicContentType;

	@Bean
	public PublicContentType publicContentType() {
		PublicContentType publicContentType = new PublicContentType();
		publicContentType.setAppId(AppId);
		publicContentType.setAppSecurity(AppSecurity);
		return publicContentType;
	}

	/**
	 * 发送邮件
	 * 
	 * @param emsRequest
	 */
	@SuppressWarnings("unchecked")
	public BaseResponse<String> emsSend(EmsRequest emsRequest) {
		try {
			String params = HttpUtils.mapper.writeValueAsString(emsRequest);
			Map<String, String> result = HttpUtils.postJson(params,
					EMS_BASE_URL + "/app/v1/messages/email", publicContentType);
			BaseResponse<String> baseResponse = objectMapper
					.readValue(result.get("result"), BaseResponse.class);
			return baseResponse;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public BaseResponse<Map<String, String>> checkEmsStatusByMsgId(String messageId) {
		String result = HttpUtils.getPathVariable(messageId,
				EMS_BASE_URL + "/app/v1/messages/email", publicContentType);
		
		
		HttpUtils.logger.info("checkEmsStatusByMsgId  ------"+result);
		try {
			BaseResponse<Map<String, String>> baseResponse = objectMapper.readValue(result, BaseResponse.class);
			return baseResponse;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public BaseResponse<String> testEmsSend(EmsRequest emsRequest) {
		try {
			String params = HttpUtils.mapper.writeValueAsString(emsRequest);
			Map<String, String> result = HttpUtils.postJson(params,
					EMS_BASE_URL + "/app/v1/messages/email", publicContentType);
			BaseResponse<String> baseResponse = objectMapper
					.readValue(result.get("result"), BaseResponse.class);
			return baseResponse;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
