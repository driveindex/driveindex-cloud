# DriveIndex-Cloud

这里是 DriveIndex 后端的 SpringCloud 实现，支持 SpringBoot 单体部署。

## 开发环境

+ IDE：JetBrains IDEA Ultimate 2022.2.1
+ Java：JDK 17.0.3.1
+ Database：H2Database + MyBatis Plus
+ Consul：1.12.3

## 构建

自行构建需要 `JDK 17`、`maven`，请自行查阅安装方法。

项目结构：

```
driveindex-cloud
├─admin-module        # 后台管理模块
│  ├─admin-common     # 后台管理逻辑
│  └─admin-service    # SpringCloud 后台管理服务
├─azure-module        # OneDrive 文件模块
│  ├─azure-common     # OneDrive 文件逻辑
│  └─azure-service    # SpringCloud OneDrive 文件服务
├─boot                # SpringBoot 模块
├─commons             # 通用逻辑模块
├─gateway-module      # Gateway 模块
│  ├─gateway          # SpringCloud Gateway 服务
│  └─gateway-common   # Gateway 逻辑
└─logging-service     # SpringCloud 日志聚合模块，暂未维护

```

### 单体部署

打包模块 `boot` 即可，得到单个 `*-bin.jar` 文件直接运行。

### 分布式部署

打包模块 `admin-module/admin-service`、`azure-module/azure-service`、`gateway-module/gateway`，得到三个 `*-bin.jar` 文件，分别部署即可。