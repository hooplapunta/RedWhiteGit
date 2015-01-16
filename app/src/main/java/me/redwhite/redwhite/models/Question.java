package me.redwhite.redwhite.models;

import android.location.Location;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by t-rochew on 12/15/2014.
 */
public class Question implements FirebaseNode{

    String action;
    String created_datetime;
    String created_username;
    String expiry_datetime;
    boolean expiry_status;
    String for_modifier;
    String image_url;
    String question = "Is this question a question?";
    int responses;
    String type;
    String updated_datetime;
    int views;

    ArrayList<String> _for_communities = new ArrayList<String>();
    QuestionLocation _location = new QuestionLocation();
    ArrayList<QuestionOption> _options = new ArrayList<QuestionOption>();

    public class QuestionLocation {
        ArrayList<LatLng> geofence = new ArrayList<LatLng>();
        double lat;
        double lng;
        String name;
        int proximity;
        boolean trigger;

        public ArrayList<LatLng> getGeofence() {
            return geofence;
        }

        public void setGeofence(ArrayList<LatLng> geofence) {
            this.geofence = geofence;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProximity() {
            return proximity;
        }

        public void setProximity(int proximity) {
            this.proximity = proximity;
        }

        public boolean isTrigger() {
            return trigger;
        }

        public void setTrigger(boolean trigger) {
            this.trigger = trigger;
        }

        public QuestionLocation() {

        }

        public QuestionLocation(ArrayList<LatLng> geofence, double lat, double lng, String name, int proximity, boolean trigger) {
            this.geofence = geofence;
            this.lat = lat;
            this.lng = lng;
            this.name = name;
            this.proximity = proximity;
            this.trigger = trigger;
        }

        public QuestionLocation(String geofence, double lat, double lng, String name, int proximity, boolean trigger) {
            String[] points = geofence.split(";");

            this.geofence = new ArrayList<LatLng>();

            for(String p: points)
            {
                String[] ll = p.split(",");

                this.geofence.add(new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1])));
            }

