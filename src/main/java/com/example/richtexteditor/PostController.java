package com.example.richtexteditor;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.richtexteditor.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/image"; // Thay đổi thành đường dẫn bạn muốn lưu ảnh

    @GetMapping("/posts")
    public String showPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts"; // View sẽ hiển thị danh sách các bài viết
    }

    @GetMapping("/post/new")
    public String showPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post_form"; // View để tạo mới bài viết
    }

    @PostMapping("/post")
    public String createPost(@RequestParam("content") String content) {
        Post post = new Post();
        post.setContent(content);
        postService.savePost(post);
        return "redirect:/posts"; // Chuyển hướng về trang danh sách bài viết sau khi lưu
    }

    @Autowired
    private Cloudinary cloudinary;
    // Xử lý upload ảnh
    @PostMapping("/upload/image")
    @ResponseBody
    public Map<String, String> uploadImage(@RequestParam("image") MultipartFile image) {
        Map<String, String> response = new HashMap<>();

        if (!image.isEmpty()) {
            try {
                // Tạo một tên file an toàn
                String filename = image.getOriginalFilename();
                String safeFilename = filename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // Bỏ ký tự không hợp lệ
                safeFilename = UUID.randomUUID().toString() + "-" + safeFilename;

                // Tải lên Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("public_id", safeFilename));

                // Trả về đường dẫn file đúng với vị trí ảnh
                System.out.println("name:"+(String) uploadResult.get("secure_url"));
                response.put("success", "true");
                response.put("filePath", (String) uploadResult.get("secure_url")); // Đường dẫn phục vụ ảnh từ Cloudinary
            } catch (IOException e) {
                e.printStackTrace();
                response.put("success", "false");
                response.put("message", "Could not upload image: " + e.getMessage());
            }
        } else {
            response.put("success", "false");
            response.put("message", "Image is empty");
        }

        return response;
    }
//    @PostMapping("/upload/image")
//    @ResponseBody
//    public Map<String, String> uploadImage(@RequestParam("image") MultipartFile image) {
//        Map<String, String> response = new HashMap<>();
//
//        if (!image.isEmpty()) {
//            try {
//                // Tạo đường dẫn lưu file
//                String filename = image.getOriginalFilename();
//                String safeFilename = filename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // Bỏ ký tự không hợp lệ
//                safeFilename=UUID.randomUUID().toString()+"-"+safeFilename;
//                File uploadDir = new File(UPLOAD_DIR);
//
//                // Kiểm tra và tạo thư mục nếu nó không tồn tại
//                if (!uploadDir.exists()) {
//                    uploadDir.mkdirs(); // Tạo thư mục nếu không tồn tại
//                }
//
//                // Lưu file
//                Path uploadFilePath = Paths.get(uploadDir.getAbsolutePath(), safeFilename);
//                System.out.println("Uploading to: " + uploadFilePath.toString()); // In đường dẫn tệp
//                try (InputStream inputStream = image.getInputStream()) {
//                    Files.copy(inputStream, uploadFilePath, StandardCopyOption.REPLACE_EXISTING);
//                }
//
//                // Trả về đường dẫn file đúng với vị trí ảnh
//                response.put("success", "true");
//                response.put("filePath", "/uploads/image/" + safeFilename); // Đường dẫn phục vụ ảnh
//            } catch (IOException e) {
//                e.printStackTrace();
//                response.put("success", "false");
//                response.put("message", "Could not upload image: " + e.getMessage());
//            }
//        } else {
//            response.put("success", "false");
//            response.put("message", "Image is empty");
//        }
//
//        return response;
//    }




}