# 项目目标
1. 不可以只是 webapp, 需要使用本地 fs, 可以读写文件, 使用 cli
2. 不用任何 agent 框架, 减少复杂度, 容易扩展
3. local first, 数据尽量都在本地, 支持离线使用
4. 动态, 边运行边修改

# 本地开发
## clojure 启动
jack in clojure-stuff 目录的 deps.edn.
勾选 dev alias.
启动 com.linzihao.web.dev/-main
在 wsl 下 electric 的登录回调可能会失败, 复制浏览器上的回调地址, 在 wsl 的 terminal 手动 curl 地址可以解决

# TODO
- 通过 @ 添加 context
- 自动提供 context?
- UI?
- 整理图片?
- 视频翻译?