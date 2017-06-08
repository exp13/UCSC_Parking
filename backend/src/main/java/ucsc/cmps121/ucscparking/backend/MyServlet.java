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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.*;

import com.google.appengine.repackaged.com.google.common.base.Flag;
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
            ObjectifyService.register(ParkingLot.class);
        }

        public static void putUser(User u){
            ofy().save().entity(u).now();
        }

        public static User getUser(String id) {
            Key<User> uKey = Key.create(User.class, id);
            User gU = ofy().load().key(uKey).now();

            return gU;
        }

        public static void putLot(ParkingLot p) {ofy().save().entity(p).now();}

        public static ParkingLot getLot(String id){
            Key<ParkingLot> pKey = Key.create(ParkingLot.class, id);
            ParkingLot gP = ofy().load().key(pKey).now();

            return gP;
        }
    }

    private static class FunctionJunction{


        private static ArrayList<String> lotIndex;
        private static int[] spotNumIndex;

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

            spotNumIndex = new int[]{
                    0,
                    100,100,100,100,100,
                    100,100,100,100,100,
                    100,100,100,100,100
            };
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

                DBHandler.putUser(nU);

                return "Saved "+nU.id;
            }
        }

        static class SavePlate extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User u = DBHandler.getUser(req.getParameter("userid"));
                String plate = req.getParameter("plate");

                u.liPlate = plate;
                DBHandler.putUser(u);

                return "Saved "+plate;
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

                if(u.prefLots == null) {
                    u.prefLots = new ArrayList<>();
                }
                u.prefLots.add(p);

                DBHandler.putUser(u);

                return "Added pref "+p.name;
            }
        }

        static class SaveLotPref extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User u = DBHandler.getUser(req.getParameter("userid"));
                String name = req.getParameter("name");
                LotPref p = new LotPref();
                for(int i=0; i<u.prefLots.size(); i++){
                    p = u.prefLots.get(i);
                    if(p.name.contentEquals(name)){
                        u.prefLots.remove(i);
                        break;
                    }
                }
                p.time = req.getParameter("time");
                p.lots[0] = lotIndex.indexOf(req.getParameter("lot1"));
                p.lots[1] = lotIndex.indexOf(req.getParameter("lot2"));
                p.lots[2] = lotIndex.indexOf(req.getParameter("lot3"));

                u.prefLots.add(p);

                DBHandler.putUser(u);

                return "Saved " + p.name;
            }
        }

        static class DeleteLotPref extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                User u = DBHandler.getUser(req.getParameter("userid"));
                String name = req.getParameter("name");
                LotPref p = new LotPref();
                for(int i=0; i<u.prefLots.size(); i++){
                    p = u.prefLots.get(i);
                    if(p.name.contentEquals(name)){
                        u.prefLots.remove(i);
                        break;
                    }
                }

                DBHandler.putUser(u);

                return "Deleted " + p.name;
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
                    }
                    resp += "!";
                }

                return resp;
            }
        }

        static class GetParkingLot extends FunctionJunction{
            @Override
            public String processRequest(HttpServletRequest req) throws IOException{
                String lotname = req.getParameter("lotid");
                ParkingLot p = DBHandler.getLot(lotname);
                String resp = new String();

                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("PST"));
                if(p == null){
                    p = new ParkingLot();
                    p.lotName = lotname;
                    p.lastUpdate = "08:00";

                    p.spots = new ArrayList<>();
                    LotSpot s;
                    for(int i=1; i<=spotNumIndex[lotIndex.indexOf(lotname)]; i++){
                        s = new LotSpot();
                        s.spotFull = false;
                        s.spotID = i;
                        s.spotUser = new String();
                        s.spotUser = "none";
                        s.checkInTime = new String();
                        s.checkInTime = "00:00";
                        s.checkInDate = cal.get(Calendar.DAY_OF_MONTH);
                        s.durationH = 0;
                        s.durationM = 0;

                        p.spots.add(s.spotID-1, s);
                    }

                }else{
                    p = updateLotStatus(p, cal);
                }

                resp = "";
                LotSpot s;
                int emSpots = 0;

                //construct response
                for(int i=0; i<p.spots.size(); i++){
                    s = p.spots.get(i);
                    if(!s.spotFull){
                        emSpots++;
                    }
                }
                resp += Integer.toString(emSpots);
                resp += "!";

                DBHandler.putLot(p);

                return resp;
            }

            private ParkingLot updateLotStatus(ParkingLot p, Calendar cal){
                Time lastUpdate = Time.valueOf(p.lastUpdate+":00");
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMin = cal.get(Calendar.MINUTE);
                String cH = Integer.toString(currentHour);
                String cM = Integer.toString(currentMin);

                if(currentHour<10){cH = "0" + cH;}
                if(currentMin<10){cM = "0" + cM;}

                // outsit parking hours
                if(currentHour < 8 || currentHour > 22) {
                    p = clearLot(p);

                //last update occured over 15 min ago
                }else if(currentHour>lastUpdate.getHours() ||
                        currentMin> (lastUpdate.getMinutes()+15) ) {

                    p.lastUpdate = cH+":"+cM;

                    LotSpot s;
                    for(int i=0; i<p.spots.size(); i++){
                        s = p.spots.get(i);
                        s = updateSpot(s);

                        p.spots.remove(i);
                        p.spots.add(i, s);
                    }

                }

                return p;
            }

            private LotSpot updateSpot(LotSpot s){
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("PST"));
                if(!s.spotFull){
                    int currentDay = cal.get(Calendar.DAY_OF_MONTH);
                    int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    int currentMin = cal.get(Calendar.MINUTE);
                    boolean overHours = false;

                    Time checkIn = Time.valueOf(s.checkInTime+":00");

                    // check if past checkin day or past hour duration
                    if(currentDay>s.checkInDate ||
                            currentHour> (checkIn.getHours()+s.durationH) )
                    {overHours = true;}

                    // if past hour duration check past minute duration and clear if true
                    if(overHours && currentMin>(checkIn.getMinutes()+s.durationM) ){
                        s.spotFull = false;
                        s.spotUser = "none";
                        s.checkInTime = "00:00";
                        s.checkInDate = cal.get(Calendar.DAY_OF_MONTH);
                        s.durationH = 0;
                        s.durationM = 0;
                    }
                }

                return s;
            }

            private ParkingLot clearLot(ParkingLot p){
                p.lastUpdate = "08:00";
                Calendar cal = Calendar.getInstance();

                LotSpot s;
                for(int i=0; i<p.spots.size(); i++){
                    s = p.spots.get(i);
                    s.spotFull = false;
                    s.spotUser = "none";
                    s.checkInTime = "00:00";
                    s.checkInDate = cal.get(Calendar.DAY_OF_MONTH);
                    s.durationH = 0;
                    s.durationM = 0;

                    p.spots.remove(i);
                    p.spots.add(i, s);
                }

                return p;
            }
        }

    }


    @Entity
    static class User{
        @Id String id;
        String liPlate;
        ArrayList<LotPref> prefLots;
    }

    @Entity
    static class ParkingLot{
        @Id String lotName;
        ArrayList<LotSpot> spots;
        String lastUpdate;
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
        funMap.put("SaveLotPref", new FunctionJunction.SaveLotPref());
        funMap.put("DeleteLotPref", new FunctionJunction.DeleteLotPref());
        funMap.put("SavePlate", new FunctionJunction.SavePlate());
        funMap.put("GetParkingLot", new FunctionJunction.GetParkingLot());

        String myResp = funMap.get(req.getParameter("func")).processRequest(req);

        resp.setContentType("text/plain");
        resp.getWriter().println(myResp);
    }
}
