package etuf.v1_0.wechat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import weixin.common.exception.WxErrorException;
import weixin.common.session.WxSessionManager;
import weixin.mp.api.WxMpInMemoryConfigStorage;
import weixin.mp.api.WxMpMessageHandler;
import weixin.mp.api.WxMpMessageRouter;
import weixin.mp.api.WxMpService;
import weixin.mp.api.impl.WxMpServiceImpl;
import weixin.mp.bean.message.WxMpXmlMessage;
import weixin.mp.bean.message.WxMpXmlOutMessage;
import weixin.mp.bean.message.WxMpXmlOutTextMessage;

@SuppressWarnings("serial")
public abstract class WechatServletAbs extends HttpServlet {
	protected WxMpInMemoryConfigStorage config;
	protected WxMpService wxMpService;
	protected WxMpMessageRouter wxMpMessageRouter;

	@Override
	public void init() throws ServletException {
		super.init();
		config = GetWechatConfig();
		wxMpService = new WxMpServiceImpl();
	    wxMpService.setWxMpConfigStorage(config);
	    
	    WxMpMessageHandler handler = new WxMpMessageHandler() {

			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
					Map<String, Object> context, WxMpService wxMpService,
					WxSessionManager sessionManager) throws WxErrorException {
				WxMpXmlOutTextMessage m
	              = WxMpXmlOutMessage.TEXT().content("easy-to-use-framework,auth by lxd.").fromUser(wxMessage.getToUser())
	              .toUser(wxMessage.getFromUser()).build();
	          return m;
			}
	      };

	      wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
	      wxMpMessageRouter
	          .rule()
	          .async(false)
	          .content("etuf") // 拦截内容为“etuf”的消息，并返回easy-to-use-framework,auth by lxd.
	          .handler(handler)
	          .end();
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
	    response.setStatus(HttpServletResponse.SC_OK);

	    String signature = request.getParameter("signature");
	    String nonce = request.getParameter("nonce");
	    String timestamp = request.getParameter("timestamp");

	    if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
	      // 消息签名不正确，说明不是公众平台发过来的消息
	      response.getWriter().println("非法请求：无法识别的签名信息！");
	      return;
	    }

	    String echostr = request.getParameter("echostr");
	    if (StringUtils.isNotBlank(echostr)) {
	      // 说明是一个仅仅用来验证的请求，回显echostr
	      response.getWriter().println(echostr);
	      return;
	    }

	    String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
	        "raw" :
	        request.getParameter("encrypt_type");

	    if ("raw".equals(encryptType)) {
	      // 明文传输的消息
	      WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
	      WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
	      response.getWriter().write(outMessage.toXml());
	      return;
	    }

	    if ("aes".equals(encryptType)) {
	      // 是aes加密的消息
	      String msgSignature = request.getParameter("msg_signature");
	      WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), config, timestamp, nonce, msgSignature);
	      WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
	      response.getWriter().write(outMessage.toEncryptedXml(config));
	      return;
	    }

	    response.getWriter().println("不可识别的加密类型");
	    return;
	}
	
	/**
	 * 获取微信公众号配置【待重写的抽象方法】
	 * @return
	 */
	protected abstract WxMpInMemoryConfigStorage GetWechatConfig();
	
	/**
	 * 处理微信消息【待重写的抽象方法】
	 */
	protected abstract void WxMpMessageHandle();
	
}
