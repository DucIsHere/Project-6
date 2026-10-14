package org.test;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class AuthCore {

    // 1. Hàm băm mật khẩu bảo mật bằng SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi băm mật khẩu", e);
        }
    }

    // 2. Tạo Token định danh phiên đăng nhập (Dùng khi Login thành công)
    public static String generateAuthToken() {
        return UUID.randomUUID().toString().replace("-", "") + Long.toHexString(System.currentTimeMillis());
    }

    // 3. Logic giả lập Sign up (Đăng ký)
    public static boolean handleSignUp(String username, String rawPassword, String email) {
        // Fail-Fast check
        if (username == null || username.isEmpty() || rawPassword == null || rawPassword.length() < 6) {
            System.out.println("[SIGNUP ERROR] Tên đăng nhập trống hoặc mật khẩu quá ngắn (dưới 6 ký tự)!");
            return false;
        }

        String securePasswordHash = hashPassword(rawPassword);

        // TODO: Lưu username, securePasswordHash, email vào file users.json tại đây
        System.out.println("[SIGNUP SUCCESS] Đã đăng ký tài khoản cho: " + username);

        // Sau khi đăng ký xong, gọi luôn hàm gửi OTP xác thực email ở class OTP bên cạnh
        OTP.sendOtpMail(email);
        return true;
    }

    // 4. Logic giả lập Login (Đăng nhập)
    public static String handleLogin(String inputUsername, String inputPassword, String storedPasswordHash) {
        // Băm mật khẩu người dùng vừa nhập lên để so sánh với bản hash trong database
        String inputHash = hashPassword(inputPassword);

        if (inputHash.equals(storedPasswordHash)) {
            // Đăng nhập thành công -> Sinh token trả về cho client
            String token = generateAuthToken();
            System.out.println("[LOGIN SUCCESS] Đăng nhập thành công! Token cấp phát: " + token);
            return token;
        } else {
            System.out.println("[LOGIN FAILED] Sai mật khẩu hoặc tài khoản không tồn tại!");
            return null;
        }
    }
}