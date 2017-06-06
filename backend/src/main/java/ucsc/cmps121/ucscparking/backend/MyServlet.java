/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package ucsc.cmps121.ucscparking.backend;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.*;

import com.google.appengine.repackaged.com.google.common.base.Function;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class MyServlet extends HttpServlet {

    private static class DBHandler{
        static{
            ObjectifyService.register(User.class);
        }

        public static void putUser(User u){
            ofy().save().entity(u).now();
        }

        public static User getUser(String id) {
            Key<User> uKey = Key.create(User.class, id);
            User gU = ofy().load().key(uKey).now();

            return gU;
        }
    }

    private static class FunctionJunction{


        private static ArrayList<String> lotIndex;

        FunctionJunction(){
            lotIndex = new ArrayList<>();
            lotIndex.add(0, "empty");
            lotIndex.add(1, "North Remote");
            lotIndex.add(2, "East Remote");
            lotIndex.add(3, "West Remote");
            lotIndex.add(4, "Core West");
            lotIndex.add(5, "Performing Arts");
            lotIndex.add(6, "Kresge College");
            lotIndex.add(7, "Stevenson College");
            lotIndex.add(8, "Baskin 1");
            lotIndex.add(9, "Baskin 2");
            lotIndex.add(10, "Rachel Carson College");
            lotIndex.add(11, "Oakes College");
            lotIndex.add(12, "Merrill College");
            lotIndex.add(13, "Porter College");
            lotIndex.add(14, "College 10");
            lotIndex.add(15, "Hahn Building");
        }

        public String processRequest(HttpServletRequest req) throws IOException{
            return "This is not the class you are looking for.";
        }

        static class TestFunc extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                return ("This is a test: "+req.getParameter("testValue"));
            }
        }

        static class CreateUser extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User nU = new User();
                nU.id = req.getParameter("userid");
                nU.liPlate = req.getParameter("plate");

                DBHandler.putUser(nU);

                return "Saved "+nU.id;
            }
        }

        static class AddLotPref extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User u = DBHandler.getUser(req.getParameter("userid"));

                LotPref p = new LotPref();

                p.name = req.getParameter("name");
                p.time = req.getParameter("time");
                p.lots[0] = lotIndex.indexOf(req.getParameter("lot1"));
                p.lots[1] = lotIndex.indexOf(req.getParameter("lot2"));
                p.lots[2] = lotIndex.indexOf(req.getParameter("lot3"));

                u.prefLots.add(p);

                DBHandler.putUser(u);

                return "Added pref "+p.name;
            }
        }

        static class CheckUserExists extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User testUser = DBHandler.getUser(req.getParameter("userid"));

                if(testUser != null){
                    return "true";
                }

                return "false";
            }
        }

        static class GetLots extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User u = DBHandler.getUser(req.getParameter("userid"));
                String resp = new String();
                LotPref p;

                if (u == null || u.prefLots == null)
                {
                    return "empty list";
                } else {

                    for (int i = 0; i < u.prefLots.size(); i++) {
                        p = u.prefLots.get(i);
                        resp += p.time + "|";
                        resp += p.name + ";";
                        for (int j = 0; j < 3; j++) {
                            resp += lotIndex.get(p.lots[j]) + ",";
                        }

                        resp += "!";
                    }

                }

                return resp;
            }
        }

    }


    @Entity
    static class User{
        @Id String id;
        String liPlate;
        ArrayList<LotPref> prefLots;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Map<String, FunctionJunction> funMap = new HashMap<>();
        funMap.put("TestFunc", new FunctionJunction.TestFunc());
        funMap.put("CreateUser", new FunctionJunction.CreateUser());
        funMap.put("CheckUserExists", new FunctionJunction.CheckUserExists());
        funMap.put("GetLots", new FunctionJunction.GetLots());
        funMap.put("AddLotPref", new FunctionJunction.AddLotPref());

        String myResp = funMap.get(req.getParameter("func")).processRequest(req);

        resp.setContentType("text/plain");
        resp.getWriter().println(myResp);
    }
}
