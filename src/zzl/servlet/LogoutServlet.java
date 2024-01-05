package zzl.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import zzl.Utils;
import zzl.beans.json.Result;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Result<Boolean> result = new Result<>();
        result.setStatus("ok");
        try {
            if(request.getSession().getAttribute("user")!=null)
            {
                request.getSession().removeAttribute("user");
            }
            result.setData(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(false);
        }
        Utils.send(response,new Gson().toJson(result));
    }
}
