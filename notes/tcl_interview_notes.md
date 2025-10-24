# Android 面试题要点整理

# Android 面试题详解（完整 Markdown 版）

> 本文档为面试备答用——每一题包含：核心要点、面试可说话术（简洁版 + 深入追问扩展）、常见实现/代码片段与注意事项。你可以直接背要点，也可以把示例代码在白板/IDE 中写出来演示。

---

## 目录
1. 原生 Launcher 与 Settings 架构
2. 性能优化（通用与场景化）
3. 电信号 / 消息发送逻辑（从应用层到传输层）
4. 冷启动 / 热启动 优化与 VSync（垂直同步）
5. 在特定应用场景下减轻系统内存负担
6. ViewStub 的具体膨胀与替代方案
7. WorkManager 与 JobScheduler 的区别与选用策略
8. 延迟加载布局的模式与实战
9. 自定义线程池的完整实现与策略
10. Kotlin 协程：使用、取消、调试与常见坑
11. `by lazy` 的使用细节与陷阱
12. Kotlin Flow（冷流、热流、常见操作）
13. 常见 Jetpack 组件及落地案例
14. Kotlin 内联函数（inline）与性能影响
15. Android 性能测量工具详解（Perfetto/Profiler 等）
16. 线程池种类、核心数策略与核心线程回收
17. Activity 启动流程（从调用到渲染）与 AMS/PMS 角色

---

## 1. 原生 Launcher 与 Settings 架构

### 核心要点
- **Launcher**：以 "模型（Model）+ 缓存 + 异步渲染" 为核心，UI 层尽可能轻量。主要管理桌面布局、图标、Widget、快捷方式、应用列表和热区操作。
- **Settings**：是配置中心，强依赖系统 Provider（SettingsProvider）、权限信息及大量同步/异步读取操作。通常按页面模块化，减少主线程阻塞。

### 模块划分（Launcher）
- `LauncherActivity`：入口与生命周期控制。
- `Workspace`：负责桌面网格布局与拖拽/重排逻辑。
- `AllApps` / `AppList`：应用拉取、排序、搜索。
- `LauncherModel`：负责数据加载、预处理、同步到 UI（通常有一个后台线程/handler）。
- `LauncherProvider`：ContentProvider，用于持久化保存桌面布局/快捷方式等。
- `IconCache`：图标缓存（内存 + 磁盘），减少图标解码与 I/O。
- `WidgetHost`：管理 AppWidget 的生命周期/更新。
- `PackageMonitor`：监听安装/卸载/更新，触发刷新。

**性能点**：
- 延迟解析与加载（首次渲染只渲染当前页）；
- 图标解码异步化并使用 LRU 缓存；
- 批量更新合并：合并多次包变更事件避免重复刷新；
- 使用 RecyclerView / Pager 的 view 复用能力；
- 减少布局层级（避免过多嵌套、使用 ConstraintLayout）。

### 模块划分（Settings）
- `SettingsActivity/PreferenceFragment`：页面展示。
- `Preference` 框架：用来快速生成设置界面，支持 `PreferenceScreen` 分页。
- `SettingsProvider` / `SystemSettings`：系统设置的存储接口（ContentProvider 层）。
- `后台 Service`：处理需要高级权限的设置或长时任务（例如网络认证、固件升级）。

**性能点**：
- 不在 UI 线程同步读取大量设置值，使用 Loader/Async 或 Flow 订阅变更；
- 将不常用设置的界面延迟加载或按需初始化；
- 使用 `ContentObserver`/LiveData 或 StateFlow 对设置变更做增量更新。

### 面试可说话术（示例）
> “Launcher 的核心是把 I/O 与解析挪到后台，做最小化的主线程渲染并用缓存减少重复工作；Settings 则更像一个配置查询服务，按需加载和异步订阅变化是关键。”

---

## 2. 性能优化（通用与场景化）

### 通用原则
1. **避免在主线程做 I/O/长计算**（16ms 帧预算）。
2. **按需加载**（lazy load）、拆分初始化（必须 / 次要）。
3. **减少内存分配与短生命周期对象**，用对象池或复用策略。
4. **减少绘制次数**（避免无意义的 `invalidate()`、复杂布局测量）。
5. **使用合适的数据结构**（例如使用 `SparseArray` 替代 `HashMap` 在大量 int->object 场景）。
6. **Profile 驱动优化**：先测再改，避免盲目优化。

