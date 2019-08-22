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
    await _channel.invokeMethod('init', <String, String>{
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
    /// categoryIds	Long or List<Long>	One or more category ids
    Set<String> categoryIds,
    /// sectionIds	Long or List<Long>	One or more section ids
    Set<String> sectionIds,
    /// labelNames	String or List<String>	One or more article labels
    Set<String> labelNames,
    bool contactUsButtonVisible,
  }) async {
    await _channel.invokeMethod('showHelpCenter', <String, String>{
      // TODO
    });
  }
}
