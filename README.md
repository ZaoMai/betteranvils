# BetterAnvils

## 概述
BetterAnvils 是一个 Minecraft Spigot 插件，它重新实现了原版铁砧的功能，
使服务器管理员能够对铁砧的行为进行更加细致的配置，同时也为玩家提供了更加友好的使用体验。

## 功能特性
- 可为每一种附魔单独配置最大附魔等级（例如：时运 VIII、锋利 X 等）
- 可配置附魔与修理时的经验消耗倍率
- 可配置附魔 / 修理的最大经验消耗上限（不再出现 “~~过于昂贵！~~”）
- 可配置不同材料的修理消耗
- 更智能的附魔 / 修理 / 重命名逻辑，防止玩家在无意义的操作上浪费材料、工具或经验等级

# 以上纯原作者页面介绍直译
这个分支来源 [@lthoerner](https://github.com/lthoerner/betteranvils)

原作者因未知原因未对此插件进行更新适配

当前分支已更新适配至 Minecraft 1.21.1-R0.1-SNAPSHOT 并修复了部分不合理的地方（~~稀碎的史山~~）

## 如何使用？
1. 前往 [Github Releas](https://github.com/ZaoMai/betteranvils/releases) 页面进行下载
2. 将插件丢进 Minecraft Server Plugins 文件夹
3. 重启服务器，插件自动生成配置文件
4. 修改 *./plugins/BetterAnvils/config.yml* 以进行个性化调整，再次重启服务器
5. 验收，喝茶

## 注意事项
- 推荐同时搭配 [BoundlessForging](https://github.com/BlurOne-GIT/Boundless-Forging) 以解锁完整体验
- 由于 Minecraft 本地策略，玩家游玩时依然会提示 [过于昂贵] ，可忽略
- 原作者留下的锅，我不予处理，比如 enchant-cost-multiplier 配置项，这里更合理的解决方案应当是以乘数倍率计算
- 配置文件已全局中文注释
- 不接受任何反馈谢谢
