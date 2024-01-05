package zzl.servlet;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import zzl.Utils;
import zzl.beans.json.LoginBeans;
import zzl.beans.json.Result;
import zzl.beans.User;
import zzl.beans.json.UserInfo;
import zzl.dao.MySql;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = Utils.getJsonFromStream(request.getInputStream());
        Result<UserInfo> result = new Result<>();
        Gson gson = new Gson();
        if (!json.isEmpty()) {
            LoginBeans loginBeans = gson.fromJson(json, LoginBeans.class);
            UserInfo user = login(loginBeans);
            if (user != null) {
                result.setStatus("ok");
                result.setData(user);
                User u = new User();
                u.setType(loginBeans.getType());
                u.setUserID(user.getId());
                request.getSession().setAttribute("user", u);
            } else {
                result.setStatus("error");
            }

        } else {
            result.setStatus("error");
            result.setMsg("错误的访问参数");
        }
        Utils.send(response, gson.toJson(result));

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doGet(request, response);
    }

    /**
     * 连接数据库验证密码
     * @param loginBeans
     * @return
     */
    private UserInfo login(LoginBeans loginBeans) {
        String sql = "select iduser,pwd,type,nickname,sex,balance from user where phone=?";
        MySql mySql = new MySql();
        UserInfo user = null;
        ResultSet rs = mySql.getData(sql, loginBeans.getPhone());
        try {
            if (rs.next()) {
                if (rs.getString("pwd").equals(loginBeans.getPwd())) {
                    String type = rs.getString("type");
                    if (String.valueOf(loginBeans.getType()).equals(type)) {
                        user = new UserInfo();
                        user.setId(rs.getInt("iduser"));
                        user.setNickName(rs.getString("nickname"));
                        user.setSex(rs.getString("sex"));
                        user.setPhone(loginBeans.getPhone());
                        user.setType(Integer.valueOf(type));
                        user.setBalance(rs.getFloat("balance"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySql.closeAllConnection();
        }
        return user;
    }
}
