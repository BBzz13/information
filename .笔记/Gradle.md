---
typora-root-url: ./
---



# Gradle

## 1、Gradle中 Projects 和 tasks

Gradle 里的任何东西都是基于这两个基础概念:

- projects ( 项目 )
- tasks ( 任务 )

每一个构建都是由一个或多个 projects 构成的。一个 project 到底代表什么取决于你想用 Gradle 做什么。

 举个例子，一个 project 可以代表一个 JAR 或者一个网页应用。它也可能代表一个发布的 ZIP 压缩包，这个 ZIP 可能是由许多其他项目的 JARs 构成的。但是一个 project 不一定非要代表被构建的某个东西。它可以代表一件**要做的事，比如部署你的应用。

每一个 project 是由一个或多个 tasks 构成的。 一个 task 代表一些更加细化的构建。可能是编译一些 classes， 创建一个 JAR， 生成 javadoc， 或者生成某个目录的压缩文件。

## 2、声明仓库

### 1.Maven 中央仓库

Maven Central 是一个流行的仓库，托管供 Java 项目使用的开源库。

要为您的构建声明 Maven 中央仓库(https://repo.maven.apache.org/maven2/)，请将其添加到您的脚本中：


```groovy
repositories {
    mavenCentral()
}
```

### 2.谷歌Maven仓库

Google 仓库托管特定于 Android 的构件，包括 Android SDK。

```groovy
repositories {
    google()
}
```

### 3.声明多个仓库

您可以定义多个仓库来解决依赖关系。如果某些依赖项仅在一个仓库中可用而在另一个仓库中不可用，则声明多个仓库会很有帮助。

```groovy
repositories {
    mavenCentral()
    maven {
        url "https://repo.spring.io/release"
    }
    maven {
        url "https://repository.jboss.org/maven2"
    }
}
```

### 4.支持的厂库类型

Gradle 支持广泛的依赖源，无论是在格式方面还是在连接方面。您可以从以下位置解决依赖关系：

- 不同的格式
  - 一个Maven 兼容构件仓库（例如：Maven Central）
  - 常春藤兼容 构件仓库（包括自定义布局）
  - 本地（平面）目录
- 具有不同的连接性
  - 经过身份验证的仓库
  - 各种各样的远程协议例如 HTTPS、SFTP、AWS S3 和谷歌云存储

一些项目可能更喜欢将依赖项存储在共享驱动器上或作为项目源代码的一部分而不是二进制仓库产品。如果您想使用（平面）文件系统目录作为仓库，只需键入：

```groovy
repositories {
    flatDir {
        dirs 'lib'
    }
    flatDir {
        dirs 'lib1', 'lib2'
    }
}
```

### 5.设置复合 Maven 厂库

仓库会将 POM 发布到一个位置，而将 JAR 和其他构件发布到另一个位置。

```groovy
repositories {
    maven {
        // Look for POMs and artifacts, such as JARs, here
        url "http://repo2.mycompany.com/maven2"
        // Look for artifacts here if not found at the above location
        artifactUrls "http://repo.mycompany.com/jars"
        artifactUrls "http://repo.mycompany.com/jars2"
    }
}
```

### 6.本地Maven仓库

Gradle 可以使用本地 Maven 仓库) 中可用的依赖项。声明此仓库对于使用一个项目发布到本地 Maven 仓库并在另一个项目中使用 Gradle 使用构件的团队是有益的。

Gradle 将已解析的依赖项存储在自己的缓存中。即使您从基于 Maven 的远程仓库解析依赖项，构建也不需要声明本地 Maven 仓库。

```groovy
repositories {
    mavenLocal()
}
```

Gradle 使用与 Maven 相同的逻辑来识别本地 Maven 缓存的位置。如果在 `settings.xml` 中定义了本地仓库位置，则将使用该位置。 `<home directory of the current user>/.m2` 中的 `settings.xml` 优先于 `*M2_HOME*/conf` 中的 `settings.xml`。如果没有可用的 `settings.xml`，Gradle 使用默认位置 `<home directory of the current user>/.m2/repository`。

### 7.mavenLocal() 的案例