### 具体手段与示例
- **布局优化**：减少嵌套，使用 ConstraintLayout 或合并布局，使用 `include` 与 `merge` 优化视图树。
- **图片处理**：使用正确大小的 Bitmap、BitmapPool、Glide/Picasso 的占位与缩放策略；使用 WebP/HEIF/AVIF 减少内存与磁盘占用。
- **IO 优化**：把 JSON/大文件解析放到后台线程并考虑流式解析（例如 `JsonReader`）以减少峰值内存。
- **GC 优化**：避免频繁创建短生命周期对象；使用 `StringBuilder` 代替字符串拼接循环；复用 `RecyclerView.ViewHolder` 中可复用对象。
- **布局/绘制优化**：开启 GPU Overdraw 调试（调试/开发），合并绘制层（减少透明度、多层次独立层）。

### 启动优化（Cold / Warm）
- **Cold**：把第三方 SDK 初始化延后；把 `ContentProvider` 的 heavy 工作移出 `onCreate()`；使用 `Splash` 屏做最小 UI 以掩盖初始化。
- **Warm**：避免重复创建资源，使用缓存与 `onSaveInstanceState` 恢复 UI 状态。

### 面试可说话术（示例）
> “优化必须基于数据，先用 Profiler 找到热点（CPU/Memory/Network），然后针对性做出优化，比如重写热点处逻辑或降低内存分配。”

---

## 3. 电信号 / 消息发送逻辑（分层视角）

> 注：题目表述比较广，可以根据你擅长的层级（应用层 IPC、网络层、或设备驱动层）选择展开。

### 应用层（网络请求）
- **原则**：可靠性（重试/幂等）、顺序性（必要时保证顺序）、带宽/速率控制、限流与退避策略。
- **实现**：请求队列 + 重试策略（指数退避）、请求去重（按业务 id 幂等）、限速（令牌桶）、批量发送（合并小包）。

### IPC / Binder
- **注意**：Binder 是跨进程通信的主要方式，事务大小不能过大（BinderBufferSize 限制），避免阻塞 Binder 线程池。
- 在 Binder 线程上不要做长耗时计算，应把工作派到工作线程。

### 驱动 / 物理层
- **要点**：帧格式、分包、CRC、确认/重发、拥塞控制、QoS、优先级。通常有硬件中断、中断去抖与 DMA 控制来提升效率。

### 面试可说话术（示例）
> “实现时先从需求（可靠 vs 低延迟）判断是否需要确认机制，使用队列 + 幂等设计能明显降低重复与错误率；Binder 层面注意不要在 Binder 线程做耗时工作以免耗尽线程池。”

---

## 4. 冷启动 / 热启动 优化与 VSync

### 冷启动（Cold Start）详解
- **主要耗时点**：Zygote fork、类加载（Dex）、`Application.onCreate()`、`ContentProvider.onCreate()`、首次布局/资源加载。
- **优化策略**：
  - 把非关键组件延迟初始化（lazy init）；
  - 用 `SplashActivity` 把真正重的工作放后台线程并显示即时 UI；
  - 启用 AOT/JIT 优化（Android App Bundles / baseline profiles）；
  - 避免在 `ContentProvider.onCreate()` 做 heavy work（很多 SDK 把初始化放这里会拖慢 cold start）。

### 热启动（Warm Start）详解
- **场景**：进程已存在但 Activity 需要创建；或者从后台恢复。
- **优化策略**：
  - 避免重复初始化单例或静态资源；
  - 使用 ViewModel + savedStateHandle 保存状态，减少 IO；
  - 使用 `setRetainInstance`（Fragment）等技术（只在合适场景）。

### VSync 与帧渲染
- **VSync（垂直同步）**：系统发送帧信号（默认 60Hz -> 16.67ms），UI 绘制须在该周期内完成，超过会产生 jank（丢帧）。
- **避免 jank 的策略**：
  - 把耗时任务分帧执行（使用 `Choreographer.postFrameCallback()`）；
  - 使用 `RenderThread` / `HardwareLayer` 把复杂绘制交给 GPU；
  - 使用 `AsyncLayoutInflater` 在后台解析布局（注意：inflate 仍需在主线程 attach）。

### 面试可说话术（示例）
> “Cold start 优化从减少 Application 工作量和延迟 SDK 初始化入手；VSync 要求每帧 <16ms，超过就要把耗时任务拆分或移到工作线程。”

---

## 5. 场景化示例：如何减轻系统内存负担

