### 🍽 **レストラン予約 API ドキュメント**

## 1. **API 概要**
このプロジェクトは、レストランの予約を管理するシステムです。  
顧客はオンラインで予約を行うことができ、管理者は予約および決済を処理できます。

### **主要機能**
- 顧客の予約作成および管理
- 顧客の支払い
- 管理者の承認および予約ステータス変更
- 決済システム
- 管理者による手動予約

### **API バージョン**
- **バージョン:** `v1.0`
- **バージョン:** `v1.1` :  予約、ユーザーのIDをUUIDに変更し、セキュリティを強化
- **バージョン:** `v2.0` :  Googleログイン機能を実装

## 🛠️ 技術スタック

- **Backend**: Java, Spring Boot, Spring Security, JPA, QueryDsl
- **Database**: MySQL
- **DevOps**: Docker, Docker Compose
---
## 2. **認証方式**
- **認証方法:** `JWTトークン方式`
- **トークン発行エンドポイント:** `/authenticate`
- **Googleトークン発行エンドポイント:** `/api/auth/google`
- **有効時間:** `10時間`
- ----

## 3. **エンドポイント一覧**

### 3.1. **認証**

#### 1) **トークン発行**
- **HTTPメソッド:** `POST`
- **URL:** `/authenticate`
```json
{
  "username": "zini",
  "password": "1234"
}
```
---


### 3.2. **管理者機能**

#### **1) ユーザー検索**
- **HTTPメソッド:** `GET`
- **検索機能:** `名前`, `電話番号`
- **URL:** `/api/admin/users/search?username=test&name=parkjinhee&phone=08012345678&page=0&size=10`

#### **2) ユーザー削除**
- **HTTPメソッド:** `DELETE`
- **URL:** `/api/admin/users/{userUuId}`

#### **3) 管理者による手動予約**
- **HTTPメソッド:** `POST`
- **URL:** `/api/admin/reservations`

**リクエスト本文:**
```json
{
  "userId": "1",
  "restaurantTableId": "2",
  "reservationTime": "2025-02-24T22:55:56",
  "numPeople": "5"
}   
```
#### **4) 予約確認**
- **HTTPメソッド:** `GET`
- **検索機能:** `名前`, `電話番号`, `日付、時間`, `ステータス`
- **URL:** `/api/admin/reservations/search?name=jinhee&phone=080-1234-5678&startTime=2023-10-01T00:00:00&endTime=2023-10-31T23:59:59&status=COMPLETED&page=0&size=20&sortField=reservationTime`


#### **5) 支払いの照会**
- **HTTPメソッド:** `GET`
- **検索機能:** `支払い状態`, `支払いID`
- **URL:** `/api/admin/payments/search?reservationId=1&status=COMPLETED&page=0&size=20&sort=paymentTime,desc`
---


### 3.3. **会員機能**

#### **1) 会員登録**
- **HTTPメソッド:** `POST`
- **URL:** `/api/join`

**リクエスト本文:**
```json
{
  "username": "zini",
  "password": "1234", 
  "name": "jinhee",  
  "email": "qq@gmail.com",   
  "phone": "080-1111-1111"    
}
```
#### **2) 会員情報の取得**
- **HTTPメソッド:** `GET`
- **検索機能:**　`ユーザー情報`
- **URL:** `/api/users/me`

#### **3) 会員情報の更新**
- **HTTPメソッド:** `PUT`
- **URL:** `/api/users/update`

**リクエスト本文:**
```json
{
  "password": "12341234",
  "name": "jinhee",
  "email": "aa@gmail.com",
  "phone": "080-1111-1111"
}
```

---
### 3.4. **予約機能**

#### **1) 予約の登録**
- **HTTPメソッド:** `POST`
- **URL:** `/api/reservations`
- **注意点:** `予約はテーブルごとに1時間単位で可能です。`

**リクエスト本文:**
```json
{
  "restaurantTableId": "2",
  "reservationTime": "2025-02-24T22:55:56",
  "numPeople": "5"
}
```

