# wejuai-console

为聚爱控制台服务，对项目整体的控制和统计服务

### 外部关联
- aliyun oss
- wejuai-trade
- wejuai-core
- wejuai-weixin-service

### 配置项
- 详情参考`wejuai-config-server`中的配置文件
- `bootstrap.yml`中的config-server配置
- `build.gradle`中的github或者其他获得dto和entity以及工具包的仓库
- `config/HttpTraceActuatorConfig.java`中配置该系统的账号密码

### 本地运行
1. 配置项以及其中的第三方服务开通
   gradle build，其中github的仓库必须使用key才可以下载，需要在个人文件夹下的.gradle/gradle.properties中添加对应的key=value方式配置，如果不行，就去下载对应仓库的代码本地install一下
2. 启动配置项中的数据库
3. 分别运行`Application.java`的`main()`方法

### docker build以及运行
- 运行gradle中的docker build task
- 如果配置了其中的第三方仓库可以运行docker push，会先build再push
- 运行方式 docker run {image name:tag}，默认是运行的profile为dev，可以通过环境变量的方式修改，默认启动配置参数在Dockerfile中