### 场景 A：Launcher（桌面）在低内存时
- 只保留当前页与相邻页的图标/Widget，非当前页面持久化到磁盘（磁盘缓存），必要时用占位图。释放不活跃 Widget 的 RemoteViews。使用 `onTrimMemory()` 回收缓存。

### 场景 B：多用户 / 多窗口
- 使用按用户/窗口分片缓存；切换用户时把上一用户的大资源写磁盘并释放内存。

### 场景 C：图片密集型应用
- 使用 downsample，按屏幕分辨率生成缩略图。使用 `BitmapFactory.Options.inBitmap`（API 支持）复用内存。使用 Glide/Coil 的内存与磁盘缓存策略。

---

## 6. ViewStub 的具体膨胀（inflate）与替代

### 行为回顾
- `ViewStub` 在 layout 中是一个非常轻量的占位 View。第一次调用 `inflate()` 或把 `visibility` 设为 `VISIBLE` 时，会把 `ViewStub` 替换为指定 layout，并返回该新 View。之后 `ViewStub` 被移除。

### 优势
- 节省初始布局解析/测量开销，适用于偶发展示的复杂布局（错误页、登录提示等）。

### 限制与替代方案
- `ViewStub` 仅能 inflate 一次，若需要多次切换显示，考虑：
  - 手动 `removeView()`/`addView()`；
  - 使用 `RecyclerView` 或 `ViewFlipper` 管理不同状态视图；
  - 使用动态 Fragment（按需 attach/detach）。

### 示例
```kotlin
val inflated = viewStub.inflate()
// inflated 为替换后的根 view
```

---

## 7. WorkManager vs JobScheduler（深入）

| 维度 | WorkManager | JobScheduler |
|------|-------------|--------------|
| 兼容性 | 支持向下兼容（AlarmManager/JobScheduler 等回退实现）| 仅 API21+
| 任务持久化 | ✔（设备重启后依旧可恢复）| ✔（系统层）
| 链式任务 | ✔（支持 chaining） | ✖
| 易用性 | 高（API 封装） | 低（需要处理更多边界）

### 选用建议
- **使用 WorkManager**：需要可靠执行（例如上传/同步），希望兼容大量设备并需要链式任务或可观察性。
- **使用 JobScheduler**：只针对 Android 5.0+ 且需要更接近系统的约束控制时可选。

---

## 8. 延迟加载布局（模式与实践）

### 模式
- `ViewStub`：单次延迟inflate，最轻量。
- 懒 Fragment（lazy fragment）：fragment 在第一次可见时加载数据/视图。
- `RecyclerView` 分页（分页加载数据并展示）：结合 Paging3 实现。
- 图片懒加载：列表滚动时加载可见项的图片，超出范围取消请求（防止加载无用图像）。

### 实战要点
- 初始 UI 只展示最小必要元素（首屏）；其他初始化放到 `idle` 时或 `onPostResume` 后执行；
- 使用 `ViewTreeObserver` 或 `OnPreDrawListener` 优化首次渲染的测量逻辑；
- 使用 `AsyncLayoutInflater` 在后台解析复杂布局 XML，再在主线程 attach（减少主线程 XML 解析开销）。

---

## 9. 自定义线程池：完整实现与注意事项

### 参考实现
```kotlin
val pool = ThreadPoolExecutor(
    corePoolSize = 4,
    maximumPoolSize = 8,
    keepAliveTime = 60L,
    unit = TimeUnit.SECONDS,
    workQueue = LinkedBlockingQueue<Runnable>(100),
    threadFactory = ThreadFactory { r -> Thread(r, "my-pool-\${counter.incrementAndGet()}") },
    handler = ThreadPoolExecutor.CallerRunsPolicy()
)
```

### 策略说明
- **核心线程数**：对 CPU 密集型任务取 `CPU_COUNT` 或 `CPU_COUNT + 1`；IO 密集型可更高。
- **队列类型**：无界队列会使 `maximumPoolSize` 无效；有界队列需配置拒绝策略。
- **拒绝策略**：`AbortPolicy`（抛异常）、`CallerRunsPolicy`（让调用者执行任务）、`DiscardOldest`、`DiscardPolicy`。
- **线程工厂**：定制命名、设置 `uncaughtExceptionHandler`、设置为 daemon（如需）等。

### 常见坑
- 忽视队列导致 OOM；
- 在 UI 线程创建大量线程或阻塞 UI；
- 错误使用 `ScheduledThreadPool` 导致任务积压。

---

## 10. Kotlin 协程：使用、取消、调试与常见坑