作为一般建议，您应该避免将 `mavenLocal()` 添加为仓库。您应该注意使用 `mavenLocal()` 的不同问题：

- Maven 将它用作缓存，而不是仓库，这意味着它可以包含部分模块。
  - 例如，如果 Maven 从未下载给定模块的源文件或 javadoc 文件，Gradle 也不会找到它们，因为一旦找到模块，它就会在单个厂库中搜索文件。
- 作为本地仓库 ，Gradle 不信任它的内容，因为：
  - 无法追踪构件的来源，这是一个正确性和安全性问题
  - 构件很容易被覆盖，这是一个安全性、正确性和可重复性问题
- 为了减轻元数据和/或构件可以更改的事实，Gradle 不会为本地仓库执行任何缓存
  - 因此，您的构建速度较慢
  - 鉴于仓库的顺序很重要，添加 `mavenLocal()` *first* 意味着您的所有构建都会变慢

在某些情况下您可能必须使用 `mavenLocal()` ：

- 与 Maven 的互操作性
  - 比如项目A是用Maven构建的，项目B是用Gradle构建的，开发过程中需要共享构件
  - *always* 最好使用内部全功能仓库
  - 如果这是不可能的，您应该将其限制为 *local builds only*
- 与 Gradle 本身的互操作性
  - 在多仓库世界中，您想要检查对项目 A 的更改是否适用于项目 B
  - 对于此用例，最好使复合构建
  - 如果出于某种原因既无法构建复合构建也无法构建全功能仓库，那么 `mavenLocal()` 是最后的选择

在所有这些警告之后，如果您最终使用 `mavenLocal()` ，请考虑将其与仓库过滤器 结合使用。这将确保它只提供预期的内容，而不提供其他内容。

### 8.Spring知识库

组织可能决定在内部 Ivy 仓库中托管依赖项。 Gradle 可以通过 URL 声明 Ivy 仓库。

定义具有标准布局的 Ivy 仓库

要使用标准布局声明 Ivy 仓库，不需要额外的自定义。您只需声明 URL。

```groovy
repositories {
    ivy {
        url "http://repo.mycompany.com/repo"
    }
}
```

### 9.为 Ivy 厂库定义命名布局

您可以使用命名布局指定您的仓库符合 Ivy 或 Maven 默认布局。

```groovy
repositories {
    ivy {
        url "http://repo.mycompany.com/repo"
        layout "maven"
    }
}
```

### 10.为 Ivy 厂库定义自定义模式布局

要使用非标准布局定义 Ivy 仓库，您可以为仓库定义 *pattern* 布局：

```groovy
repositories {
    ivy {
        url "http://repo.mycompany.com/repo"
        patternLayout {
            artifact "[module]/[revision]/[type]/[artifact].[ext]"
        }
    }
}
```

要定义从不同位置获取 Ivy 文件和构件的 Ivy 仓库，您可以定义单独的模式以用于定位 Ivy 文件和构件：

为仓库指定的每个 `artifact` 或 `ivy` 添加一个 *additional* 模式以供使用。模式按照它们被定义的顺序使用。

```groovy
repositories {
    ivy {
        url "http://repo.mycompany.com/repo"
        patternLayout {
            artifact "3rd-party-artifacts/[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"
            artifact "company-artifacts/[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"
            ivy "ivy-files/[organisation]/[module]/[revision]/ivy.xml"
        }
    }
}
```

可选地，具有模式布局的仓库可以将其 `'organisation'` 部分以 Maven 样式进行布局，使用正斜杠代替点作为分隔符。例如，组织 `my.company` 将表示为 `my/company` 。

```groovy
repositories {
    ivy {
        url "http://repo.mycompany.com/repo"
        patternLayout {
            artifact "[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"
            m2compatible = true
        }
    }
}
```

### 11.访问经过身份验证的 Ivy 仓库

您可以为受基本身份验证保护的 Ivy 仓库指定凭据。

```groovy
repositories {
    ivy {
        url "http://repo.mycompany.com"
        credentials {
            username "user"
            password "password"
        }
    }
}
```

### 12.仓库过滤器

