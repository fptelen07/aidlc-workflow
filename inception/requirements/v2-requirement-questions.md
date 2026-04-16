# V2 需求澄清问题 — 邮件通知功能

请回答以下问题，在 [Answer]: 后填写字母选项。

## Question 1
邮件发送方式？

A) AWS SES（Amazon Simple Email Service）
B) SMTP（自建或第三方如 Gmail、SendGrid）
C) Other (please describe after [Answer]: tag below)

[Answer]: 

## Question 2
用户邮箱从哪里获取？目前 User 表没有 email 字段。

A) 在 User 表新增 email 字段，注册时必填
B) 在 User 表新增 email 字段，注册后在个人设置中填写
C) 使用 username 作为邮箱（假设 username 就是邮箱格式）
D) Other (please describe after [Answer]: tag below)

[Answer]: 

## Question 3
兑换码格式？

A) 随机字符串（如 AXBK-7F9M-Q2WE）
B) UUID（如 550e8400-e29b-41d4-a716-446655440000）
C) 数字序列（如 20260415001）
D) Other (please describe after [Answer]: tag below)

[Answer]: 

## Question 4
兑换码在什么时候生成？

A) 管理员确认订单时生成（confirm 阶段）
B) 员工下单时就生成（create 阶段）
C) Other (please describe after [Answer]: tag below)

[Answer]: 

## Question 5
邮件模板语言？

A) 纯文本邮件
B) HTML 邮件（带样式）
C) Other (please describe after [Answer]: tag below)

[Answer]: 

## Question 6
邮件发送失败时的处理策略？

A) 静默失败（记录日志，不影响订单流程）
B) 重试 3 次后失败（不影响订单状态）
C) 邮件发送失败则订单确认也失败（强一致）
D) Other (please describe after [Answer]: tag below)

[Answer]: 
