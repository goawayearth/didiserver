package zzl.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import zzl.Utils;
import zzl.beans.User;
import zzl.beans.json.PathBean;
import zzl.beans.json.Result;
import zzl.dao.MySql;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@WebServlet(name = "UpdatePathServlet",urlPatterns = {"/updatepath"})
public class UpdatePathServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json= Utils.getJsonFromStream(request.getInputStream());
        Gson gson=new Gson();
        PathBean[] data=gson.fromJson(json,PathBean[].class);
        Result<Boolean> result=new Result<>();
        result.setStatus("ok");
        MySql mySql=new MySql();
        try {
            User user =(User)request.getSession().getAttribute("user");
            Connection conn=mySql.getConnection();
            conn.setAutoCommit(false);
            mySql.updateData("delete from path where iduser=?",user.getUserID());
            for(int i=0,len=data.length;i<len;i++)
            {
                mySql.updateData("insert into path (iduser,location,orderNum,carriage) values (?,?,?,?)"
                        ,user.getUserID()
                        ,data[i].getLocation()
                        ,i
                        ,data[i].getCarriage());
            }
            conn.commit();
            result.setData(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(false);
        } finally {
            mySql.closeAllConnection();
        }
        Utils.send(response,gson.toJson(result));
    }
}
