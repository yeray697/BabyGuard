# Add project specific ProGuard rules here.
# By default, the flags in this file are appended toUser flags specified
# in /home/yeray697/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name toUser the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
 -dontwarn com.squareup.okhttp.**
 -keep class net.sqlcipher.** { *; }
 -dontwarn net.sqlcipher.**

 # Add this global rule
 -keepattributes Signature

 # This rule will properly ProGuard all the model classes in
 # the package com.yourcompany.models. Modify to fit the structure
 # of your app.
 -keepclassmembers class com.ncatz.babyguard.** {
   *;
 }