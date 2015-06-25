# UniversalCommunityAccount(UCA)

UCA是Android上的一个第三方账号登录库，目的是简化第三方SDK的集成步骤，将各种SDK集合在一起，开发者只需引入这一个包就可以。

## 用法

#### 使用Gradle引入

```gradle
dependencies {
    compile 'com.fatsoon:uca:0.1.3'
}
```
#### 配置

在`AndroidManifest.xml`中加入以下代码
```xml
<!--微博必须-->
<meta-data android:name="uca_weibo_key" android:value="weibo你的微博AppKey"/><!--例如weibo1158881934-->
<meta-data android:name="uca_weibo_redirect_url" android:value="你的微博RedirectUrl"/>
<!--qq互连必须 -->
<activity
    android:name="com.tencent.tauth.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="tencent你的qq互联AppId" /><!--例如tencent100415388-->
    </intent-filter>
</activity>
```





## 支持哪些第三方账号
| 名称 | 状态 |
|--------|--------|
|   微博  | 支持   |
|   QQ  | 支持   |
|   微信  | 即将支持   |

## License
```java
/*
 * Copyright (C) 2015 fatsoon <fanshuo1989@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```
