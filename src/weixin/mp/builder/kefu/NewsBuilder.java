package weixin.mp.builder.kefu;

import weixin.common.api.WxConsts;
import weixin.mp.bean.kefu.WxMpKefuMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 * <pre>
 * 用法:
 * WxMpKefuMessage m = WxMpKefuMessage.NEWS().addArticle(article).toUser(...).build();
 * </pre>
 * @author chanjarster
 *
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder> {

  private List<WxMpKefuMessage.WxArticle> articles = new ArrayList<>();

  public NewsBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_NEWS;
  }

  public NewsBuilder addArticle(WxMpKefuMessage.WxArticle article) {
    this.articles.add(article);
    return this;
  }

  @Override
  public WxMpKefuMessage build() {
    WxMpKefuMessage m = super.build();
    m.setArticles(this.articles);
    return m;
  }
}
