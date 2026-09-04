# soul_trail 前端部署指南（阿里云 ECS 121.43.104.168）

> 把 `frontend/heart` 的 H5 构建产物部署到跟后端同一台 ECS 上，用 Nginx 做静态托管。

---

## 0. 前置条件（一次性确认）

| 项 | 检查命令（在 ECS 上跑） | 期望 |
|---|---|---|
| Nginx 已装 | `nginx -v` | 输出版本号 |
| 后端在跑 | `curl -s http://127.0.0.1:8080` 或 `ss -lntp \| grep :8080` | 后端存活 |
| 能 SSH 本机 | `ssh root@121.43.104.168 "echo ok"` | 输出 `ok` |
| 安全组开放 80 | 阿里云控制台 → ECS → 安全组 → 入方向 | 有 80/0.0.0.0/0 |
| 本地有 scp | Git Bash 里 `which scp` | 找到路径 |

如果 `scp` 不在（PowerShell 默认没有），方案：
- 用 **Git Bash**（推荐，你已经装了）
- 或装 **WinSCP** / **FileZilla**（图形化拖拽）
- 或装 **OpenSSH 客户端**（Win10+ 自带，开"设置 → 应用 → 可选功能 → OpenSSH 客户端"）

---

## 1. 首次部署（一次性）

### 1.1 在 ECS 上建前端目录

```bash
ssh root@121.43.104.168 "mkdir -p /opt/soul_trail/frontend"
```

### 1.2 上传 nginx 配置并测试

在**本机** `frontend/heart/` 目录下执行：

```bash
# 把 nginx.conf 推到 nginx 配置目录
scp deploy/nginx.conf root@121.43.104.168:/etc/nginx/conf.d/soul_trail.conf

# 远端测试配置语法
ssh root@121.43.104.168 "nginx -t"
```

`nginx -t` 应该输出 `syntax is ok ... test is successful`。有报错先看错（一般是路径写错或语法问题）。

### 1.3 首次 reload nginx（还没传前端文件，访问会 404，是正常的）

```bash
ssh root@121.43.104.168 "systemctl reload nginx"
```

---

## 2. 发布新版本（每次改完前端代码后）

### 2.1 本机构建

在 `frontend/heart/` 下：

```powershell
npm.cmd run build:h5
```

构建产物在 `frontend/heart/dist/`，是纯静态文件（HTML/JS/CSS/图片）。

### 2.2 上传 dist 到 ECS

```bash
# 清空旧前端目录（避免上次残留文件）
ssh root@121.43.104.168 "rm -rf /opt/soul_trail/frontend/*"

# 上传新 dist 内容
scp -r dist/* root@121.43.104.168:/opt/soul_trail/frontend/
```

> ⚠️ `dist/*` 在 Git Bash 里是"dist 下所有内容但不含 dist 本身"，正好覆盖到 `/opt/soul_trail/frontend/`。PowerShell 用 `dist\*` 同义。
> 如果是 FileZilla/WinSCP：本地源选 `dist/` 内全部，远程目标选 `/opt/soul_trail/frontend/`。

### 2.3 浏览器验证

开 `http://121.43.104.168`，看到 soul_trail 首页即成功。

---

## 3. 关键决策记录（为什么这么配）

### 3.1 没启用 `/api` 反代
当前 `frontend/heart/src/utils/request.js:16` 的 baseURL **写死**是 `http://121.43.104.168:8080`，所以前端在哪都能直连后端，不需要 nginx 反代。

如果你以后想：
- 把前端 baseURL 改成相对路径 `/api`
- 让前后端共用一个域名（更标准、避免 CORS）

→ 打开 `deploy/nginx.conf` 里 `/api/` 那段注释，同步改前端 baseURL，nginx reload 即可。

### 3.2 没上 HTTPS
现在只有 80 端口，浏览器会标"不安全"。部署到生产前**必须**补：
1. 申请证书（阿里云免费 DV SSL，或 Let's Encrypt）
2. nginx 加 `listen 443 ssl` + `ssl_certificate` 配置
3. 强制 80 → 301 跳 443
4. 后端 `WebConfig` 放行新域名（CORS，因为协议/域名变了）
5. `request.js` baseURL 改 `https://你的域名/api` 或保留 `https://...`

### 3.3 uni-app 路由模式
uni-app 默认 **hash 路由**（URL 带 `#/pages/...`），无需服务端 fallback。但 nginx 我还是加了 `try_files $uri $uri/ /index.html`，万一以后切 history 模式也不用改配置。

---

## 4. 回滚（出问题立刻回到上一个版本）

**没有 git 的轻量回滚**：

```bash
# 在 ECS 上把当前 dist 备份，再 scp 上一个版本覆盖
ssh root@121.43.104.168 "cp -r /opt/soul_trail/frontend /opt/soul_trail/frontend.bak.$(date +%Y%m%d)"
```

**正经做法**（推荐从下一次开始）：
- 在 `frontend/heart/` 用 git 维护，每次发版 `git tag v1.0.x`
- 回滚 = 切到上一个 tag → rebuild → scp
- 我可以帮你初始化这个 tag 流程，需要的话说一声

---

## 5. 常见问题排查

| 现象 | 可能原因 | 检查 |
|---|---|---|
| 浏览器访问 80 端口超时 | ECS 安全组没放行 80 | 阿里云控制台 |
| 访问 502 Bad Gateway | nginx 配了反代但后端没起 | `curl 127.0.0.1:8080` |
| 页面打开但 API 调用失败 | `request.js` 的 baseURL 跟实际后端地址不符 | 看 F12 Network |
| 静态资源 404 | `root` 路径写错，或 dist 没传完整 | `ls /opt/soul_trail/frontend` |
| `nginx -t` 报错 | 配置文件语法错 | 看具体报错行号 |

---

## 6. 还没做的（按需推进）

- [ ] 启用 `/api` 反代 + 改前端 baseURL（前后端同源）
- [ ] 上 HTTPS（生产必须）
- [ ] 接入 git tag 发版流程
- [ ] nginx 访问日志接入（按天切割，防撑爆磁盘）
- [ ] 阿里云 OSS 静态托管（比 ECS 起 nginx 更省事，不用维护服务器）

需要哪一项就说，我帮你推进。