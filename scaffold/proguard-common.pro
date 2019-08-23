# 从官方 proguard-android.txt 拷贝过来的配置方便我们自己定义，和防止被外界篡改
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#
# This file is no longer maintained and is not used by new (2.2+) versions of the
# Android plugin for Gradle. Instead, the Android plugin for Gradle generates the
# default rules at build duration and stores them in the build directory.

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
#-keep public class com.google.vending.licensing.ILicensingService
#-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends androidx.core.content.FileProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.Service


# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep  class * implements android.os.Parcelable {
  *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
#-dontwarn android.support.**

# Understand the @Keep support annotation.
-keep class androidx.annotation.Keep

-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
#-keepnames public class * extends androidx.fragment.app.Fragment


# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**

#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保留行号
-keepattributes SourceFile,LineNumberTable
#保持泛型
-keepattributes Signature
#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile

#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#指定压缩级别
-optimizationpasses 5


-dontnote androidx.**
-dontwarn androidx.**

# 移除所有log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int d(...);
    public static int w(...);
    public static int v(...);
    public static int i(...);
}

# material
-dontwarn com.google.android.material.**
-keep class com.google.android.material.**{*;}