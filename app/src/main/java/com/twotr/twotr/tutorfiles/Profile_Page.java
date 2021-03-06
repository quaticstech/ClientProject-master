package com.twotr.twotr.tutorfiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;
import com.twotr.twotr.R;
import com.twotr.twotr.globalpackfiles.Global_url_twotr;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class Profile_Page extends AppCompatActivity {
    Button but_personal,but_educational,but_professional,but_profile_edit;
    ScrollView scrollview_personal,scrollView_educational,scrollview_professional;
    String s_profile;
    SharedPreferences Shared_user_details;
    String Stoken,Sid,SfirstName,SmiddleName,SlastName,Sdob,Sdescription,SlineOne,SzipCode,Snumber,Scode,Sroles;
    TextView TVemailverify,TVmobileverify;
    String  Sgender;
    AVLoadingIndicatorView avi;
    Context context;
    TextView TVfirstname,TVmiddlename,TVlastname,TVgender,TVdob,TVaddress,TVpostalcode,TVmobile_number,TVemail,TVaboutme;
   TextView TVmajor,TVinsitute,TVyear;
   TextView TVtitle,TVproffinstitue,TVexperi;
   TextView TVheadernamefull;
   CircleImageView CIVprofimage;
   ImageButton IB_back;
   GifImageView pendinggiflist;
   TextView pendingtextlist;
    ArrayList<String> Listgrade ;
    ArrayList<Integer> Listgradeint;
    ArrayList<String> ListSubject ;
    ArrayList<String> ListSubjectid ;
    String  Surl= null;
    RecyclerView recyclerView,recyclerViewsub;
    RecyclerView.LayoutManager RecyclerViewLayoutManager,RecyclerViewLayoutManagersub;
    RecyclerViewAdapter RecyclerViewHorizontalAdapter,RecyclerViewHorizontalAdaptersub;
    LinearLayoutManager HorizontalLayout,HorizontalLayoutsub ;
    private Boolean mobile_firstTime = null;
    private Boolean email_firstTime = null;
    Boolean isEducationCompleted, isProfessionalCompleted;
    String Smajor ;
    String Sinstitution ;
    String SfieldOfStudy ;
    String SstartYear ;
    String SendYear ;
    String Stitle;
    String SinstitutionName;
    String Sexperience;
    Boolean SisStudyingCurrently;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        context=this;
        but_personal=findViewById(R.id.prof_but_personal);
        but_educational=findViewById(R.id.prof_nut_educational);
        but_professional=findViewById(R.id.prof_but_professional);
        scrollview_personal=findViewById(R.id.srcollview_personal);
        but_profile_edit=findViewById(R.id.profile_edit);
        TVfirstname=findViewById(R.id.profile_name);
        TVmiddlename=findViewById(R.id.profile_middle);
        TVlastname=findViewById(R.id.profile_last);
        TVgender=findViewById(R.id.profile_gender);
        TVdob=findViewById(R.id.profile_dob);
        TVaddress=findViewById(R.id.profile_address);
        TVpostalcode=findViewById(R.id.profile_postcode);
        TVmobile_number=findViewById(R.id.profile_monumber);
        TVemail=findViewById(R.id.profile_email);
        TVaboutme=findViewById(R.id.profile_description);
        TVemailverify=findViewById(R.id.profile_email_verify);
        TVmobileverify=findViewById(R.id.profile_mobile_verify);
    TVheadernamefull=findViewById(R.id.prof_prof_name);
    pendinggiflist=findViewById(R.id.nodatagif);
    pendingtextlist=findViewById(R.id.nodatatext);

        scrollView_educational=findViewById(R.id.srcollview_educational);
        scrollview_professional=findViewById(R.id.srcollview_professional);
        Shared_user_details=getSharedPreferences("user_detail_mode",0);
        Stoken=  Shared_user_details.getString("token", null);
        Sid=  Shared_user_details.getString("id", null);
        Sroles=  Shared_user_details.getString("roles", null);

        TVmajor=findViewById(R.id.profile_edu_major);
 TVinsitute=findViewById(R.id.prof_edu_insitute);
 TVyear=findViewById(R.id.prof_edu_dyear);
CIVprofimage=findViewById(R.id.image_profile);
 TVtitle=findViewById(R.id.prof_profess_title);
        TVproffinstitue=findViewById(R.id.prof_profess_insti);
 TVexperi=findViewById(R.id.prof_profess_experi);
 IB_back=findViewById(R.id.back_ima_scedule);
        recyclerView = findViewById(R.id.recyclerview1);
        recyclerViewsub=findViewById(R.id.recyclerviewsubject);

        IB_back.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         finish();
     }
 });

        avi=findViewById(R.id.avi);
       Listgrade= new ArrayList<String>();
        ListSubject= new ArrayList<String>();
        ListSubjectid= new ArrayList<String>();

        Listgradeint=new ArrayList<>();
        gettallprofiledetails();

        if (Sroles.equals("student"))
        {
            but_professional.setVisibility(View.INVISIBLE);
        }
        else {
            but_professional.setVisibility(View.VISIBLE);
        }
        s_profile="personal";

        but_personal.setTextColor(getResources().getColor(R.color.buttonColorPrimary));
        but_personal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                s_profile="personal";
                but_personal.setTextColor(getResources().getColor(R.color.buttonColorPrimary));
                but_educational.setTextColor(getResources().getColor(R.color.colorwhite));
                but_professional.setTextColor(getResources().getColor(R.color.colorwhite));
                scrollview_personal.setVisibility(View.VISIBLE);
                scrollView_educational.setVisibility(View.GONE);
                scrollview_professional.setVisibility(View.GONE);
                pendinggiflist.setVisibility(View.INVISIBLE);
                pendingtextlist.setVisibility(View.INVISIBLE);

            }
        });
        but_educational.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                s_profile="educational";
                but_educational.setTextColor(getResources().getColor(R.color.buttonColorPrimary));
                but_personal.setTextColor(getResources().getColor(R.color.colorwhite));
                but_professional.setTextColor(getResources().getColor(R.color.colorwhite));
                scrollView_educational.setVisibility(View.VISIBLE);
                scrollview_personal.setVisibility(View.GONE);
                scrollview_professional.setVisibility(View.GONE);
                if (!isEducationCompleted)
                {
                    scrollView_educational.setVisibility(View.INVISIBLE);

                    pendinggiflist.setVisibility(View.VISIBLE);
                    pendingtextlist.setVisibility(View.VISIBLE);
                }
                else
                {
                    scrollView_educational.setVisibility(View.VISIBLE);

                    pendinggiflist.setVisibility(View.INVISIBLE);
                    pendingtextlist.setVisibility(View.INVISIBLE);
                }
            }
        });
        but_professional.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                s_profile="professional";
                but_professional.setTextColor(getResources().getColor(R.color.buttonColorPrimary));
                but_personal.setTextColor(getResources().getColor(R.color.colorwhite));
                but_educational.setTextColor(getResources().getColor(R.color.colorwhite));
                scrollview_professional.setVisibility(View.VISIBLE);
                scrollview_personal.setVisibility(View.GONE);
                scrollView_educational.setVisibility(View.GONE);
                if (!isProfessionalCompleted)
                {
                    scrollview_professional.setVisibility(View.INVISIBLE);

                    pendinggiflist.setVisibility(View.VISIBLE);
                    pendingtextlist.setVisibility(View.VISIBLE);
                }
                else
                {
                    scrollview_professional.setVisibility(View.VISIBLE);

                    pendinggiflist.setVisibility(View.INVISIBLE);
                    pendingtextlist.setVisibility(View.INVISIBLE);
                }
            }
        });
        but_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s_profile.matches("personal")||s_profile.equals("personal"))
                {
                    Intent intent = new Intent(Profile_Page.this, Profile_Update_Personal.class);
                    intent.putExtra("firstname", SfirstName);
intent.putExtra("lastname",SlastName);
                    intent.putExtra("gender",Sgender);
                    intent.putExtra("dob",Sdob);
                    intent.putExtra("descrip",Sdescription);
                    intent.putExtra("addres",SlineOne);
                    intent.putExtra("zipcode",SzipCode);
                    intent.putExtra("monumber",Snumber);
                    intent.putExtra("mcode",Scode);
                   intent.putIntegerArrayListExtra("gradelevelint",Listgradeint);
                    intent.putStringArrayListExtra("subid",ListSubjectid);
                    intent.putStringArrayListExtra("subname",ListSubject);
                    intent.putStringArrayListExtra("gradelevel",Listgrade);
intent.putExtra("imageurl",Surl);
                    startActivity(intent);

                }
                else if(s_profile.matches("educational")||s_profile.equals("educational"))
                {
                    Intent intentin=new Intent(Profile_Page.this,Profile_update_educational.class);
                    intentin.putExtra("smajor",Smajor);
                    intentin.putExtra("sinstitution",Sinstitution);
                    intentin.putExtra("sfieldOfStudy",SfieldOfStudy);
                    intentin.putExtra("sstartYear",SstartYear);
                    intentin.putExtra("sendYear",SendYear);

                    startActivity(intentin);

                }
                else if(s_profile.matches("professional")||s_profile.equals("professional"))
                {
                    Intent intentinprof=new Intent(Profile_Page.this,Profile_Update_Professional.class);
                    intentinprof.putExtra("stitle",Stitle);
                    intentinprof.putExtra("sinstitutionName",SinstitutionName);
                    intentinprof.putExtra("sexperience",Sexperience);

                    startActivity(intentinprof);

                }
            }
        });

