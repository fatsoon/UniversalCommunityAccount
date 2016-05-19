# UniversalCommunityAccount(UCA)

UCA是Android上的一个第三方账号登录库，目的是简化第三方SDK的集成步骤，将各种SDK集合在一起，开发者只需引入这一个包就可以。

## 用法

#### 使用Gradle引入

```gradle
dependencies {
    compile 'com.fatsoon:uca:0.1.9'
}
```
#### 配置

在`AndroidManifest.xml`中加入以下代码
```xml
<!--微博必须-->
<meta-data android:name="uca_weibo_key" android:value="weibo你的微博AppKey"/><!--例如weibo1158881934-->
<meta-data android:name="uca_weibo_redirect_url" android:value="你的微博RedirectUrl"/>
<!--qq互连必须 -->
<meta-data android:name="uca_qq_appid" android:value="tencent你的qq互联appid"/>
<!--微信必须 -->
<meta-data android:name="uca_wx_appid" android:value="你的微信appid"/>
```





## 支持哪些第三方账号
| 名称 | 状态 |
|--------|--------|
|   微博  | 支持   |
|   QQ  | 支持   |
|   微信  | 只支持分享   |

## License
[Apache License](LICENSE)
