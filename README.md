# code-review

#### 介绍
使用hook里面的钩子函数，在提交代码时候自动截取提交的的代码，调用deepseek/智普等大模型，使用大模型对你的代码进行审查。
将审查的结果存入git仓库，并且将文件子啊仓库中的地址通过微信公众号发送给代码提交人


#### 安装教程

1.  引入此依赖即可（依赖没有打包到中央仓库，需要自己下载源代码打包到本地仓库）


#### 使用说明

1.  引入此依赖（依赖没有打包到中央仓库，需要自己下载源代码打包到本地仓库）
2.  hook文件夹中配置post-commit的脚本,是需要将提供的post-commit放到.git/hook这个文件夹下面。
3.  在post-commit/pre-push的脚本中配置好大模型和git仓库地址(post-commit在提交的时候同步审查，pre-push在推送的时候异步审查，推荐使用pre-push)
    参数解释
    GIT_URL：存放审查文件的代码仓库
    GIT_USER：git账号
    GIT_PASSWORD= git的密码或者token
    WEIXIN_APPID=微信公众号的ID
    WEIXIN_SECRET=微信公众号的密钥
    WEIXIN_TOUSER=推送用户
    WEIXIN_TEMPLATE_ID=模版ID
    MODEL_KEY=大模型的key
    MODEL_URL=调用大模型的地址
    MODEL=大模型名称
5. 公众号模版消息配置 
   项目名称{{repo_name.DATA}} 分支名称{{branch_name.DATA}} 提交者{{commit_author.DATA}} 提交信息{{commit_message.DATA}}