TVemailverify.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        email_verify();
        new MaterialDialog.Builder(Profile_Page.this)

                .title("Email Verification")
                .content("Verification Mail has been sent to your mail id.Please Check !")
                .positiveText("Ok")
                .negativeText("Resend")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(context, "Dismissed", Toast.LENGTH_SHORT).show();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(context, "EMail Resend", Toast.LENGTH_SHORT).show();

                    }
                })
                .show();

    }
});

TVmobileverify.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mobile_verify();
        new MaterialDialog.Builder(Profile_Page.this)
                .title("Enter Your OTP")
                .content("A verification sms has been sent to your mobile number")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER)
                .input("Enter Your OTP","", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        mobile_verify_pass(input.toString());
                    }
                }).show();

    }
});

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        RecyclerViewHorizontalAdapter = new RecyclerViewAdapter(Listgrade);

        HorizontalLayout = new LinearLayoutManager(Profile_Page.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
recyclerView.setHorizontalScrollBarEnabled(false);

        RecyclerViewLayoutManagersub = new LinearLayoutManager(getApplicationContext());
        recyclerViewsub.setLayoutManager(RecyclerViewLayoutManagersub);
        RecyclerViewHorizontalAdaptersub = new RecyclerViewAdapter(ListSubject);
        HorizontalLayoutsub = new LinearLayoutManager(Profile_Page.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewsub.setLayoutManager(HorizontalLayoutsub);
        recyclerViewsub.setHorizontalScrollBarEnabled(false);





    }


    public void gettallprofiledetails()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Profile_getdetails, new Response.Listener<String>() {

            public void onResponse(String response) {
                try {
                    avi.hide();
                    JSONObject jObj = new JSONObject(response);
                  //  String id = jObj.getString("_id");
                   // String userId = jObj.getString("userId");
                  //  String updatedAt = jObj.getString("updatedAt");
                  //  String createdAt = jObj.getString("createdAt");

                    JSONObject profile = jObj.getJSONObject("profileInfo");
                     Sdob =profile.getString("dob");
                      Sgender=profile.getString("gender");
             //       String Stimezone =profile.getString("timezone");
               //     String Scantutor =profile.getString("canTutor");
                //    String  SdefaultCountry=profile.getString("defaultCountry");
                      Sdescription=profile.getString("description");


                    JSONObject paddress = profile.getJSONObject("address");
                      SlineOne=paddress.getString("lineOne");
               //     String  Scity=paddress.getString("city");
                 //   String  Sstate=paddress.getString("state");
                      SzipCode=paddress.getString("zipCode");
                  //  String  Scountry=paddress.getString("country");

                    JSONObject pmonum = profile.getJSONObject("mobileNumber");
                      Scode=pmonum.getString("code");
                      Snumber=pmonum.getString("number");

                    JSONArray jsonArray = profile.getJSONArray("gradeLevel");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        Listgrade.add(String.valueOf(jsonArray.get(i)));
                   Listgradeint.add(i);
                    }

                    recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

                    JSONArray jsonArray1 = profile.getJSONArray("subjectIds");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        String Skind =jsonObject.getString("title");
                        String Sid =jsonObject.getString("_id");
                        ListSubject.add(Skind);
                        ListSubjectid.add(Sid);

                    }

                    recyclerViewsub.setAdapter(RecyclerViewHorizontalAdaptersub);


//roles of tutor is left
                    JSONObject userprofile = jObj.getJSONObject("userProfile");


                    JSONObject verification = userprofile.getJSONObject("verification");
                     Boolean isEmailVerified =verification.getBoolean("isEmailVerified");
                    Boolean isMobileVerified=verification.getBoolean("isMobileVerified");
                    Boolean isProfileCompleted=verification.getBoolean("isProfileCompleted");
                     isEducationCompleted=verification.getBoolean("isEducationCompleted");
                     isProfessionalCompleted=verification.getBoolean("isProfessionalCompleted");
                    Boolean isIdVerified=verification.getBoolean("isIdVerified");
                    Boolean isTeachingVerified=verification.getBoolean("isTeachingVerified");

                    SfirstName=userprofile.getString("firstName");
                    try {
                        SlastName=userprofile.getString("lastName");
                        SmiddleName=userprofile.getString("middleName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    TVfirstname.setText(SfirstName);
                   TVmiddlename.setText(SmiddleName);
                    TVlastname.setText(SlastName);
                    TVgender.setText(Sgender);
                    String monthformating= DateTimeUtils.formatWithPattern(Sdob, "MMM dd, yyyy");
                    TVdob.setText(monthformating);
                    TVaddress.setText(SlineOne);
                    TVpostalcode.setText(SzipCode);
                    TVmobile_number.setText(Snumber);
                    TVemail.setText("");
                    TVaboutme.setText(Sdescription);
                    TVheadernamefull.setText(SfirstName+" "+SlastName);

if (isEmailVerified)
{


    TVemailverify.setVisibility(View.INVISIBLE);
}
if (isMobileVerified)
{
    TVmobileverify.setVisibility(View.INVISIBLE);

}

                    try {
                        JSONObject educationinfo = jObj.getJSONObject("educationInfo");
                        JSONArray jsonArray2 = educationinfo.getJSONArray("degrees");
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObjectedu = jsonArray2.getJSONObject(i);
                             Smajor =jsonObjectedu.getString("major");
                             Sinstitution =jsonObjectedu.getString("institution");
                             SfieldOfStudy =jsonObjectedu.getString("fieldOfStudy");
                             SstartYear =jsonObjectedu.getString("startYear");
                             SendYear =jsonObjectedu.getString("endYear");
                            Boolean SisStudyingCurrently =jsonObjectedu.getBoolean("isStudyingCurrently");

                            TVmajor.setText(Smajor);
                            TVinsitute.setText(Sinstitution);
                            TVyear.setText(SstartYear+"-"+SendYear);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject professionalInfo = jObj.getJSONObject("professionalInfo");
                         Stitle=professionalInfo.getString("title");
                         SinstitutionName=professionalInfo.getString("institutionName");
                         Sexperience=professionalInfo.getString("experience");

                        TVtitle.setText(Stitle);
                        TVproffinstitue.setText(SinstitutionName);
                        TVexperi.setText(Sexperience);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject profilePicture = userprofile.getJSONObject("profilePicture");
                        String profileimage = profilePicture.getString("url");

                        Surl = Global_url_twotr.Image_Base_url+profileimage;

                        Picasso
                                .with(context)
                                .load(Surl)
                                .fit()
                                .centerCrop()
                                .into(CIVprofimage);


                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }




                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                avi.hide();
                new SweetAlertDialog(Profile_Page.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Password Mismatch")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("content-Type", "application/json");
                headers.put("x-tutor-app-id", "tutor-app-android");
                headers.put("authorization", "Bearer "+Stoken);
                return headers;

            }



        };

        requestQueue.add(stringRequest);
    }
    public void mobile_verify_send() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Tutor_number_send, new Response.Listener<String>() {

            public void onResponse(String response) {
                Log.i("mssre",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("msser",error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json");
                headers.put("x-tutor-app-id", "tutor-app-android");
                headers.put("authorization", "Bearer "+Stoken);


                return headers;

            }



        };

        requestQueue.add(stringRequest);
    }

    public void mobile_verify_resend() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Tutor_number_resend, new Response.Listener<String>() {

            public void onResponse(String response) {
                Log.i("mssre",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("msser",error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json");
                headers.put("x-tutor-app-id", "tutor-app-android");
                headers.put("authorization", "Bearer "+Stoken);


                return headers;

            }



        };

        requestQueue.add(stringRequest);
    }

    public void email_verify_send() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Tutor_email_send, new Response.Listener<String>() {

            public void onResponse(String response) {
                Log.i("mssre",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("msser",error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json");
                headers.put("x-tutor-app-id", "tutor-app-android");
                headers.put("authorization", "Bearer "+Stoken);


                return headers;

            }



        };

        requestQueue.add(stringRequest);
    }
    public void email_verify_resend() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Tutor_email_resend, new Response.Listener<String>() {

            public void onResponse(String response) {
                Log.i("mssre",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("msser",error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json");
                headers.put("x-tutor-app-id", "tutor-app-android");
                headers.put("authorization", "Bearer "+Stoken);


                return headers;

            }



        };

        requestQueue.add(stringRequest);
    }
    public void mobile_verify_pass(String otp) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Tutor_number_send_otp_verify+Sid+"/"+otp, new Response.Listener<String>() {

            public void onResponse(String response) {
                TVmobileverify.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("mssotper",error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json");
                headers.put("x-tutor-app-id", "tutor-app-android");
                headers.put("authorization", "Bearer "+Stoken);


                return headers;

            }



        };

        requestQueue.add(stringRequest);
    }

    public void mobile_verify() {
        if (mobile_firstTime== null) {
            SharedPreferences mPreferences = this.getSharedPreferences("mobile_firstTime", Context.MODE_PRIVATE);
            mobile_firstTime = mPreferences.getBoolean("mobile_firstTime", true);
            if (mobile_firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("mobile_firstTime", false);
                editor.apply();
                mobile_verify_send();
            } else
            {
                mobile_verify_resend();
            }
        }
    }
    public void email_verify() {
        if (email_firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("email_firstTime", Context.MODE_PRIVATE);
            email_firstTime = mPreferences.getBoolean("email_firstTime", true);
            if (email_firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("email_firstTime", false);
                editor.apply();
                email_verify_send();
            } else
            {
                email_verify_resend();
            }
        }
    }
}