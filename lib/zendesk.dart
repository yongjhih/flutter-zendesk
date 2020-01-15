import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/foundation.dart';

class Zendesk {
  static const MethodChannel _channel =
      const MethodChannel('com.codeheadlabs.zendesk');

  Future<void> init(String accountKey) async {
    await _channel.invokeMethod('init', <String, String>{
      'accountKey': accountKey,
    });
  }

  Future<void> initSupport({
    @required
    String url,
    @required
    String appId,
    @required
    String clientId,
  }) async {
    await _channel.invokeMethod('initSupport', <String, String>{
      'url': url,
      'appId': appId,
      'clientId': clientId,
    });
  }

  Future<void> setVisitorInfo({
    String name,
    String email,
    String phoneNumber,
    String note,
  }) async {
    await _channel.invokeMethod('setVisitorInfo', <String, String>{
      'name': name,
      'email': email,
      'phoneNumber': phoneNumber,
      'note': note,
    });
  }

  Future<void> startChat() async {
    await _channel.invokeMethod('startChat');
  }

  Future<void> showHelpCenter({
    /// Long or List<Long>	One or more category ids
    Set<int> categories,
    /// Long or List<Long>	One or more section ids
    Set<int> sections,
    /// String or List<String>	One or more article label names
    Set<String> labels,
    bool contactUsButtonVisible,
  }) async {
    await _channel.invokeMethod('showHelpCenter', <String, dynamic>{
      'categories': categories,
      'sections': sections,
      'labels': labels,
      'contactUsButtonVisible': contactUsButtonVisible,
    });
  }

  Future<void> request({
    Set<String> tags,
    String subject,
  }) async {
    await _channel.invokeMethod('request', <String, dynamic>{
      'tags': tags,
      'subject': subject,
    });
  }

  Future<void> viewArticle({
    bool contactUsButtonVisible,
  }) async {
    await _channel.invokeMethod('viewArticle', <String, dynamic>{
      'contactUsButtonVisible': contactUsButtonVisible,
    });
  }
}
