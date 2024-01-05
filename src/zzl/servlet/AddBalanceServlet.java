package zzl.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import zzl.Utils;
import zzl.beans.User;
import zzl.beans.json.OrderItemBean;
import zzl.beans.json.Result;
import zzl.dao.MySql;
import java.io.IOException;


@WebServlet(name = "AddBalanceServlet",urlPatterns = {"/addbalance"})
public class AddBalanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException, ServletException {
        super.doPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        MySql mySql = new MySql();
        User user = (User) request.getSession().getAttribute("user");
        Result<Boolean> result = new Result<>();
        try {

            result.setStatus("ok");
            String str=request.getParameter("money");
            result.setData(false);
            if(str!=null&&!str.isEmpty())
            {
                float money=Float.parseFloat(str);
                if(money>0)
                {
                    mySql.updateBalance(user.getUserID(),money);
                    result.setData(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("error");
        } finally {
            mySql.closeAllConnection();
        }
        Gson gson = new Gson();
        Utils.send(response, gson.toJson(result));
    }
}