### 基本概念
- `CoroutineScope`：管理协程生命周期（如 `lifecycleScope`, `viewModelScope`）。
- `Job`：协程的可取消句柄。
- `Dispatcher`：决定协程执行的线程（Main/IO/Default/Unconfined）。
- `suspend`：标记挂起函数，挂起不会占用线程。

### 代码示例
```kotlin
// 在 Activity 中
lifecycleScope.launch {
  try {
    val result = withContext(Dispatchers.IO) { heavyIo() }
    updateUi(result)
  } catch (e: CancellationException) {
    // 协程取消
  }
}
```

### 取消细节
- **协作取消**：协程必须在挂起点检查取消（例如 `delay()`、`withContext` 等）。
- 在长计算中要显式检查 `coroutineContext.isActive` 或调用 `yield()`。
- `job.cancel()` 会抛出 `CancellationException` 到协程的挂起点。
- 父取消会传播给子（层级传播），通过 `SupervisorJob` 可隔离子任务失败影响。

### 串联中的取消
- 在 `coroutineScope { launch { ... } }` 中，若父 scope 取消，子协程会被取消。取消并不是瞬时的（需在挂起点生效），若协程在非挂起的长循环中，会继续运行直到遇到可协作位置或显式检查 `isActive`。

### 调试技巧
- 使用 `DebugProbes`、`CoroutineName`、`uncaughtExceptionHandler` 来定位问题；
- Android Studio 的协程调试插件可在堆栈中展示挂起点。

### 面试可说话术（示例）
> “协程是轻量级线程，挂起点并不占用线程；取消是协作式的，需要挂起点或显式检查 `isActive` 才会生效，父作用域取消会传播到子协程。”

---

## 11. `by lazy` 使用注意事项

- 默认线程安全（`SYNCHRONIZED`），若性能敏感可用 `NONE`（不安全）或 `PUBLICATION`（允许多个初始化但取第一个返回）。
- 若 lazy 的初始化涉及抛异常，默认行为是再次访问会重新尝试初始化还是抛出上次异常取决于实现（一般会缓存异常并再次抛出）。
- 不要在 lazy 中捕获/持有 Activity/Context 的强引用以免内存泄漏。

---

## 12. Flow（Kotlin）

### 概念回顾
- **冷流（Cold Flow）**：只有 `collect` 时才会启动执行。
- **热流（StateFlow/SharedFlow）**：会持续存在并广播值。

### 常见操作符
- `map`, `filter`, `flatMapConcat`, `flatMapMerge`, `buffer`, `debounce`, `distinctUntilChanged`, `collectLatest`。

### 使用场景
- 搜索输入防抖：`debounce` + `flatMapLatest`。
- 数据库监听：Room 支持返回 `Flow<T>`，可作实时更新流。

### 示例
```kotlin
searchQuery
  .debounce(300)
  .filter { it.isNotEmpty() }
  .flatMapLatest { query -> repository.search(query) }
  .flowOn(Dispatchers.IO)
  .collect { results -> updateUi(results) }
```

---

## 13. Jetpack 组件（详细落地）

- **ViewModel**：保存 UI 数据，避免 Activity 重建丢失；与 `SavedStateHandle` 配合可恢复跨进程杀死的状态。
- **LiveData / StateFlow**：生命周期感知的数据流。
- **Room**：本地数据库，支持返回 `Flow`/`LiveData`。
- **WorkManager**：可靠后台任务。
- **Paging 3**：分页加载数据并支持缓存。
- **Navigation**：界面跳转与参数传递。
- **Hilt**：依赖注入，简化组件创建与测试。

### 面试可说话术
> “讲一个你用这些组件解决实际问题的例子：比如用 Paging + Room + RemoteMediator 实现离线优先的分页列表，用 ViewModel+StateFlow 解耦 UI 与数据层。”

---

## 14. Kotlin inline 函数

- `inline` 把函数体复制到调用处，减少高阶函数产生的额外 `Function` 对象与调用开销。
- `noinline`：防止某个 lambda 被内联（在 inline 函数参数中）。
- `crossinline`：禁止被内联 lambda 做非局部返回。
- 注意 binary size 的增长以及可能影响调试（堆栈变长）。

---

## 15. Android 性能工具（实战指南）

### 工具链
- **Android Studio Profiler**：CPU / Memory / Network 的快速定位。
- **Perfetto**：系统级 tracing（推荐用于跨进程、长时间 trace 分析）。
- **Systrace / atrace**：内核/系统追踪（legacy）。
- **Heap Analyzer / MAT**：分析内存泄漏与堆快照。
- **adb shell dumpsys meminfo / top**：进程内存与 CPU 快照。

