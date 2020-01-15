# zendesk

Flutter interface for Zendesk Mobile SDK

## Installation

Make sure the theme is applied AppCompat, for example:

```xml
    <style name="ZendeskTheme" parent="Theme.MaterialComponents.Light.DarkActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

        <activity android:name="zendesk.support.guide.HelpCenterActivity"
            android:exported="true"
            android:theme="@style/ZendeskTheme" />

        <activity android:name="zendesk.support.guide.ViewArticleActivity"
            android:theme="@style/ZendeskTheme" />

        <activity android:name="zendesk.support.requestlist.RequestListActivity"
            android:theme="@style/ZendeskTheme" />

        <activity android:name="zendesk.support.request.RequestActivity"
            android:theme="@style/ZendeskTheme" />
```


