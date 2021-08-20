##  Jenkins Pipeline Jenkins流水项目构建

Jenkins创建自由风格或者Jmaven项目需要维护一套从源码管理到构建过程，再到和构建后的操作，对于项目的
维护以及持续集成还是比较繁琐的，如果想要更加方便的管理项目，可以考虑Pipeline工作流框架。

### Jenkins Pipeline 简介

> Pipeline是一套运行在Jenkins上的工作流框架，将原来独立运行于单个或者多个任务的节点任务连接起来，
> 实现单个任务难以完成的复杂流程编排和可视化的工作。

### 使用Pipeline的优势

> 代码：Pipeline以代码的形式实现，通常被检入源代码控制，使团队能够编辑，审查和迭代其传送流程。  
> 耐用：Pipeline可以在计划和计划外重新启动Jenkins管理时同时存在。  
> Pausable：Pipeline可以选择停止并等待人工输入或批准，然后再继续Pipeline运行。  
> 多功能：Pipeline支持复杂的现实世界连续交付要求，包括并行分叉/连接，循环和执行工作的能力。  
> 可扩展：Pipeline插件支持其DSL的自定义扩展 以及与其他插件集成的多个选项。


### 如何创建Jenkins Pipeline
- Pipeline脚本是由Groovy语言实现的，支持两种语法：声明式（Declarative）和脚本式（Scrpited Pipeline）  
- Pipeline有两种创建方法：一种是可以直接在Jenkins的Web UI界面种输入脚本；另一种就是直接从源代码中通过创建一个Jenkindfile脚本文件放入项目源码库中。

### 名词释义

- Stages  
脚本执行步骤，每一和可执行脚本只有一个Stages
- Stage  
脚本执行具体步骤，在Stages下可以有多个。  
- Step  
具体执行命令，比如：项目的拉取步骤，项目构建步骤，项目构建后项目等。
  
### 脚本编写

拉取代码

编译构建

远程部署

### 可配置在项目中自动拉去脚本执行







