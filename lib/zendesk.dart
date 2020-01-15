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

  Future<void> setIdentity({
    String name,
    String email,
  }) async {
    final args = <String, String>{};
    if (name != null) {
      args.putIfAbsent('name', () => name);
    }
    if (email != null) {
      args.putIfAbsent('email', () => email);
    }
    await _channel.invokeMethod('setIdentity', args);
  }

  Future<void> setVisitorInfoAndIdentity({
    String name,
    String email,
    String phoneNumber,
    String note,
  }) async {
    final args = <String, String>{};
    if (name != null) {
      args.putIfAbsent('name', () => name);
    }
    if (email != null) {
      args.putIfAbsent('email', () => email);
    }
    if (phoneNumber != null) {
      args.putIfAbsent('phoneNumber', () => phoneNumber);
    }
    if (note != null) {
      args.putIfAbsent('note', () => note);
    }
    await _channel.invokeMethod('setVisitorInfoAndIdentity', args);
  }

  Future<void> setVisitorInfo({
    String name,
    String email,
    String phoneNumber,
    String note,
  }) async {
    final args = <String, String>{};
    if (name != null) {
      args.putIfAbsent('name', () => name);
    }
    if (email != null) {
      args.putIfAbsent('email', () => email);
    }
    if (phoneNumber != null) {
      args.putIfAbsent('phoneNumber', () => phoneNumber);
    }
    if (note != null) {
      args.putIfAbsent('note', () => note);
    }
    await _channel.invokeMethod('setVisitorInfo', args);
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
    final args = <String, dynamic>{};
    if (categories != null) {
      args.putIfAbsent('categories', () => categories);
    }
    if (sections != null) {
      args.putIfAbsent('sections', () => sections);
    }
    if (labels != null) {
      args.putIfAbsent('labels', () => labels);
    }
    if (contactUsButtonVisible != null) {
      args.putIfAbsent('contactUsButtonVisible', () => contactUsButtonVisible);
    }
    await _channel.invokeMethod('showHelpCenter', args);
  }

  Future<void> request({
    Set<String> tags,
    String subject,
  }) async {
    final args = <String, dynamic>{};
    if (tags != null) {
      args.putIfAbsent('tags', () => tags);
    }
    if (subject != null) {
      args.putIfAbsent('subject', () => subject);
    }
    await _channel.invokeMethod('request', args);
  }

  Future<void> viewArticle(int id, {
    bool contactUsButtonVisible,
  }) async {
    final args = <String, dynamic>{};
    args.putIfAbsent('id', () => id);
    if (contactUsButtonVisible != null) {
      args.putIfAbsent('contactUsButtonVisible', () => contactUsButtonVisible);
    }
    await _channel.invokeMethod('viewArticle', args);
  }
}
