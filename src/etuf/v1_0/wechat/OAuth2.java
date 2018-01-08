package etuf.v1_0.wechat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import etuf.v1_0.common.Common;
import etuf.v1_0.model.base.output.OutputSimpleResult;

import weixin.common.api.WxConsts;
import weixin.common.exception.WxErrorException;
import weixin.mp.bean.result.WxMpOAuth2AccessToken;
import weixin.mp.bean.result.WxMpUser;

/**
 * OAuth2 授权跳转页面
 * 
 * @author lxd
 * 
 */
@SuppressWarnings("serial")
public abstract class OAuth2 extends WechatServletAbs {

	private String code;
	private String state;
	private WxMpOAuth2AccessToken accessToken = null;
	private WxMpUser wechatUser = null;
	private HttpSession session;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		session=request.getSession(true);
		code = request.getParameter(WxConsts.OAUTH2_SCOPE_CODE);
		state = request.getParameter(WxConsts.OAUTH2_SCOPE_STATE);
		OutputSimpleResult osr=new OutputSimpleResult();
		try {
			accessToken = GetWechatOAuth2AccessToken();
			if (WxConsts.OAUTH2_SCOPE_USER_INFO.equals(accessToken.getScope())) {
				wechatUser = GetWechatUserInfo();
			}
			try {
				// 处理用户逻辑
				osr=DoDispatch(request,response,accessToken, wechatUser, state);
			} catch (Exception e) {
				osr.result="用户业务处理以及调度逻辑执行过程中发生异常。";
				e.printStackTrace();
			}
			if(!osr.IsSucceed()){//处理失败，打印错误信息
				if(Common.IsNullOrEmpty(osr.result)){
					osr.result="未知错误。用户业务处理以及调度失败，但未返回任何错误信息。";
				}			
				response.getWriter().write(osr.result);
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 该页面不需要处理微信公众号消息
	 */
	@Override
	protected void WxMpMessageHandle() { }

	/**
	 * 获取当前session
	 * @return
	 */
	protected HttpSession GetSession(){
		return session;
	}
	
	/**
	 * 获取用户授权的AccessToken，即用户基本信息
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	protected WxMpOAuth2AccessToken GetWechatOAuth2AccessToken()
			throws WxErrorException {
		if (accessToken == null) {
			accessToken = wxMpService.oauth2getAccessToken(code);
		} else {
			if (!wxMpService.oauth2validateAccessToken(accessToken)) {
				accessToken = wxMpService.oauth2refreshAccessToken(accessToken
						.getRefreshToken());
			}
		}
		return accessToken;
	}

	/**
	 * 获取用户授权基本信息【静默授权】
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	protected WxMpOAuth2AccessToken GetWechatUserBase() throws WxErrorException {
		return GetWechatOAuth2AccessToken();
	}

	/**
	 * 获取用户授权详细信息【用户手动授权】
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	protected WxMpUser GetWechatUserInfo() throws WxErrorException {
		if (wechatUser == null) {
			WxMpOAuth2AccessToken at = GetWechatOAuth2AccessToken();
			wechatUser = wxMpService.oauth2getUserInfo(at, null);
		}
		return wechatUser;
	}

	/**
	 * 基于用户信息处理业务调度逻辑
	 * 
	 * @param request
	 * 			  HttpServletRequest
	 * @param response
	 * 			  HttpServletResponse
	 * @param wechatUserBase
	 *            静默授权用户基本信息，即微信用户的openid
	 * @param wechatUser
	 *            用户手动授权用户信息，即微信用户的基本信息。
	 *            如果用户没有请求用户基本信息，则该参数为null
	 * @param state
	 *            用户授权时传入的state，原样返回
	 */
	protected abstract OutputSimpleResult DoDispatch(HttpServletRequest request,HttpServletResponse response,WxMpOAuth2AccessToken wechatUserBase,
			WxMpUser wechatUser, String state);
}
