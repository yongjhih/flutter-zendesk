package com.codeheadlabs.zendesk;

import android.content.Intent;

import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.ZopimChatActivity;
import zendesk.support.guide.HelpCenterActivity;
import zendesk.support.Support;
import zendesk.core.Zendesk;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

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
  public void onMethodCall(MethodCall call, Result result) {
    switch (call.method) {
      case "init":
        handleInit(call, result);
        break;
      case "setVisitorInfo":
        handleSetVisitorInfo(call, result);
        break;
      case "startChat":
        handleStartChat(call, result);
        break;
      case "initSupport":
        showHelpCenter(call, result);
        break;
      case "showHelpCenter":
        showHelpCenter(call, result);
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  private void handleInit(MethodCall call, Result result) {
    ZopimChat.init((String) call.argument("accountKey"));
    result.success(true);
  }

  private void initSupport(MethodCall call, Result result) {
    final String url = (String) call.argument("url");
    final String appId = (String) call.argument("appId");
    final String clientId = (String) call.argument("clientId");
    Zendesk.INSTANCE.init(mRegistrar.activeContext(), url, appId, clientId);
    Support.INSTANCE.init(Zendesk.INSTANCE);
    result.success(true);
  }

  private void handleSetVisitorInfo(MethodCall call, Result result) {
    VisitorInfo.Builder builder = new VisitorInfo.Builder();
    if (call.hasArgument("name")) {
      builder.name((String) call.argument("name"));
    }
    if (call.hasArgument("email")) {
      builder.name((String) call.argument("email"));
    }
    if (call.hasArgument("phoneNumber")) {
      builder.phoneNumber((String) call.argument("phoneNumber"));
    }
    if (call.hasArgument("note")) {
      builder.note((String) call.argument("note"));
    }
    ZopimChat.setVisitorInfo(builder.build());
    result.success(true);
  }

  private void handleStartChat(MethodCall call, Result result) {
    Intent intent = new Intent(mRegistrar.activeContext(), ZopimChatActivity.class);
    mRegistrar.activeContext().startActivity(intent);
    result.success(true);
  }

  private void showHelpCenter(MethodCall call, Result result) {
    HelpCenterActivity.builder().show(mRegistrar.activeContext());
    //Intent intent = new Intent(mRegistrar.activeContext(), HelpCenterActivity.class);
    //mRegistrar.activeContext().startActivity(intent);
    result.success(true);
  }
}
