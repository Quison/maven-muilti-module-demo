# Maven多模块构建项目

标签（空格分隔）： Maven

---

## 1.概述
　　使用Maven多模块构建项目有两种方式：
> 1. 聚合，主要是为了方便快速构建项目；
> 2. 基础，主要是为了消除重复配置。
　　
　　他们的区别如下：
> 1. 对于聚合模块来说，他知道哪些被聚合的模块，但那些被聚合的模块不知道这些聚合模块的存在；
> 2. 对于继承关系的父POM来说，它不知道有哪些子模块继承于它，但那些子模块必须知道自己POM是什么。

　　他们的打包方式都是POM，并且我们通常会将这两种情况结合使用。

## 2.聚合
　　在某些情况下我们会一次构建多个项目，通过Maven的聚合特性，我们可以一次性完成构建。首先我们需要额外构建一个Maven项目专门用于聚合，它不包含出了pom文件之外的任何文件，**并且其打包的方式必须为pom**，其pom.xml文件内容如下：
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.moguichun</groupId>
    <artifactId>mvn-aggergator</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>mvn-module1</module>
        <module>mvn-module2</module>
    </modules>
</project>
```
　　在聚合中，子模块可以和父模块平行，但是父模块中加载模块是需要写对路径。
　　
## 3.继承
　　在通过聚合特性来聚合项目的各个模块时，我们发现子模块中的pom.xml配置会大量的重复，所以我们可以通过继承的特性消除这些重复。
　　和面向对象中的继承一致，Maven中的继承也是父子结构，我们可以在父POM中声明一些配置供子POM继承。我们需要先创建一个名为**mvn-parent**的maven项目，它只包含pom文件。
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.moguichun</groupId>
    <artifactId>mvn-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging>

	<modules>
		<module>mvn-dao</module>
		<module>mvn-service</module>
	    <module>mvn-model</module>
		<module>mvn-web</module>
	</modules>
</project>
```
　　子模块的pom.xml如下：
```
<project
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
	xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.moguichun</groupId>
		<artifactId>mvn-parent</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</parent>

	<artifactId>mvn-dao</artifactId>
	<packaging>jar</packaging>
	<name>mvn-dao</name>
	<url>http://maven.apache.org</url>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>
	<dependencies>
		<dependency>
			<groupId>com.moguichun</groupId>
			<artifactId>mvn-model</artifactId>
			<version>${project.version}</version>
		</dependency>
	</dependencies>
</project>

```
　　在子模块中，其坐标只需要声明artifactId，而groupId和version会继承自父模块的坐标。并且该子模块如果依赖于其他模块那么就需要自己写dependency。

### 3.1 可继承的元素
　　在Maven多模块中，可以继承的元素如下：
|元素|描述|
|----|----|
|groupId|项目组ID，项目坐标的核心元素|
|version|项目版本，项目坐标的核心元素|
|description|项目的描述信息|
|organazation|项目的组织信息|
|inceptionYear|项目的创始年份|
|developers|项目的开发者信息|
|contributors|项目的贡献者信息|
|distributionManagement|项目的部署信息|
|issueManagement|项目的缺陷跟踪系统信息|
|ciManagement|项目的持续集成系统信息|
|scm|项目的版本控制系统信息|
|mailingLists|项目的邮件列表信息|
|properties|自定义的Maven属性|
|dependencies|项目的依赖配置|
|dependencyManagement|项目的依赖管理配置|
|repositories|项目的仓库配置|
|build|包括项目的源码目录配置、输出目录配置、插件配置、插件管理配置等|
|reporting|包括项目的报告输出目录配置、报告插件配置|