            this.lat = lat;
            this.lng = lng;
            this.name = name;
            this.proximity = proximity;
            this.trigger = trigger;
        }
    }

    public class QuestionOption {
        String key;
        ArrayList<QuestionAnswer> _answers;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public ArrayList<QuestionAnswer> get_answers() {
            return _answers;
        }

        public void set_answers(ArrayList<QuestionAnswer> _answers) {
            this._answers = _answers;
        }

        public QuestionOption() {

        }

        public QuestionOption(String key, ArrayList<QuestionAnswer> _answers) {
            this.key = key;
            this._answers = _answers;
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getCreated_username() {
        return created_username;
    }

    public void setCreated_username(String created_username) {
        this.created_username = created_username;
    }

    public String getExpiry_datetime() {
        return expiry_datetime;
    }

    public void setExpiry_datetime(String expiry_datetime) {
        this.expiry_datetime = expiry_datetime;
    }

    public boolean isExpiry_status() {
        return expiry_status;
    }

    public void setExpiry_status(boolean expiry_status) {
        this.expiry_status = expiry_status;
    }

    public String getFor_modifier() {
        return for_modifier;
    }

    public void setFor_modifier(String for_modifier) {
        this.for_modifier = for_modifier;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getResponses() {
        return responses;
    }

    public void setResponses(int responses) {
        this.responses = responses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_datetime() {
        return updated_datetime;
    }

    public void setUpdated_datetime(String updated_datetime) {
        this.updated_datetime = updated_datetime;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public ArrayList<String> get_for_communities() {
        return _for_communities;
    }

    public void set_for_communities(ArrayList<String> _for_communities) {
        this._for_communities = _for_communities;
    }

    public QuestionLocation get_location() {
        return _location;
    }

    public void set_location(QuestionLocation _location) {
        this._location = _location;
    }

    public ArrayList<QuestionOption> get_options() {
        return _options;
    }

    public void set_options(ArrayList<QuestionOption> _options) {
        this._options = _options;
    }

    public Question() {

    }

    public Question(String action, String created_datetime, String created_username, String expiry_datetime, boolean expiry_status, String for_modifier, String image_url, String question, int responses, String type, String updated_datetime, int views, ArrayList<String> _for_communities, QuestionLocation _location, ArrayList<QuestionOption> _options) {
        this.action = action;
        this.created_datetime = created_datetime;
        this.created_username = created_username;
        this.expiry_datetime = expiry_datetime;
        this.expiry_status = expiry_status;
        this.for_modifier = for_modifier;
        this.image_url = image_url;
        this.question = question;
        this.responses = responses;
        this.type = type;
        this.updated_datetime = updated_datetime;
        this.views = views;
        this._for_communities = _for_communities;
        this._location = _location;
        this._options = _options;
    }

    public Question(String action, String created_datetime, String created_username, String expiry_datetime, boolean expiry_status, String for_modifier, String image_url, String question, int responses, String type, String updated_datetime, int views, Map<String, Object> _for_communities, Map<String, Object> _location, Map<String, Object>  _options) {
        this.action = action;
        this.created_datetime = created_datetime;
        this.created_username = created_username;
        this.expiry_datetime = expiry_datetime;
        this.expiry_status = expiry_status;
        this.for_modifier = for_modifier;
        this.image_url = image_url;
        this.question = question;
        this.responses = responses;
        this.type = type;
        this.updated_datetime = updated_datetime;
        this.views = views;


        this._for_communities = new ArrayList<String>();

        for(Map.Entry<String, Object> e: _for_communities.entrySet())
        {
            if(e != null)
            {
                this._for_communities.add(e.getKey());
            }
        }

        this._location = new QuestionLocation(
                (String) _location.get("geofence"),
                (double) _location.get("lat"),
                (double) _location.get("lng"),
                (String) _location.get("name"),
                (int)(long) _location.get("proximity"),
                (boolean) _location.get("trigger")
        );

        this._options = new ArrayList<QuestionOption>();

        for(Map.Entry<String, Object> e: _options.entrySet())
        {
            if (e != null)
            {
                QuestionOption qo = new QuestionOption();
                qo.setKey(e.getKey());
                qo.set_answers(new ArrayList<QuestionAnswer>());

                Map<String, Object> answers = (Map<String, Object>)e.getValue();
                for(Map.Entry<String, Object> x: answers.entrySet()) {
                    qo.get_answers().add(QuestionAnswer.convertFromMap((HashMap<String, Object>) x.getValue()));
                }

                this._options.add(qo);
            }
        }
    }

    public static Question convertFromMap(Map<String, Object> map)
    {
        return new Question(
                (String)map.get("action"),
                (String)map.get("created_datetime"),
                (String)map.get("created_username"),
                (String)map.get("expiry_datetime"),
                (boolean)map.get("expiry_status"),
                (String)map.get("for_modifier"),
                (String)map.get("image_url"),
                (String)map.get("question"),
                (int)(long)map.get("responses"),
                (String)map.get("type"),
                (String)map.get("updated_datetime"),
                (int)(long)map.get("views"),
                (Map<String,Object>)map.get("for_communities"),
                (Map<String,Object>)map.get("location"),
                (Map<String,Object>)map.get("options")
        );
    }

    public static ArrayList<Question> convertListFromMap(Map<String, Object> map)
    {
        ArrayList<Question> c = new ArrayList<Question>();

        for (Map.Entry<String, Object> e : map.entrySet()) {
            c.add(
                    Question.convertFromMap((Map)e.getValue())
            );
        }

        return c;
    }

    public static void findNodes(ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "question");
        ref.addListenerForSingleValueEvent(listener);
    }

    public static void findNodeByKey(String key, ValueEventListener listener) {
        Firebase ref = new Firebase(FIREBASEPATH + "question/" + key);
        ref.addListenerForSingleValueEvent(listener);
    }

}