### 实战示例：找 jank
1. 使用 Android Studio CPU profiler 或 `adb shell screenrecord --verbose` + perfetto Trace 收集问题期间的 trace。
2. 定位主线程中耗时方法（Long running on main thread）；
3. 检查连续布局/重绘（layout/draw）与重复的 measure；
4. 优化：把长任务拆分、减少布局深度、缓存绘制结果（`setHasTransientState()`、`hardwareLayer`）。

---

## 16. 线程池种类、核心数策略与核心线程回收

### 常见实现
- `Executors.newFixedThreadPool(n)`、`newCachedThreadPool()`、`newSingleThreadExecutor()`、`newScheduledThreadPool()`。
- 实际使用建议自定义 `ThreadPoolExecutor` 以便调整策略。

### 核心数策略
- CPU 密集型：`N = CPU_COUNT` 或 `CPU_COUNT + 1`；
- IO 密集型：`N = CPU_COUNT * (1 + waitTime/computeTime)`（经验公式）。

### 核心线程回收
- 默认情况下 core threads 不会回收，除非调用 `allowCoreThreadTimeOut(true)`。

---

## 17. Activity 启动流程与 AMS/PMS 的角色（逐步）

1. **上层调用**：应用或 Launcher 调用 `Context.startActivity()` -> 走到 `ActivityManager` 代理（Binder IPC）。
2. **AMS（ActivityManagerService）**：在系统进程解析 Intent，选择目标组件、决定是否要创建新进程或复用现有进程，管理任务栈（task/affinity）。
3. **PMS（PackageManagerService）**：提供包信息（包解析、Activity 元数据、权限），为 AMS 的 Intent resolution 提供数据。
4. **进程创建/复用**：如需新进程，AMS 会通过 Zygote fork 出新进程并启动 `ActivityThread`。
5. **ActivityThread 启动**：负责在目标进程中创建 `Application`、调用 `Activity.attach()`、`performLaunchActivity()`，进而触发 `onCreate/onStart/onResume`。

### 面试补充点
- 可说明 `AMS` 如何做跨进程同步与优先级，`PMS` 如何缓存解析结果以优化启动速度。

---

## 附：常见面试追问与简短回答库
- **Q：如何判断应用是否 CPU 还是 IO 密集？** 通过 Profile 看线程利用率与阻塞时间，IO 密集通常 CPU 使用低但大量等待 I/O。
- **Q：协程取消失败怎么办？** 在长计算里插入 `ensureActive()`/`yield()` 或检查 `isActive`，或把计算拆成可挂起的块。
- **Q：如何减少 cold start?** 避免在 `Application.onCreate()` 中初始化非必须 SDK，使用延迟 init 和 baseline profile。

---

> 如果你希望我把这份详细文档导成一个 `.md` 文件并给出下载链接，或者把它拆成面试 PPT（每页 1–2 个点）/一页速记卡，请告诉我，我会直接生成并提供下载链接。



## 1. synchronized 锁定对象和锁定代码块的区别

**锁定对象：**
```java
synchronized(obj) {
    // 代码块
}
```
锁定的是 `obj` 对象，只有持有这个对象锁的线程才能执行代码块。  
不同对象的锁互不影响。

**锁定方法（锁定 this 或类对象）：**
```java
synchronized void method() { ... }
```
相当于锁定 `this` 对象（实例方法）或类的 Class 对象（静态方法）。

**区别总结：**
| 类型 | 锁定范围 | 适用场景 |
|------|-----------|-----------|
| synchronized(obj) | 局部锁，控制细粒度 | 仅需同步部分逻辑 |
| synchronized 方法 | 整个方法锁定 | 简单同步逻辑 |
| static synchronized 方法 | 锁住类对象 | 控制类级资源 |

---

## 2. 应用掉帧（卡顿）排查思路

Android 屏幕每 16ms（60Hz）刷新一次，主线程需在 16ms 内完成绘制。超过此时间会**丢帧**。

### 常见原因：
1. **主线程执行耗时操作**（I/O、复杂计算、JSON解析等）
2. **过度绘制**（Overdraw）
3. **布局层级太深或频繁 requestLayout()**
4. **频繁 GC**
5. **动画或列表滑动中触发昂贵操作**

