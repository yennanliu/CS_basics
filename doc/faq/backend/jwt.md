# 以下是 JWT 的實作細節與流程：

---

## 1. JWT 的結構 (三部分)

一個 JWT 看起來像這樣：`xxxxx.yyyyy.zzzzz`

1. **Header (標頭)**：標註類型 (JWT) 與加密演算法 (如 HS256)。
2. **Payload (負載)**：存入使用者的非敏感資料（如 `userId`, `role`, `exp` 過期時間）。
3. **Signature (簽章)**：這是最關鍵的部分。伺服器用 **私鑰 (Secret Key)** 對前兩部分進行雜湊運算。
> $$Signature = HMACSHA256(base64(Header) + "." + base64(Payload), SecretKey)$$
> 
> 



---

## 2. 標準實作流程 (Workflow)

1. **登入**：使用者送出帳密。
2. **簽發 Token**：伺服器驗證成功後，生成一個 JWT 並回傳給前端。
3. **前端儲存**：通常存放在 `localStorage` (方便) 或 `HttpOnly Cookie` (較安全)。
4. **請求驗證**：前端在後續請求的 Header 加入：
`Authorization: Bearer <token>`
5. **伺服器校驗**：伺服器拿回 Token，用自己的 **Secret Key** 重新計算簽章。如果計算結果與 Token 上的簽章一致，且沒過期，就視為合法。

---

## 3. 安全性實作細節：雙 Token 機制

單一 JWT 有個致命傷：**發出去就收不回**。為了兼顧安全與體驗，實務上會使用 **Access Token** + **Refresh Token**：

* **Access Token (短效)**：有效期約 15 分鐘。用來請求 API。
* **Refresh Token (長效)**：有效期約 7~30 天。存放在資料庫或 Redis。
* 當 Access Token 過期時，前端拿 Refresh Token 去換一個新的 Access Token。
* **優點**：如果使用者想登出，伺服器只需從資料庫刪除 Refresh Token，該帳號在 15 分鐘後就會徹底失效。



---

## 4. 程式碼範例 (Node.js + jsonwebtoken)

這是實作 JWT 簽發與驗證的基本樣板：

```javascript
const jwt = require('jsonwebtoken');

const SECRET_KEY = 'your_super_safe_secret_key';

// 1. 簽發 Token (Login 時執行)
function generateToken(user) {
  return jwt.sign(
    { id: user.id, role: user.role }, // Payload
    SECRET_KEY,                       // 私鑰
    { expiresIn: '1h' }               // 設定 1 小時過期
  );
}

// 2. 驗證 Token (Middleware 中間件)
function verifyToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1]; // 取得 Bearer 後的字串

  if (!token) return res.sendStatus(401); // 未授權

  jwt.verify(token, SECRET_KEY, (err, decodedUser) => {
    if (err) return res.sendStatus(403); // Token 無效或過期
    req.user = decodedUser;              // 將解出的資料存入 req 供後續使用
    next();
  } );
}

```

---

## 5. 實作中的常見陷阱 (Best Practices)

1. **別存敏感資料**：JWT 雖然有簽章，但 Payload 只是 Base64 編碼，任何人都能解碼看到內容。**絕對不要存密碼！**
2. **演算法確認**：驗證時要明確指定演算法（如 `algorithms: ['HS256']`），防止惡意人士將 Header 改成 `none` 繞過驗證。
3. **防止 XSS**：如果將 JWT 存放在 `localStorage`，網站若有 XSS 漏洞，Token 會被腳本偷走。建議存放在 **HttpOnly Cookie**。
4. **檢查過期 (exp)**：務必檢查 Payload 中的 `exp` 欄位，確保 Token 尚未失效。

---

**你想進一步瞭解如何實作「黑名單 (Blacklist)」來強制註銷 JWT，還是想看看前端如何自動處理 Access Token 過期時的無感重新整理？**