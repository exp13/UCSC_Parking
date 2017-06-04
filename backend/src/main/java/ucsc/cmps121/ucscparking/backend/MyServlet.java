/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package ucsc.cmps121.ucscparking.backend;

import java.io.IOException;
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

    private static class FunctionJunction{

        FunctionJunction(){}

        public String processRequest(HttpServletRequest req) throws IOException{
            return "This is not the class you are looking for.";
        }

        static class TestFunc extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                return ("This is a test: "+req.getParameter("testValue"));
            }
        }

        static class SaveUser extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User nU = new User();
                nU.id = req.getParameter("userid");
                nU.liPlate = req.getParameter("plate");

                DBHandler.putUser(nU);

                return "Saved "+nU.id;
            }
        }
    }

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
        funMap.put("SaveUser", new FunctionJunction.SaveUser());

        String myResp = funMap.get(req.getParameter("func")).processRequest(req);

        resp.setContentType("text/plain");
        resp.getWriter().println(myResp);
    }
}
