这是一个1.16.5高版本起床战争插件
目前仅支持一端一图的模式（bungeecord）
基本实现了hypixel起床战争的功能

使用的依赖库：
1. 使用JScoreboards实现动态计分板
2. 使用hologram api创建全息显示文字
3. 使用citizens api创建商店npc
4. 使用beecp连接MySQL数据库

命令：
1. 使用 /setup <地图名（世界名）> 来开始配置一张地图，配置完成后，再次进入服务器，就会直接进入等待状态
2. 使用 /start 来强制开始游戏