#### **2) 予約の確認**
- **HTTPメソッド:** `GET`
- **検索機能:**　`ユーザーの予約情報`
- **URL:** `/api/reservations/me`

#### **3) 予約の修正**
- **HTTPメソッド:** `PUT`
- **URL:** `/api/reservations/{reservationUuid}`

**リクエスト本文:**
```json
{
  "restaurantTableId": "2",
  "reservationTime": "2025-02-24T22:55:56",
  "numPeople": "4"
}
```
----
### 3.5. **テーブル機能(管理者)**

#### **1) テーブルの追加**
- **HTTPメソッド:** `POST`
- **URL:** `/api/admin/restaurant_tables`

**リクエスト本文:**
```json
{
  "tableNumber": "1",
  "capacity": "5"
}
```
#### **2) テーブルの照会**
- **HTTPメソッド:** `GET`
- **検索機能:**　`テーブルの情報`

- **URL:** `/api/admin/restaurant_tables/{tableId}`

#### **3) テーブルの修正**
- **HTTPメソッド:** `PUT`
- **URL:** `/api/admin/restaurant_tables/{tableId}`

**リクエスト本文:**
```json
{
  "tableNumber": "2",
  "capacity": "5",
  "isAvailable": false
}
```
#### **4) テーブルの削除**
- **HTTPメソッド:** `DELETE`
- **URL:** `/api/admin/restaurant_tables/{tableId}`

---

### 3.6. **決済機能**

#### **1) 決済**
- **HTTPメソッド:** `POST`
- **URL:** `/api/payments`

**リクエストボディ:**
```json
{
  "reservationId": "1",
  "amount": "1000.00",
  "paymentMethod": "CASH"
}
```
#### **2) 決済情報の取得**
- **HTTPメソッド:** `GET`
- **検索機能:**　`ユーザーのお支払い情報`
- **URL:** `/api/payments/me`

#### **3) お支払いの修正**
- **HTTPメソッド:** `PUT`
- **URL:** `/api/payments/{paymentUuid}`

**リクエスト本文:**
```json
{
  "reservationId": "1",
  "amount": "1000.00",
  "paymentMethod": "CASH"
}
```

#### **3) 決済削除**
- **HTTPメソッド:** `DELETE`
- **URL:** `/api/payments/{paymentUuid}`

---



テスト
- **Postmanテスト:** `すべてのエンドポイントはPostmanを使用してテストをしました。`
- **サービス層テスト:** `ビジネスロジックはサービス層で単体テストを行います。各テストはJUnitを使用して作成され、外部の依存関係はMockingを使用して最小限に抑えます。`


-----------

-----------
### 韓国語説明


### 🍽 **식당 예약 API 문서**

## 1. **API 개요**
이 프로젝트는 레스토랑 예약을 관리하는 시스템입니다.  
고객은 온라인으로 예약을 진행할 수 있으며, 관리자는 예약 및 결제를 처리할 수 있습니다.

### **주요 기능**
- 고객 예약 생성 및 관리
- 고객 지불
- 관리자 승인 및 예약 상태 변경
- 결제 시스템 
- 관리자 수동 예약 


### **API 버전**
- **버전:** `v1.0`


## 🛠️ 기술 스택

- **Backend**: Java, Spring Boot , Spring Security, JPA, QueryDsl
- **Database**: MySQL
- **DevOps**: Docker, Docker Compose

---

## 2. **인증 방식**
- **인증 방법:** `JWT 토큰 방식`
- **토큰 발급 엔드포인트:** `/authenticate`
- **발급시간:** `10시간`

---

## 3. **엔드포인트 목록**

### 3.1. **인증**

#### 1) **토큰 발급**
- **HTTP 메서드:** `POST`
- **URL:** `/authenticate`
```json
{
  "username": "zini",
  "password": "1234"
}
```
---
### 3.2. **회원기능**

#### **1) 회원가입**
- **HTTP 메서드:** `POST`
- **URL:** `/api/join`

