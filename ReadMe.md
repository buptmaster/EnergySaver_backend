## 计划

### 控制器状态
* allow：控制器允许设备自由开关
* forbidden：控制器禁用，同时设备被切断电源
* offline：控制器离线，无法控制该设备，异常状态

### 控制器ID
* 不能以Topic为id了，万一要改呢，是吧，就云端生成一个独特id让控制器订阅就是了
* 控制器id不能直接用mqtt的topic，需要独立出一个id来，一个控制器可以用多个topic
* 以下说的都是屁话，打算采用mqtt的形式，topic可以作为id，就是/test/123这样
* 使用64无符号长整型id
* 最后4位是控制器的个数，默认一组控制器最大个数为16个
* 采用分组的形式，可以自由添加组别
* 控制器id还有一个特性，就比如说全0是控制所有控制器的，这样能一组控制器的数量就要-1了
* 其实可以不一定用数字id的，也可以考虑字符组装的形式，更为简单

### 动态配置计划
* 在数据库建立一张表，3个字段，key、value和category
* key是属性名，value是属性值，category是这些键值对归属的某一个大类，可空
* 这个可以用来进行分组计划
* 还可以定义标记和标记对应的意义（比如说标记这个教室是不是自习教室什么的）

### 策略计划
* 数字的大小为优先级，一个策略可以覆盖另外一个策略
* 基本策略（日常策略）：***何日到何日，何时到何时开启***，优先级为1
* 临时策略：依靠优先级字段，优先级比基本策略肯定要高，优先级可以为999
* 一旦策略过期了，就定时任务把他删除掉
* 还一个字段是这个策略所控制的topic，我靠这样简单多了
* 策略的名字