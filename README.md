# Introduction

An android scaffold project for publish android library to maven center on enviroment of Gradle 6.7.1 and Gradle plugin 4.2.2 .


## Difference when publish android library in different Gradle version 

In 2023 years, publishing an android library is very simple. However, it only established when you use environment of Gradle above 7.0 and Gradle plugin above 7.0. When you use old version, such as Gradle 6.7.1 and Gradle plugin 4.2.2, the situation is totally different.


Main different points is list there:

1. The `setting.gradle` file play different role in Gradle version above 7.0 and below 7.0.

   - Gradle version > 7.0, `setting.gradle` file will include some modules and manage dependency repository.  

   - Gradle version < 7.0, `setting.gradle` file only include modules, the dependency repository script will be written in `build.gradle` file.

2. Build artifact generate

   - Gradle version > 7.0, publish will auto generator build artifact, you only need to include the release artifact. It will include source code, doc file, resource file, etc.

   - Gradle version < 7.0, you need to generate build artifact by write task manually, then include the task in script.


## Reference

Publish configuration mostly reference from [android-gpuimage](https://github.com/cats-oss/android-gpuimage) project. It is a excellent project, It's source  `build.gradle` file location is [there](https://github.com/cats-oss/android-gpuimage/blob/master/library/build.gradle).
If you want to publish library on old Gradle and Gradle plugin version, you could refer this project carefully.