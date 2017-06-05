/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package ucsc.cmps121.ucscparking.backend;

import java.io.IOException;

import javax.servlet.http.*;

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
            ofy().save().entity(u);
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
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        User nU = new User();
        nU.id = "testID";
        nU.liPlate = name;
        DBHandler.putUser(nU);
        nU = DBHandler.getUser("testID");
        resp.getWriter().println("Hello " + nU.liPlate);
    }
}
