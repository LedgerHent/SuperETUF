package etuf.v1_0.wechat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weixin.common.api.WxConsts;

/**
 * 授权主页（如果需要授权，那么未授权用户会拦截到此页面）
 * 
 * @author lxd
 * 
 */
@SuppressWarnings("serial")
public abstract class AuthIndex extends WechatServletAbs {
	// 获取用户授权方式，默认只需要openid，即静默授权
	private boolean isBaseAuth = true;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String url= wxMpService.oauth2buildAuthorizationUrl(GetOAuth2RedirectUrl(request),
				isBaseAuth ? WxConsts.OAUTH2_SCOPE_BASE
						: WxConsts.OAUTH2_SCOPE_USER_INFO,
				GetOAuth2UrlParameterUserExtendInfo(request));
		response.sendRedirect(url);
	}

	/**
	 * 该页面不需要处理微信公众号消息
	 */
	@Override
	protected void WxMpMessageHandle() { }
	
	/**
	 * oauth2授权跳转页面
	 * 
	 * @author lxd
	 * @param request
	 * @return
	 */
	protected abstract String GetOAuth2RedirectUrl(HttpServletRequest request);

	/**
	 * 重定向后会带上用户扩展信息参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 * 
	 * @author lxd
	 * @param request
	 * @return
	 */
	protected abstract String GetOAuth2UrlParameterUserExtendInfo(
			HttpServletRequest request);

	/**
	 * 获取微信授权方式是否为静默授权
	 * 
	 * @return
	 */
	public boolean isBaseAuth() {
		return isBaseAuth;
	}

	/**
	 * 设置微信授权方式是否为静默授权
	 * 
	 * @param isBaseAuth
	 *            true:静默授权 false:手动授权
	 */
	public void setBaseAuth(boolean isBaseAuth) {
		this.isBaseAuth = isBaseAuth;
	}

}
