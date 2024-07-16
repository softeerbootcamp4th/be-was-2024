# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판
# DB
## SQL
### 회원 테이블 생성
```sql
create table member (
 id varchar(255) primary key,
 password varchar(255),
 name varchar(255),
 email varchar(255)
)
```
### 상품 테이블 생성
```sql
create table post (
 id int auto_increment primary key,
 title varchar(255),
 content varchar(255),
 author_name varchar(100)
)
```