### 3.2 依赖管理
　　只要是上节中所列出的元素在父模块中配置后，子模块就会继承。但是在某些情况下，一些特殊的子模块比如util模块，不需要继承父模块中配置的一些依赖如springframek等等，那么我们可以使用**dependencyManagement元素来解决这种情况。
　　Maven提供的dependencyManagement元素既能让子模块继承到父模块的依赖配配置，也可以保证子模块依赖使用的灵活性。
　　首先我们在mvn-parent文件中使用dependencyManagement元素进行配置。
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.moguichun</groupId>
	<artifactId>mvn-parent</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>pom</packaging>

	<modules>
		<module>mvn-dao</module>
		<module>mvn-service</module>
	    <module>mvn-model</module>
		<module>mvn-web</module>
	</modules>

	<properties>
		<springframework.version>2.5.6</springframework.version>
		<junit.version>4.7</junit.version>
	</properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-core</artifactId>
                <version>${springframework.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-beans</artifactId>
                <version>${springframework.version}</version>
            </dependency>            
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context</artifactId>
                <version>${springframework.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context-suport</artifactId>
                <version>${springframework.version}</version>
            </dependency>    
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```
　　对应的子模块pom.xml文件内容如下：
```
<properties>
    <javax.mail.version>1.4.1</javax.mail.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
    <dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-beans</artifactId>
    <dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
    <dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context-suppport</artifactId>
    <dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
    <dependency>
    <dependency>
        <groupId>javax.mail</groupId>
        <artifactId>mail</artifacId>
        <version>${javax.mail.version}</version}
    <dependency>
</dependendies>
```
　　可见，子模块的配置比原先的简单了许多，所有的springframework依赖只配置了groupId和artifactId，省去了version，而junit还省去了scope，这些信息之所以能够省略不配置，是因为该子模块继承了父模块中的dependencyManagement配置，因为完整的配置已经声明在父POM中，所有子模块只需要配置groupId和artifactId就可以获取到完整的依赖。但是如果子模块中不声明依赖的使用，即使父模块中声明了dependencyManagement也不会产生任何效果。
　　在dependencyManagement元素下，有一个位**import**的依赖范围，使用该依赖范围通常是只想一个POM，作用是将目标POM中的dependencyManagement配置导入合并到当前的POM的dependencyManagement元素中，例如想要在另外一个模块中使用与mvn-parent完全一样的dependencyManagement配置，除了赋值外还可以使用import范围进行导入，代码如下所示：
```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.moguichun</groupId>
            <artifactId>mvn-parent</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        <dependency>
    <dependencies>
<dependencyManagement>
```

### 3.3 插件管理
　　Maven提供了dependencyManagement元素帮助管理依赖，类似的，Maven也提供了一个pluginManagement元素帮助管理插件。该元素中配置的依赖不会造成实际的插件调用行为，当POM中配置了真正的plugin元素，并且其groupId和artifactId与pluginManagement中配置的插件匹配时，pluginManagement的配置才会影响实际的插件行为。
　　例如父POM如下：
```
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>2.1.1</version>
                <executions>
                    <execution>
                        <id>attach-sources</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>jar-no-fork</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```
　　继承了上面父模块想使用maven-source-plugin插件只需要如下配置就好了。
```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
## ４.反应堆
### 4.1 反应堆的顺序
　　在一个多模块的Maven项目中，反应堆（Reactor）是指所有模块组成的一个构建结构，对于单个模块的项目反应堆就是该模块本身，但对于多模块来说，反应堆就包含了各个模块之间的继承和依赖关系，从而能够自动计算出合理的模块构建顺序。
　　而Maven中实际的构建模块是这样形成的：Maven按序读取POM，如果该POM没有依赖，那么就直接构建该模块，否则就先构建其依赖的模块，如果该依赖还依赖于其他模块，则进一步优先构建依赖的依赖。
　　模块间的依赖会将反应堆构成一个有向非循环图，如果出现循环依赖，那么Mavne就好报错。
### 4.2 裁剪反应堆
　　一般来说，用户会选择构建整个项目或者选择构建单个模块，但有些时候，用户会想要仅仅构建完整的某些模块。换句话说，用户需要实时的裁剪反应堆。裁剪反应堆的命令如下：
> 1. -am，--also-make 同时构建所列模块的依赖模块；
> 2. -amd，--also-make-dependents 同时构建依赖于所有模块的模块；
> 3. -pl，--projects <arg> 构建指定的模块，模块间用逗号分隔；
> 4. -rf，-resume-from <arg> 从指定的模块回复反应堆。

　　在大型的项目中，我们可以灵活的使用这些命令构建项目，而不需要全部构建。
　　
## 5.war包之间的合并依赖
　　某些情况下，为了提高开发效率，避免重复造轮子，我们可以使用Maven中的maven-war-plugin插件将多个web项目合并，例如我们开发一套通用的UI，然后让它成为公共的模块，供其它项目复用。
Maven的Overlay模式能够将一个外部的war包部署到当前的war包中，包括静态文件，和class，最后的效果是将两个war包合并起来，默认以webapp为根目录，将路径相同的文件存放到一起，这个路径可以通过overlay的子元素targetPath进行配置。
demo中，我们的mvn-web模块依赖了common这个web项目。只需要在mvn-web这个主项目的pom.xml文件中进行如下配置就能够将common的war包合并到mvn-web中：
1.添加依赖：
```
	<dependency>
			<groupId>com.moguichun</groupId>
			<artifactId>webcommon</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<type>war</type>
	</dependency>
```
2.配置插件：
```
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-war-plugin</artifactId>
				<version>2.1.1</version>
				<configuration>
					<overlays>
						<overlay>
							<groupId>com.moguichun</groupId>
							<artifactId>webcommon</artifactId>
						</overlay>
					</overlays>
				</configuration>
	</plugin>
```

这只是最简单的将两个war项目合并，我们发现打包发布mvn-web项目时，webcommon中的资源文件会出现在mvn-web包中，我们访问mvn-web/pages/index.jsp就可以访问到webcommon中的这个index.jsp页面。我们可以根据项目的需要对overlay进行更多的配置，以符合我们项目的要求。详细的overlay配置，请参见：
http://maven.apache.org/plugins/maven-war-plugin/overlays.html
