package com.codeheadlabs.zendesk;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import zendesk.core.AnonymousIdentity;
import zendesk.support.guide.ArticleUiConfig;
import zendesk.support.guide.HelpCenterActivity;
import zendesk.support.Support;
import zendesk.core.Zendesk;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import zendesk.support.guide.HelpCenterUiConfig;
import zendesk.support.guide.ViewArticleActivity;
import zendesk.support.request.RequestActivity;
import zendesk.support.request.RequestUiConfig;

/** ZendeskPlugin */
public class ZendeskPlugin implements MethodCallHandler {
  private final Registrar mRegistrar;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.codeheadlabs.zendesk");
    channel.setMethodCallHandler(new ZendeskPlugin(registrar));
  }

  private ZendeskPlugin(Registrar registrar) {
    this.mRegistrar = registrar;
  }

  @Override
  public void onMethodCall(MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "init":
        init(call, result);
        break;
      case "initSupport":
        initSupport(call, result);
        break;
      case "setVisitorInfo":
        setVisitorInfo(call, result);
        break;
      case "startChat":
        startChat(call, result);
        break;
      case "showHelpCenter":
        showHelpCenter(call, result);
        break;
      case "viewArticle":
        viewArticle(call, result);
        break;
      case "request":
        request(call, result);
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  private void init(MethodCall call, Result result) {
    ZopimChat.init(call.<String>argument("accountKey"));
    result.success(true);
  }

  private void initSupport(MethodCall call, Result result) {
    final String url = call.argument("url");
    final String appId = call.argument("appId");
    final String clientId = call.argument("clientId");
    if (url != null && appId != null)  {
      Zendesk.INSTANCE.init(mRegistrar.activeContext(), url, appId, clientId);
      Zendesk.INSTANCE.setIdentity(new AnonymousIdentity());
      Support.INSTANCE.init(Zendesk.INSTANCE);
      result.success(true);
    } else {
      result.error("NPE", "url or appId == null", null);
    }
  }

  private void setVisitorInfo(MethodCall call, Result result) {
    VisitorInfo.Builder builder = new VisitorInfo.Builder();
    if (call.hasArgument("name")) {
      builder.name(call.<String>argument("name"));
    }
    if (call.hasArgument("email")) {
      builder.name(call.<String>argument("email"));
    }
    if (call.hasArgument("phoneNumber")) {
      builder.phoneNumber(call.<String>argument("phoneNumber"));
    }
    if (call.hasArgument("note")) {
      builder.note(call.<String>argument("note"));
    }
    ZopimChat.setVisitorInfo(builder.build());
    result.success(true);
  }

  private void startChat(MethodCall call, Result result) {
    Intent intent = new Intent(mRegistrar.activeContext(), ZopimChatActivity.class);
    mRegistrar.activeContext().startActivity(intent);
    result.success(true);
  }

  public static List<Long> toLongs(List<Integer> items) {
      final List<Long> res = new ArrayList<>();
      for (Integer it : items) {
        res.add(it.longValue());
      }
      return res;
  }

  private void showHelpCenter(MethodCall call, Result result) {
    final HelpCenterUiConfig.Builder builder = HelpCenterActivity.builder();
    if (call.hasArgument("categories")) {
      final List<Integer> items = Objects.requireNonNull(call.<List<Integer>>argument("categories"));
      builder.withArticlesForCategoryIds(toLongs(items));
    }
    if (call.hasArgument("sections")) {
      final List<Integer> items = Objects.requireNonNull(call.<List<Integer>>argument("sections"));
        builder.withArticlesForSectionIds(toLongs(items));
    }
    if (call.hasArgument("labels")) {
      builder.withLabelNames(Objects.requireNonNull(call.<List<String>>argument("labels")));
    }
    if (call.hasArgument("contactUsButtonVisible")) {
      builder.withContactUsButtonVisible(Objects.requireNonNull(call.<Boolean>argument("contactUsButtonVisible")));
    }

    builder.show(mRegistrar.activity());
    result.success(true);
  }

  private void request(MethodCall call, Result result) {
    final RequestUiConfig.Builder builder = RequestActivity.builder();
    if (call.hasArgument("tags")) {
      builder.withTags(Objects.requireNonNull(call.<List<String>>argument("tags")));
    }
    if (call.hasArgument("subject")) {
      builder.withRequestSubject(Objects.requireNonNull(call.<String>argument("subject")));
    }

    builder.show(mRegistrar.activity());
    result.success(true);
  }
  private void viewArticle(MethodCall call, Result result) {
    if (call.hasArgument("id")) {
      final ArticleUiConfig.Builder builder = ViewArticleActivity.builder(Objects.requireNonNull(call.<Integer>argument("id")).longValue());
      if (call.hasArgument("contactUsButtonVisible")) {
        builder.withContactUsButtonVisible(Objects.requireNonNull(call.<Boolean>argument("contactUsButtonVisible")));
      }
      builder.show(mRegistrar.activity());
      result.success(true);
    } else {
      result.error("NPE", "id == null", null);
    }
  }
}
