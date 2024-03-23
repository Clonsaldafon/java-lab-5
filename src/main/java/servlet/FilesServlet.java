package servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@WebServlet("/files")
public class FilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path");


        File directory = new File(path);
        File[] files = directory.listFiles();


        req.setAttribute("basePath", directory.getAbsolutePath());
        req.setAttribute("files", files);
        req.setAttribute("timestamp", new Date());

        RequestDispatcher view = req.getRequestDispatcher("files.jsp");
        view.forward(req, resp);
    }

}