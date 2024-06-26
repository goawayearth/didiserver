package zzl.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import zzl.Utils;
import zzl.beans.User;
import zzl.beans.json.OrderItemBean;
import zzl.beans.json.PathBean;
import zzl.beans.json.Result;
import zzl.dao.MySql;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

@WebServlet(name = "OrderItemServlet", urlPatterns = {"/orderitem"})
public class OrderItemServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        MySql mySql = new MySql();
        User user = (User) request.getSession().getAttribute("user");
        Result<List<OrderItemBean>> result = new Result<>();
        List<OrderItemBean> list = new LinkedList<>();
        try {
            String sql;
            if(user.getType()==0)//货主查询订单
            {
                sql="select idorder,start,end,price,iduser,nickname,phone,order_time from orders,user " +
                        "where iduser=driverid and ownerid=?";
            }else{//司机查询订单
                sql="select idorder,start,end,price,iduser,nickname,phone,order_time from orders,user " +
                        "where iduser=ownerid and driverid=?";
            }
            ResultSet rs = mySql.getData(sql, user.getUserID());

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));//解决时间快8个小时
            while (rs.next()) {
                OrderItemBean item=new OrderItemBean();
                item.setOrderID(rs.getInt("idorder"));
                item.setStart(rs.getString("start"));
                item.setEnd(rs.getString("end"));
                item.setPrice(rs.getFloat("price"));
                item.setAccount(rs.getInt("iduser"));
                item.setNickName(rs.getString("nickname"));
                item.setPhone(rs.getString("phone"));
                Timestamp time=rs.getTimestamp("order_time");
                item.setTime(sdf.format(time));

                list.add(item);
            }
            result.setStatus("ok");
        } catch (SQLException e) {
            e.printStackTrace();
            result.setStatus("error");
        } finally {
            mySql.closeAllConnection();
        }
        result.setData(list);


        Gson gson = new Gson();
        Utils.send(response, gson.toJson(result));
    }
}
