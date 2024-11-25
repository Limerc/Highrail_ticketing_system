## 环境要求
java1.7、
jdk17、
maven3、
openGauss 2.1.0

## 项目部署
### 1. 数据库配置  
1. 先执行：建表.sql
2. 再执行：车次区间触发器.sql
3. 利用Navicat的执行.sql文件导入数据;也可以用csv表单导入
![Navicat执行SQL文件]()
![选择sql文件]()
5. 再执行“其他触发器”文件夹中的.sql文件创建需要的触发器和函数

### 2. 目录结构描述
```
├── Readme.md                   // help文档  
├── src  
│   ├── main  
│   |    ├── java               // 主要功能代码  
│   |    |    ├── ClassDesign  
│   |    |    |      ├── hk_12306  
│   |    |    |      |      ├── config            // 扩展SpringMVC配置  
│   |    |    |      |      ├── controller        // 控制层  
│   |    |    |      |      ├── exception         // 全局错误捕获  
│   |    |    |      |      ├── interceptors      // 拦截器  
│   |    |    |      |      ├── mapper            // mapper接口  
│   |    |    |      |      ├── pojo              // 映射类  
│   |    |    |      |      ├── service           // 服务层  
│   |    |    |      |      ├── utils             // 工具类  
│   |    |    |      |      ├── ClassDesignApplication    // 启动类  
│   |    ├── resources              // 资源文件  
│   |    |    ├── static            //静态资源文件  
│   |    |    ├── templates         //模板文件  
│   |    |    ├── application.yaml  //项目配置文件  
│   ├── test  
|—— 其他触发器  
|—— 数据样本csv  
|—— 数据样本sql  
|—— pom.xml          // maven坐标依赖文件
|—— 建表语句.sql
└──  车次区间触发器.sql  
```
### 3. SpringBoot项目配置与启动
记得确保数据库正常运行
1. 用IDEA打开项目
2. 在src/main/resource/application.yaml文件下配置自己的环境
![Spring环境配置]()
3. 打开ClassDesginApplication，右键main方法旁边的小箭头，点击启动
![启动项目]()
4. 端口出现后就可以在浏览器访问啦（或者用Postman之类的测试工具测试端口也行）
![出现端口]()
![请求登录]()
![参数响应]()
