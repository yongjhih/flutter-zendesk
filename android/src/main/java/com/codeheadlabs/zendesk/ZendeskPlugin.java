package com.codeheadlabs.zendesk;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import zendesk.core.AnonymousIdentity;
import zendesk.core.Identity;
import zendesk.core.Zendesk;
import zendesk.support.Support;
import zendesk.support.guide.ArticleUiConfig;
import zendesk.support.guide.HelpCenterActivity;
import zendesk.support.guide.HelpCenterUiConfig;
import zendesk.support.guide.ViewArticleActivity;
import zendesk.support.request.RequestActivity;
import zendesk.support.request.RequestUiConfig;

/** ZendeskPlugin */
public class ZendeskPlugin implements MethodCallHandler {
  private final Registrar mRegistrar;

  /** Plugin registration. */
  public static void registerWith(@NonNull final Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.codeheadlabs.zendesk");
    channel.setMethodCallHandler(new ZendeskPlugin(registrar));
  }

  private ZendeskPlugin(@NonNull final Registrar registrar) {
    this.mRegistrar = registrar;
  }

  @Override
  public void onMethodCall(@NonNull final MethodCall call, @NonNull Result result) {
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
      case "setVisitorInfoAndIdentity":
        setVisitorInfoAndIdentity(call, result);
        break;
      case "setHelpCenterLocaleOverride":
        setHelpCenterLocaleOverride(call, result);
        break;
      case "setIdentity":
        setIdentity(call, result);
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

  private void init(@NonNull final MethodCall call, @NonNull final Result result) {
    ZopimChat.init(call.<String>argument("accountKey"));
    result.success(true);
  }

  private void initSupport(@NonNull final MethodCall call, @NonNull final Result result) {
    final String url = call.argument("url");
    final String appId = call.argument("appId");
    final String clientId = call.argument("clientId");

    if (url != null && appId != null)  {
      Zendesk.INSTANCE.init(mRegistrar.activeContext(), url, appId, clientId);

      Support.INSTANCE.init(Zendesk.INSTANCE);
      result.success(true);
    } else {
      result.error("NPE", "url or appId == null", null);
    }
  }

  @Nullable
  public static Locale toLocale(@NonNull final String s) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return Locale.forLanguageTag(s);
    } else {
      final Locale[] availableLocales = Locale.getAvailableLocales();
      for (final Locale locale : availableLocales) {
        if (locale.toString().equals(s)) {
          return locale;
        }
      }
      return null;
    }
  }

  private void setHelpCenterLocaleOverride(@NonNull final MethodCall call, @NonNull final Result result) {
    final String localeString = call.arguments();
    final Locale locale = toLocale(localeString);
    if (locale != null) {
      Support.INSTANCE.setHelpCenterLocaleOverride(locale);
      result.success(true);
    } else {
      result.error("NotFound", localeString + " not found", null);
    }
  }


  private void setIdentity(@NonNull final MethodCall call, @NonNull final Result result) {
    final AnonymousIdentity.Builder builder = new AnonymousIdentity.Builder();

    if (call.hasArgument("name")) {
      builder.withNameIdentifier(call.<String>argument("name"));
    }
    if (call.hasArgument("email")) {
      builder.withEmailIdentifier(call.<String>argument("email"));
    }

    Zendesk.INSTANCE.setIdentity(builder.build());

    result.success(true);
  }

  private void setVisitorInfoAndIdentity(@NonNull final MethodCall call, @NonNull final Result result) {
    final VisitorInfo.Builder builder = new VisitorInfo.Builder();

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

    final VisitorInfo visitorInfo = builder.build();

    ZopimChat.setVisitorInfo(visitorInfo);
    Zendesk.INSTANCE.setIdentity(toIdentity(visitorInfo));

    result.success(true);
  }

  @NonNull
  public static Identity toIdentity(@NonNull final VisitorInfo visitorInfo) {
    final AnonymousIdentity.Builder builder = new AnonymousIdentity.Builder();
    final String name = visitorInfo.getName();

    if (name != null) {
      builder.withNameIdentifier(name);
    }
    final String email = visitorInfo.getEmail();
    if (email != null) {
      builder.withEmailIdentifier(email);
    }

    return builder.build();
  }

  private void setVisitorInfo(@NonNull final MethodCall call, @NonNull final Result result) {
    final VisitorInfo.Builder builder = new VisitorInfo.Builder();

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

  private void startChat(@NonNull final MethodCall call, @NonNull final Result result) {
    final Intent intent = new Intent(mRegistrar.activeContext(), ZopimChatActivity.class);

    mRegistrar.activeContext().startActivity(intent);

    result.success(true);
  }

  @NonNull
  public static List<Long> toLongs(@NonNull final List<? extends Number> items) {
      final List<Long> res = new ArrayList<>();
      for (Number it : items) {
        res.add(it.longValue());
      }
      return res;
  }

  /**
   * Checks that the specified object reference is not {@code null}. This
   * method is designed primarily for doing parameter validation in methods
   * and constructors, as demonstrated below:
   * <blockquote><pre>
   * public Foo(Bar bar) {
   *     this.bar = Objects.requireNonNull(bar);
   * }
   * </pre></blockquote>
   *
   * @param obj the object reference to check for nullity
   * @param <T> the type of the reference
   * @return {@code obj} if not {@code null}
   * @throws NullPointerException if {@code obj} is {@code null}
   */
  @NonNull
  public static <T> T requireNonNull(@Nullable final T obj) {
    if (obj == null)
      throw new NullPointerException();
    return obj;
  }

  private void showHelpCenter(@NonNull final MethodCall call, @NonNull final Result result) {
    final HelpCenterUiConfig.Builder builder = HelpCenterActivity.builder();

    if (call.hasArgument("categories")) {
      final List<Number> items = call.argument("categories");
      builder.withArticlesForCategoryIds(toLongs(requireNonNull(items)));
    }
    if (call.hasArgument("sections")) {
      final List<Number> items = call.argument("sections");
      builder.withArticlesForSectionIds(toLongs(requireNonNull(items)));
    }
    if (call.hasArgument("labels")) {
      builder.withLabelNames(requireNonNull(call.<List<String>>argument("labels")));
    }
    if (call.hasArgument("contactUsButtonVisible")) {
      builder.withContactUsButtonVisible(requireNonNull(call.<Boolean>argument("contactUsButtonVisible")));
    }

    builder.show(mRegistrar.activity());
    result.success(true);
  }

  private void request(@NonNull final MethodCall call, @NonNull final Result result) {
    final RequestUiConfig.Builder builder = RequestActivity.builder();

    if (call.hasArgument("tags")) {
      builder.withTags(requireNonNull(call.<List<String>>argument("tags")));
    }
    if (call.hasArgument("subject")) {
      builder.withRequestSubject(requireNonNull(call.<String>argument("subject")));
    }

    builder.show(mRegistrar.activity());
    result.success(true);
  }

  private void viewArticle(@NonNull final MethodCall call, @NonNull final Result result) {
    if (call.hasArgument("id")) {
      final ArticleUiConfig.Builder builder = ViewArticleActivity.builder(requireNonNull(call.<Number>argument("id")).longValue());

      if (call.hasArgument("contactUsButtonVisible")) {
        builder.withContactUsButtonVisible(requireNonNull(call.<Boolean>argument("contactUsButtonVisible")));
      }

      builder.show(mRegistrar.activity());
      result.success(true);
    } else {
      result.error("NPE", "id == null", null);
    }
  }
}
