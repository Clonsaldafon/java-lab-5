package servlet;

import model.User;
import service.UserService;
import service.UserServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/reg")
public class UsersServlet extends HttpServlet {

    private final String fileManagerPath = "/home/anton_bezmelnitsin/file-manager/";
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = (String)req.getSession().getAttribute("login");

        User user = userService.get(login);
        if (user != null) {
            String encodePath = URLEncoder.encode(fileManagerPath + login, "UTF-8");
            resp.sendRedirect(req.getContextPath() + "/files?path=" + encodePath);
            return;
        }
        RequestDispatcher view = req.getRequestDispatcher("reg.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");

        if (email.isEmpty() || login.isEmpty() || pass.isEmpty()) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Заполните все поля");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (userService.get(login) != null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Такой пользователь уже существует");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User user = new User(login, pass, email);
        userService.add(user);

        req.getSession().setAttribute("login", login);
        req.getSession().setAttribute("pass", pass);

        File directory = new File(fileManagerPath + login);
        if (!directory.mkdir()) {
            resp.setContentType("text/html;charset=utf-8");
            /*resp.getWriter().println("Не удалось создать папку пользователя");*/
            resp.getWriter().println(fileManagerPath + login);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String encodePath = URLEncoder.encode(fileManagerPath + login, "UTF-8");
        resp.sendRedirect(req.getContextPath() + "/files?path=" + encodePath);
    }
}
