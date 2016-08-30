# word2vec-example
1、使用Maven生成一个新项目
project_name=$1
mvn archetype:generate -DgroupId=$project_name -DartifactId=$project_name
如：
  project_name=word2vec-example
  mvn archetype:generate -DgroupId=$project_name -DartifactId=$project_name
2、修改pom.xml文件，加入依赖库
所需要的依赖库可以在以下链接找：http://mvnrepository.com/artifact

3、创建scala代码存放目录
 mkdir -p word2vec-example/src/main/scala
 
 4、编译打包
  mvn package
