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
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/")
public class SessionsServlet extends HttpServlet {

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

        RequestDispatcher view = req.getRequestDispatcher("log.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");

        if (login.isEmpty() || pass.isEmpty()) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Заполните все поля");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User user = userService.get(login);

        if (user == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Пользователя с таким логином не существует");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!user.getPass().equals(pass)) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Неверный пароль");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        req.getSession().setAttribute("login", login);
        req.getSession().setAttribute("pass", pass);

        String encodePath = URLEncoder.encode(fileManagerPath + login, "UTF-8");
        resp.sendRedirect(req.getContextPath() + "/files?path=" + encodePath);
    }
}
