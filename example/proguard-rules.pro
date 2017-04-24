# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-keep class !me.ele.** {*;}

# butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {@butterknife.* <fields>; }
-keepclasseswithmembernames class * {@butterknife.* <methods>;}

# eventbus
-keepclassmembers class * {
    public void onEvent*(**);
}

# router
-keep class **RouterManager** { *; }

# gandalf
#-keep class me.ele.gandalf.** {*;}

-dontwarn **

# jpush
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

# gexin
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

# greendao
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep public class AnnotationDatabaseImpl

-keepclassmembers class * extends android.webkit.WebChromeClient {
     public void openFileChooser(...);
     public void onShowFileChooser(...);
}

-keep class com.amap.api.services.**{*;}
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

# xiaomi
-keepclasseswithmembernames class com.xiaomi.**{*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver


-keepclasseswithmembers class me.ele.*.BuildConfig {
  *;
}