Gradle 公开了一个 API 来声明仓库可能包含或不包含的内容。它有不同的用例：

- 性能，当您知道永远不会在特定仓库中找到依赖项时
- 安全性，通过避免泄露私有项目中使用的依赖项
- 可靠性，当某些仓库包含损坏的元数据或构件时

考虑到仓库的声明顺序很重要时，这一点就更加重要了。

声明仓库过滤器

```groovy
repositories {
    maven {
        url "https://repo.mycompany.com/maven2"
        content {
            // this repository *only* contains artifacts with group "my.company"
            includeGroup "my.company"
        }
    }
    mavenCentral {
        content {
            // this repository contains everything BUT artifacts with group starting with "my.company"
            excludeGroupByRegex "my\\.company.*"
        }
    }
}
```

### 13.Maven 仓库过滤

对于Maven 仓库，通常情况下仓库将包含版本或快照。

```groovy
repositories {
    maven {
        url "https://repo.mycompany.com/releases"
        mavenContent {
            releasesOnly()
        }
    }
    maven {
        url "https://repo.mycompany.com/snapshots"
        mavenContent {
            snapshotsOnly()
        }
    }
}
```

## 3、声明依赖

#### **依赖管理的核心**

- Gradle依赖管理是依靠俩个classpath：`compileClasspath、runtimeClasspath`
- compileClasspath:编译时能使用的代码和类，当一个组件参与编译时，Gradle会将其放在compileClasspath中
- runtimeClasspath：运行时使用的代码和类，当一个组件参与打包时，Gradle就会将其放在runtimeClasspath中
- 编译时：代码还在编写阶段，只要还没有编译为class，就是编译时
- 运行时：当编译成class文件，在机器上运行的时候叫做运行时
- compileClasspath中含有的代码和类库，是我们在编写代码的时候需要使用到的类库，如果这里面没有的类库，我们编写时是会找不到该类的
- runtimeClasspath这个里面主要包括app运行时需要的类库，如果这个里面没有包含的库，那么运行时找不到类而crash
- implementation、api 这些操作符只是对依赖库不同的操作方式，其核心的逻辑就是，从远程拉下来的库到底是放在`compileClasspath`中还是`runtimeClasspath`还是`俩个都放`

#### **依赖管理配置**

目前Gradle版本支持的依赖配置`implementation、api、compileOnly、runtimeOnly 和 annotationProcessor`已废弃的是`compile、provided、apk、providedCompile`

下面我们通过implementation和api来理解下`compileClasspath`和`runtimeClasspath`

**implementation**

会添加依赖到编译路径，并且会将依赖打包输出到aar/apk,但编译时不会把依赖暴露给其他moudle

比如：A implementation B  B implementation C

- 在B中，可以使用C中类库
- 在A中，不能使用C只给的类库，但是可以使用B中类库
- 这是因为`implementation`引入的依赖，会把C加入B的`compileClasspath`和`runtimeClasspath`，会把C加入A的`runtimeClasspath`
- 因为C没有加入A的compileClasspath，所以A没有办法在编译时访问C中的类库，又是因为C加入A的runtimeClasspath，所以A可以在运行时访问C类库

**api**

会添加依赖到编译路径，并且把依赖打包输出到aar/apk,与implementation不同的是，这个依赖可以传递

例如：A implementation B   B api C

- 在B中，可以使用C中类库
- 在A中，可以使用B中类库，也可以使用C中类库
- 这是因为api引入的依赖，会把C加入B的`compileClasspath`和`runtimeClasspath`，同时会把C加入A的`compileClasspath`和`runtimeClasspath`，所以A也可以在编译时访问C中类库

**compileOnly**

依赖只在编译时使用，不会打包到aar/apk运行时不能使用

例如：A implementation B   B compileOnly C

- A访问不到C的代码，B可以访问C，且C不会打包到apk中

**runtimeOnly**

依赖编译时不能使用，只会打包到aar/apk运行时使用

例如：A implementation B   B runtimeOnly C

- AB都不可以调用C中代码，但是C会打包到APK中

#### 依赖方式说明