### 排查工具：
- **Profile GPU Rendering**：查看每帧耗时
- **Systrace / Perfetto**：系统级性能分析
- **Android Profiler**：分析 CPU、内存、网络性能
- **Layout Inspector**：查看布局层级、Overdraw

### 优化方向：
- 在子线程执行耗时操作（I/O、数据库、网络）
- 使用 `ViewStub`、`include`、`merge` 优化布局
- 缓存 Bitmap 与 ViewHolder
- 减少 UI 层反复创建销毁
- 合理使用 DiffUtil 优化 RecyclerView 刷新

---

## 3. 动态模块引用与打包剔除

在大型应用中可使用 **Dynamic Feature Module (DFM)** 或通过 Gradle 控制依赖。

### 开发阶段：
在 `settings.gradle` 中 include 模块：
```gradle
include ':feature_x'
```
主模块依赖：
```gradle
implementation project(':feature_x')
```

### 上线阶段（需要剔除）：
可用以下方式：
- 使用 `buildVariant` 配置，只在 debug 引用：
```gradle
debugImplementation project(':feature_x')
releaseImplementation files()
```
- 或在发布脚本中自动剔除模块依赖。

---

## 4. 系统设置项目中常见的设计模式

| 模式 | 应用场景 | 示例 |
|------|----------|------|
| 单例模式 | 全局唯一对象 | 系统配置管理器 |
| 工厂模式 | 统一创建对象 | UI 控件创建器 |
| 观察者模式 | 数据变化通知 | LiveData / Flow |
| 代理模式 | 控制访问逻辑 | IPC、AIDL 服务封装 |
| 命令模式 | 封装操作请求 | 设置项的行为抽象 |
| MVVM 架构 | 解耦 UI 与逻辑 | ViewModel + DataBinding |

---

## 5. Kotlin object 线程安全性

`object` 声明的对象在首次访问时**线程安全地懒加载初始化**，使用的是 **JVM 类加载机制的线程安全特性**。  
因此：
```kotlin
object ConfigManager {
    val name = "SystemSetting"
}
```
是线程安全的，且全局唯一。

---

## 6. 静态类与静态对象的线程安全

| 类型 | 初始化时机 | 线程安全性 | 备注 |
|------|------------|-------------|------|
| Java 静态类 | 类加载时初始化 | ✅ 线程安全 | 类加载器保证原子性 |
| Kotlin object | 首次访问时初始化 | ✅ 线程安全 | 懒加载单例 |
| Lazy by lazy | 可配置线程安全性 | 取决于参数 | 默认 `SYNCHRONIZED` 模式 |

---

## 7. 面试时长与通过率关系

| 面试时长 | 含义 | 通过率趋势 |
|-----------|------|--------------|
| 10-20 分钟 | 初筛或快速淘汰 | 较低 |
| 30-50 分钟 | 正常技术面 | 中等偏上 |
| >60 分钟 | 深入讨论或 HR 面 | 视表现而定 |

时长并非决定因素，**关键是沟通逻辑清晰、思路完整**。  
如果面试官愿意深入聊架构和实现细节，说明你已经进入“可考虑录用”阶段。

---

## 8. OkHttp 日志打印最佳实践

建议使用 **LoggingInterceptor**：

```kotlin
val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()
```

或者自定义拦截器：

```kotlin
class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("HTTP", "→ ${request.method} ${request.url}")
        val response = chain.proceed(request)
        Log.d("HTTP", "← ${response.code} ${response.message}")
        return response
    }
}
```

**建议：**
- 不在生产环境打印 BODY 内容（避免泄漏敏感信息）
- 使用拦截器链打印请求与响应时间
- 日志写入本地文件方便分析

---

## 9. Launcher 启动优化

启动优化目标：**冷启动时间 < 1s**。

### 关键优化点：
1. **减少 Application 初始化开销**
  - 延迟 SDK 初始化（如在子线程或空闲时加载）
  - 使用 `AppStartup` 或自定义初始化框架
2. **优化布局加载**
  - 首屏布局使用 `ConstraintLayout`
  - 减少层级、提前渲染关键组件
3. **异步加载数据**
  - 使用 `SplashScreen API` 显示启动动画掩盖加载
4. **使用 Systrace/Perfetto 分析启动阶段性能瓶颈**

---

✅ **总结：**  
这份文档覆盖了系统设置类项目常见的面试问题与实践方案，包括锁机制、性能优化、动态模块化、Kotlin 单例线程安全、设计模式与启动优化等主题。面试中若能结合项目经验具体说明，会大幅提升说服力。

