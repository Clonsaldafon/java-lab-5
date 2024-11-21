package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;
import service.UserServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = (String)req.getSession().getAttribute("login");
        String pass = (String)req.getSession().getAttribute("pass");

        User user = userService.get(login);
        if (user == null || !user.getPass().equals(pass)) {
            resp.setContentType("text/html;charset=utf-8");
            resp.sendRedirect(req.getContextPath());
            return;
        }

        String path = req.getParameter("path");

        File file = new File(path);
        if (file.exists()) {
            String mimeType = getServletContext().getMimeType(file.getAbsolutePath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            resp.setContentType(mimeType);
            resp.setContentLength((int) file.length());

            String header = String.format("attachment; filename=\"%s\"", file.getName());
            resp.setHeader("Content-Disposition", header);

            FileInputStream in = new FileInputStream(file);
            OutputStream out = resp.getOutputStream();

            byte[] buffer = new byte[4096];

            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();
            out.close();
        }
    }

}