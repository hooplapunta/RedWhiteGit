package me.redwhite.redwhite;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import me.redwhite.redwhite.models.User;


public class NewUserActivity extends Activity {

    Button signUpButton;
    EditText etname;
    EditText etusername;
    EditText etemail;
    EditText etpostalCode;
    EditText etpassword;
    EditText etdob;
    RadioButton rbM;
    RadioButton rbF;

    String name;
    String username;
    String email;
    String postalCode;
    String password;
    String dob;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        signUpButton =(Button)findViewById(R.id.btn_submit);
        rbM = (RadioButton)findViewById(R.id.rbMale);
        rbF = (RadioButton)findViewById(R.id.rbFemale);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               name = etname.getText().toString();


                email = etemail.getText().toString();
                postalCode =etpostalCode.getText().toString();


                dob = etdob.getText().toString();

                if(rbM.isChecked()){
                    gender = "Male";
                }else{
                    gender = "Female";
                }

                User u = new User();
                u.setName(name);
                u.setDob(dob);
                u.setEmail(email);
                u.setPostalcode(postalCode) ;
                u.setGender(gender);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
