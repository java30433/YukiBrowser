还没开发好！！

注：本地构建需要配置签名，请在app/目录下新建secret.gradle.kts文件，文件内容如下：
```
rootProject.extra["storeFile"] = file("path/to/your/file")
rootProject.extra["storePassword"] = "xxxx"
rootProject.extra["keyAlias"] = "xxxx"
rootProject.extra["keyPassword"] = "xxxx"
```
或者在app/build.gradle.kts中移除signingConfig块。