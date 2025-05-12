# Facebook Clone

Dự án mô phỏng mạng xã hội Facebook sử dụng Java, Spring Boot và Thymeleaf. Ứng dụng cho phép người dùng đăng ký, đăng nhập, đăng bài viết, bình luận, kết bạn, nhắn tin,...

## 🚀 Tính năng chính

- Đăng ký / Đăng nhập người dùng
- Đăng bài viết và hiển thị bài viết
- Bình luận và thả cảm xúc
- Kết bạn và danh sách bạn bè
- Nhắn tin giữa các người dùng
- Tường cá nhân của từng người dùng

## 🛠 Công nghệ sử dụng

- **Back-end**: Spring Boot, Spring Security, Spring Data JPA
- **Front-end**: Thymeleaf, Bootstrap, JavaScript, AJAX
- **Database**: MySQL
- **Build tool**: Maven

## ⚙️ Yêu cầu hệ thống

- JDK 17 trở lên
- Maven 3.6+
- MySQL 8+
- IDE: Eclipse / IntelliJ IDEA / VSCode
- Trình duyệt: Chrome / Firefox

## 📥 Hướng dẫn cài đặt

### Bước 1: Clone dự án

```bash
git clone https://github.com/ten-cua-ban/facebook-clone.git
cd facebook-clone
```

### Bước 2: Tạo cơ sở dữ liệu

Mở file db_clone trong MySQL và chạy


### Bước 3: Cấu hình database trong `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/facebook_clone
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Bước 4: Chạy ứng dụng

- Mở file `FacebookCloneApplication.java`
- Nhấn **Run** hoặc chạy lệnh:

```bash
./mvnw spring-boot:run
```

Truy cập trình duyệt tại: `http://localhost:8080`

## 📁 Cấu trúc thư mục

```text
facebook-clone/
├── src/main/java/com/facebookclone/
│   ├── controller/    --> Xử lý request
│   ├── service/       --> Logic nghiệp vụ
│   ├── repository/    --> Giao tiếp với DB
│   ├── model/         --> Entity
│   ├── config/        --> Cấu hình bảo mật, CORS
│   ├── dto/           --> Các lớp truyền dữ liệu
│   ├── interceptor/   --> Middleware xử lý trước controller
├── src/main/resources/
│   ├── templates/     --> Giao diện Thymeleaf
│   ├── static/        --> JS, CSS, hình ảnh
│   └── application.properties
├── pom.xml            --> File cấu hình Maven
```

## 🔍 Lưu ý

- Thư mục `IMG` dùng để lưu ảnh upload cần được tạo thủ công trong thư mục gốc nếu bạn chưa có.
- Đảm bảo MySQL đang chạy trước khi khởi động ứng dụng.
- Mật khẩu người dùng được mã hóa bằng BCrypt.
- Nếu gặp lỗi CORS, hãy kiểm tra lại file `WebConfig.java`.

## 📌 Hướng phát triển

- Thêm tính năng thông báo real-time
- Tích hợp WebSocket cho chat
- Deploy ứng dụng lên Heroku hoặc AWS
- Phát triển bản mobile với React Native hoặc Flutter

## 📚 Tài liệu tham khảo

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Thymeleaf Docs](https://www.thymeleaf.org/)
- [MySQL Docs](https://dev.mysql.com/doc/)

## Tài khoản admin
- Account: admin
- Password: 1