- implementation
  这个指令的特点就是，对于使用了该命令编译的依赖，对该项目有依赖的项目将无法访问到使用该命令编译的依赖中的任何程序，也就是将该依赖隐藏在内部，而不对外部公开。
- api
  完全等同于compile指令。
- compile
  这种是我们最常用的方式，使用该方式依赖的库将会参与编译和打包。
- testCompile
  testCompile 只在单元测试代码的编译以及最终打包测试apk时有效。
- debugCompile
  debugCompile 只在debug模式的编译和最终的debug apk打包时有效。
- releaseCompile
  releaseCompile 仅仅针对Release模式的编译和最终的Release apk打包

### 1.从另一个配置扩展一个配置

```groovy
configurations {
    smokeTest.extendsFrom testImplementation
}

dependencies {
    testImplementation 'junit:junit:4.13'
    smokeTest 'org.apache.httpcomponents:httpclient:4.5.5'
}
```

### 2.可解析和可消费的配置

配置是 Gradle 中依赖解析的基本部分。在依赖解析的上下文中，区分 *consumer* 和 *producer* 很有用。按照这些思路，配置至少具有 3 个不同的角色：

1. 声明依赖关系
2. 作为 *consumer* ，解析一组对文件的依赖关系
3. 作为 *producer* ，公开构件及其依赖项以供其他项目使用（例如 *consumable* 配置通常代表生产者向其消费者提供的 [变体](https://doc.qzxdp.cn/gradle/8.1.1/userguide/variant_model.html#understanding-variant-selection) ）

例如，要表示应用程序 `app` *depends on* 库 `lib` , *at least* 需要一个配置：

```groovy
configurations {
    // 声明名为“someConfiguration”的“configuration”
    someConfiguration
}
dependencies {
    // 将项目依赖项添加到“someConfiguration”配置
    someConfiguration project(":lib")
}
```

配置可以通过扩展其他配置来继承它们的依赖关系。现在，请注意上面的代码没有告诉我们任何有关此配置的预期 *consumer* 的信息。特别是，它没有告诉我们配置的含义是 *used* 。假设 `lib` 是一个 Java 库：它可能公开不同的东西，例如它的 API、实现或测试装置。根据我们正在执行的任务（针对 `lib` 的 API 进行编译、执行应用程序、编译测试等），可能需要更改我们解析 `app` 依赖项的方式。为了解决这个问题，您通常会找到配套配置，这些配置旨在明确声明用法：

```groovy
configurations {
    // 声明一个配置，该配置将解析应用程序的编译类路径
    compileClasspath.extendsFrom(someConfiguration)

    // 声明一个配置，该配置将解析应用程序的运行时类路径
    runtimeClasspath.extendsFrom(someConfiguration)
}
```

此时，我们有 3 种具有不同角色的不同配置：

- `someConfiguration` 声明了我的应用程序的依赖项。它只是一个可以容纳依赖项列表的桶。
- `compileClasspath` 和 `runtimeClasspath` 是配置 *meant to be resolved* ：解析后它们应该分别包含应用程序的编译类路径和运行时类路径。

这种区别由 `Configuration` 类型中的 `canBeResolved` 标志表示。 *can be resolved* 的配置是我们可以为其计算依赖图的配置，因为它包含发生解析所需的所有信息。也就是说，我们要计算一个依赖图，解析图中的组件，并最终得到构件。 `canBeResolved` 设置为 `false` 的配置并不意味着要解析。有这样的配置*only to declare dependencies*。原因是根据使用情况（编译类路径、运行时类路径），它*can*解析为不同的图。尝试解析将 `canBeResolved` 设置为 `false` 的配置是错误的。在某种程度上，这类似于不应该实例化的*abstract class*(`canBeResolved` =false)，以及扩展抽象类的具体类(`canBeResolved` =true)。可解析配置将扩展至少一个不可解析配置（并且可能扩展不止一个）。

另一方面，在库项目端（*producer*），我们也使用配置来表示可以使用的内容。例如，库可能公开 API 或运行时，我们会将构件附加到其中之一、另一个或两者。通常，要针对 `lib` 进行编译，我们需要 `lib` 的 API，但不需要它的运行时依赖项。因此 `lib` 项目将公开一个 `apiElements` 配置，该配置针对寻找其 API 的消费者。这样的配置是可消费，但不是要解决的。这是通过 `Configuration` 的 *canBeConsumed* 标志表示的：

```groovy
configurations {
    // A configuration meant for consumers that need the API of this component
    exposedApi {
        // This configuration is an "outgoing" configuration, it's not meant to be resolved
        canBeResolved = false
        // As an outgoing configuration, explain that consumers may want to consume it
        canBeConsumed = true
    }
    // A configuration meant for consumers that need the implementation of this component
    exposedRuntime {
        canBeResolved = false
        canBeConsumed = true
    }
}
```





# Gradle插件

## Gradle Plugin插件

### 一、Settings类

settings.gradle（对应Settings.java）决定哪些工程需要被gradle处理，占用了整个gradle生命周期的三分之一，即Initialzation初始化阶段。

### 二、SourceSet类

对默认的文件位置进行修改，从而让gradle知道哪种资源要从哪些文件夹中去查找。

```groovy
// sourceSets是可以调用多次的
android {
    sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }
    sourceSets {
        main {
            res.srcDirs = ['src/main/res',
                           'src/main/res-ad',
                           'src/main/res-player']
        }
    }
}

// sourceSets一般情况下是一次性配置
android {
    sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
            res.srcDirs = ['src/main/res',
                           'src/main/res-ad',
                           'src/main/res-player']
        }
    }
}

// 使用编程的思想，配置sourceSets
this.android.sourceSets{
    main {
        jniLibs.srcDirs = ['libs']
        res.srcDirs = ['src/main/res',
                       'src/main/res-ad',
                       'src/main/res-player']
    }
}
```

### 三、Gradle插件（Plugin）是什么

Gradle中的Plugin是对完成指定功能的Task封装的体现，只要工程依赖了某个Plugin，就能执行该Plugin中所有的功能，如：使用java插件，就可以打出jar包，使用Android插件，就可以生成apk、aar。

### 四、自定义Plugin

1、创建插件工程
在工程目录下创建buildSrc文件夹。
在buildSrc目录下，创建src文件夹、build.gradle文件。
在buildSrc/src目录下，再创建main文件夹。
在buildSrc/src/main目录下，再分别创建groovy、resources文件夹。
在buildSrc/src/main/resources再创建一个META-INF文件夹，再在META-INF下创建一个gradle-plugins文件夹。
在build.gradel文件中输入如下脚本：




```groovy
apply plugin: 'groovy'

sourceSets {
    main {
        groovy {
            srcDir 'src/main/groovy'
        }
        resources {
            srcDir 'src/main/resources'
        }
    }
}
```

最后，Async一下工程，buildSrc就会被识别出来了






# 使用依赖项

```groovy
//声明gradle脚本自身需要使用的资源。
//可以声明的资源包括依赖项、第三方插件、maven仓库地址等。
//gradle在执行脚本时，会优先执行buildscript代码块中的内容，然后才会执行剩余的build脚本
buildscript {
   repositories {
       //声明多个厂库
       //阿里云Maven仓库
       maven { 
           url "https://maven.aliyun.com/repository/spring-plugin" 
       }
       maven { 
           url "https://maven.aliyun.com/nexus/content/repositories/spring-plugin" 
       }
       //spring镜像仓库地址
       maven { 
           url "https://repo.spring.io/plugins-release" 
       }
   }
   dependencies {
       //引入全局环境依赖
       classpath("io.spring.gradle:propdeps-plugin:0.0.9.RELEASE")
       classpath("org.asciidoctor:asciidoctorj-pdf:1.5.0-alpha.16")
   }
}

// 第三方插件存储库可以在settings.gradle中进行配置
plugins {
    id "io.spring.dependency-management" version "1.0.7.RELEASE" apply false
    id "org.jetbrains.kotlin.jvm" version "1.2.71" apply false
    id "org.jetbrains.dokka" version "0.9.18"
    id "org.asciidoctor.convert" version "1.5.8"
}
```





