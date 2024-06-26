package zzl.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import zzl.Utils;
import zzl.beans.User;
import zzl.beans.json.Result;

import java.io.IOException;

@WebServlet(name = "IsLoginServlet",urlPatterns = {"/islogin"})
public class IsLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        Result<Boolean> result = new Result<>();
        result.setStatus("ok");
        try {
            User user=(User) request.getSession().getAttribute("user");
            result.setData(user!=null);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(false);
        }
        Utils.send(response,new Gson().toJson(result));
    }
}