**요청 본문:**
```json
{
  "username": "zini",
  "password": "1234", 
  "name": "jinhee",  
  "email": "qq@gmail.com",   
  "phone": "080-1111-1111"    
}
```
#### **2) 회원 정보 조회**
- **HTTP 메서드:** `GET`
- **URL:** `/api/users/me`

#### **3) 회원 정보 수정**
- **HTTP 메서드:** `PUT`
- **URL:** `/api/users/update`

**요청 본문:**
```json
{
  "password": "12341234",
  "name": "jinhee",
  "email": "aa@gmail.com",
  "phone": "080-1111-1111"
}
```

---
### 3.3. **예약기능**
#### **1) 예약 등록**
- **HTTP 메서드:** `POST`
- **URL:** `/api/reservation`

**요청 본문:**
```json
{
  "restaurantTableId": "2",
  "reservationTime": "2025-02-24T22:55:56",
  "numPeople": "5"
}
```

#### **2) 예약 조회**
- **HTTP 메서드:** `GET`
- **URL:** `/api/reservation/me`

#### **3) 예약 수정**
- **HTTP 메서드:** `PUT`
- **URL:** `/api/reservation/update`

**요청 본문:**
```json
{
  "restaurantTableId": "2",
  "reservationTime": "2025-02-24T22:55:56",
  "numPeople": "4"
}
```
----
### 3.4. **테이블기능(관리자)**

#### **1) 테이블 추가**
- **HTTP 메서드:** `POST`
- **URL:** `/admin/restaurant_table`

**요청 본문:**
```json
{
  "tableNumber": "1",
  "capacity": "5",
  "isAvailable": true
}
```
#### **2) 테이블 조회**
- **HTTP 메서드:** `GET`
- **URL:** `/api/admin/restaurant_table/{tableId}`

#### **3) 테이블 수정**
- **HTTP 메서드:** `PUT`
- **URL:** `/api/admin/restaurant_table/{tableId}`

**요청 본문:**
```json
{
  "tableNumber": "2",
  "capacity": "5",
  "isAvailable": false
}
```
#### **4) 테이블 삭제**
- **HTTP 메서드:** `DELETE`
- **URL:** `/api/admin/restaurant_table/{tableId}`

---

### 3.5. **결제기능**

#### **1) 결제**
- **HTTP 메서드:** `POST`
- **URL:** `/api/payment`

**요청 본문:**
```json
{
  "reservationId": "1",
  "amount": "1000.00",
  "paymentMethod": "CASH",
  "status": "PENDING"
}
```
#### **2) 결제 조회**
- **HTTP 메서드:** `GET`
- **URL:** `/api/payment/me`


#### **4) 결제 취소 **
- **HTTP 메서드:** `DELETE`
- **URL:** `/api/payment/update`

---

### 3.6. **관리자기능**

#### **1) 유저 조회**
- **HTTP 메서드:** `GET`
- **조회 기능:** `이름`, `휴대폰`
- **URL:** `/user/search?username=test&name=parkjinhee&phone=08012345678&page=0&size=10`


#### **2) 유저 삭제**
- **HTTP 메서드:** `DELETE`
- **URL:** `/api/admin/user/{userId}`


#### **3) 관리자 수동 예약**
- **HTTP 메서드:** `POST`
- **URL:** `/api/payment`

**요청 본문:**
```json
{
  "userId": "1",
  "restaurantTableId": "2",
  "reservationTime": "2025-02-24T22:55:56",
  "numPeople": "5"
}   
```
#### **4) 예약 조회**
- **HTTP 메서드:** `GET`
- **조회 기능:** `이름`, `휴대폰`, `시간`
- **URL:** `/reservation/search?name=jinhee&phone=080-1234-5678&startTime=2023-10-01T00:00:00&endTime=2023-10-31T23:59:59&status=COMPLETED&page=0&size=20&sortField=reservationTime`


#### **5) 결제 조회**
- **HTTP 메서드:** `GET`
- **조회 기능:** `결제상태`, `결제ID`
- **URL:** `/payment/search?reservationId=1&status=COMPLETED&page=0&size=20&sort=paymentTime,desc`
---

테스트
- Postmen Test 
- servcie 계층 Test