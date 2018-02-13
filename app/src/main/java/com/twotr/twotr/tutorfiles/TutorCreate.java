package com.twotr.twotr.tutorfiles;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.twotr.twotr.R;
import com.twotr.twotr.globalpackfiles.Global_url_twotr;
import com.twotr.twotr.globalpackfiles.TinyDB;
import com.wooplr.spotlight.SpotlightView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorCreate extends Fragment {

    private Button Boneonone;
    private Button Bgroup;
    private EditText ETtotalone,ETtotalamount,ETminamount,ETshortins,ETnofstudents;
    private TextView ETgrade;
    private TextView TVaddsched,TVaddmap;
    private TextView TVtypesearch;
    private  String Ssubjectid;
    SearchBox search;
    String BaseSearchurl="http://twotr.com:4040/api/subject/search?key=";
    String search_result;
    public static TutorCreate newInstance() {
        return new TutorCreate();
    }
    final ArrayList<MultiSelectModel> ASgrade= new ArrayList<>();
    ArrayList aryGrade=new ArrayList();
    SharedPreferences Shared_user_details;
    public String Stoken;
    RelativeLayout relativeLayout;
    List<String> starttimesched;
    List<String> endtimesched;
    String typeofstudteach;
Boolean Bisusersubject;
Button add_subject;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_tutor_create, container, false);
        Shared_user_details=this.getActivity().getSharedPreferences("user_detail_mode",0);
        Stoken=  Shared_user_details.getString("token", null);

        Boneonone=view.findViewById(R.id.one_to_one);
        Bgroup=view.findViewById(R.id.group_button);
        ETtotalone=view.findViewById(R.id.amount_create);
        ETnofstudents=view.findViewById(R.id.group_students);

        ETtotalamount=view.findViewById(R.id.total_amount_group);
        ETminamount=view.findViewById(R.id.min_amount_group);
        TVaddsched=view.findViewById(R.id.add_schedule_create);
        TVaddmap=view.findViewById(R.id.add_map_create);
        ETshortins=view.findViewById(R.id.shrot_des_create);
        ETgrade=view.findViewById(R.id.grade_create);
        relativeLayout=view.findViewById(R.id.searchnew);
        ImageView IVaddsch = view.findViewById(R.id.hint_sched);
        ImageView IVaddmap = view.findViewById(R.id.hint_map);
        search =  view.findViewById(R.id.searchbox);
        add_subject=view.findViewById(R.id.add_subject_create);
        search.setLogoText("Search Your Subject here");

        subject_spinner("http://twotr.com:4040/api/userinfo/basic/profile");
        search.enableVoiceRecognition(getActivity());
        starttimesched=new ArrayList<>();
        endtimesched=new ArrayList<>();
        typeofstudteach="oneonone";
        Bisusersubject=false;
        search.setMenuListener(new SearchBox.MenuListener(){

            @Override
            public void onMenuClick() {

            }

        });
        search.setSearchListener(new SearchBox.SearchListener(){

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
                search.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                search.clearSearchable();
                search.clearResults();


            }

            @Override
            public void onSearchTermChanged(String s) {

                subject_spinner(BaseSearchurl+s);

            }

            @Override
            public void onSearch(String searchTerm) {
if (searchTerm.matches(" ")) {
    search.clearSearchable();
    search.clearResults();
}
                TVtypesearch.setText(searchTerm);
                search.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                user_subject(searchTerm);
                Bisusersubject=true;
              //  Toast.makeText(getActivity(), searchTerm +" Searched", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onResultClick(SearchResult result){
                search_result= String.valueOf(result);
                TVtypesearch.setText(search_result);
                search.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onSearchCleared() {

            }

        });
        TVtypesearch=view.findViewById(R.id.type_search);
        subject_grade_spinner();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        TVtypesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.clearSearchable();
                search.clearResults();
                subject_spinner("http://twotr.com:4040/api/userinfo/basic/profile");

                search.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);

            }
        });

        ETgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                        .title("Grade Level") //setting title for dialog
                        .titleSize(22)
                        .positiveText("Done")
                        .negativeText("Cancel")
                        .multiSelectList(ASgrade) // the multi select model list with ids and name
                        .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                            @Override
                            public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                                aryGrade=selectedNames;
                                //will return list of selected
                                List<String> mStrings = new ArrayList<String>();
                                for (int i = 0; i < selectedIds.size(); i++) {
                                    ETgrade.setText(dataString);

//                                    Toast.makeText(Profile_Edit_Personal.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
//                                            "Selected Names : " + selectedNames.get(i) + "\n" +
//                                            "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancel() {
                                //  Log.d(TAG,"Dialog cancelled");
                            }


                        });

                multiSelectDialog.show(getFragmentManager(), "multiSelectDialog");

            }
        });


        Boneonone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boneonone.setBackgroundResource(R.drawable.tab_button_selected);
                Bgroup.setBackgroundResource(R.drawable.tab_button_unselected);
                Boneonone.setTextColor(getResources().getColor(R.color.colorwhite));
                Bgroup.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
     ETnofstudents.setVisibility(View.GONE);
                ETtotalone.setVisibility(View.VISIBLE);
                ETtotalamount.setVisibility(View.GONE);
                ETminamount.setVisibility(View.GONE);
                TVaddsched.setVisibility(View.VISIBLE);
                TVaddmap.setVisibility(View.VISIBLE);
                ETshortins.setVisibility(View.VISIBLE);
                ETgrade.setVisibility(View.VISIBLE);


            }
        });

        Bgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeofstudteach="group";
                Bgroup.setBackgroundResource(R.drawable.tab_button_selected);
                Boneonone.setBackgroundResource(R.drawable.tab_button_unselected);
                Bgroup.setTextColor(getResources().getColor(R.color.colorwhite));
                Boneonone.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                ETnofstudents.setVisibility(View.VISIBLE);
                ETtotalone.setVisibility(View.INVISIBLE);
                ETtotalamount.setVisibility(View.VISIBLE);
                ETminamount.setVisibility(View.VISIBLE);
                TVaddsched.setVisibility(View.VISIBLE);
                TVaddmap.setVisibility(View.VISIBLE);
                ETshortins.setVisibility(View.VISIBLE);
                ETgrade.setVisibility(View.VISIBLE);

            }
        });


        IVaddsch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String genrateach= UUID.randomUUID().toString();
                new SpotlightView.Builder(getActivity())
                        .introAnimationDuration(300)
                        //.enableRevealAnimation(isRevealEnabled)
                        .performClick(true)
                        .fadeinTextDuration(300)
                        .headingTvColor(Color.parseColor("#eb273f"))
                        .headingTvSize(32)
                        .headingTvText("Add Schedule")
                        .subHeadingTvColor(Color.parseColor("#ffffff"))
                        .subHeadingTvSize(16)
                        .subHeadingTvText("Add your Available Schedule time\nthis Will been shown to Users.")
                        .maskColor(Color.parseColor("#dc000000"))
                        .target(TVaddsched)
                        .lineAnimDuration(300)
                        .lineAndArcColor(Color.parseColor("#eb273f"))
                        .dismissOnTouch(true)
                        .dismissOnBackPress(true)
                        .enableDismissAfterShown(true)
                        .usageId(genrateach)
                        .show();

            }
        });

        IVaddmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String genrate= UUID.randomUUID().toString();
                new SpotlightView.Builder(getActivity())
                        .introAnimationDuration(300)
                        //.enableRevealAnimation(isRevealEnabled)
                        .performClick(true)
                        .fadeinTextDuration(300)
                        .headingTvColor(Color.parseColor("#eb273f"))
                        .headingTvSize(32)
                        .headingTvText("Add Map")
                        .subHeadingTvColor(Color.parseColor("#ffffff"))
                        .subHeadingTvSize(16)
                        .subHeadingTvText("To add location of your\n class going to add.")
                        .maskColor(Color.parseColor("#dc000000"))
                        .target(TVaddmap)
                        .lineAnimDuration(300)
                        .lineAndArcColor(Color.parseColor("#eb273f"))
                        .dismissOnTouch(true)
                        .dismissOnBackPress(true)
                        .enableDismissAfterShown(true)
                        .usageId(genrate)
                        .show();
            }
        });
        TVaddmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Addmaptutor.class);
                startActivity(intent);
            }
        });

        TVaddsched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ssubject_name=TVtypesearch.getText().toString();

                if (Ssubject_name.isEmpty())
                {
                    Toast.makeText(getActivity(), "Type Subject Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), ScheduleStart.class);
                    intent.putExtra("sub_name",Ssubject_name);
                    startActivity(intent);
                }
            }
        });

        add_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ssubjectname=TVtypesearch.getText().toString();
                String Sgroup=typeofstudteach;
                String Sprice=ETtotalone.getText().toString();
String Snoofstudents=ETnofstudents.getText().toString();
if (Snoofstudents.isEmpty())
{
    Snoofstudents="1";
}

String Sshortdesc=ETshortins.getText().toString();

                TinyDB tinydb = new TinyDB(getContext());
                starttimesched=   tinydb.getListString("starttime");
                endtimesched=  tinydb.getListString("endtime");
          String lati= tinydb.getString("latitude");
             String longi=tinydb.getString("longitude");


                tutor_create(Ssubjectname,Sgroup,Sprice,Snoofstudents,Sshortdesc,lati,longi);

            }
        });


        return  view;
    }



    public void tutor_create(String ssubjectname, String sgroup, String sprice, String snoofstudents, String sshortdesc, String lati, String longi)
    {
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            JSONObject jsonBody = new JSONObject();


            JSONArray array2=new JSONArray(aryGrade);
            //array2.put(Sgrade);


            JSONObject jsonObjectall=new JSONObject();
            jsonObjectall.put("lat",lati);
            jsonObjectall.put("lng",longi);

            JSONArray startendarray=new JSONArray();
            for(int i=0;i<starttimesched.size();i++){
                JSONObject obj=new JSONObject();
                try {
                    obj.put("start",starttimesched.get(i));
                    obj.put("end",endtimesched.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startendarray.put(obj);
            }

            jsonBody.put("location",jsonObjectall);
            jsonBody.put("schedules",startendarray);
            jsonBody.put("gradeLevel",array2);
            jsonBody.put("type",sgroup);
            jsonBody.put("isUserSubject",Bisusersubject);
            jsonBody.put("price",sprice);
            jsonBody.put("studentsCount",snoofstudents);
            jsonBody.put("description",sshortdesc);
            jsonBody.put("minPrice","50");
            jsonBody.put(  "subjectId",Ssubjectid);


            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Global_url_twotr.Profile_subject_create_class, new Response.Listener<String>() {
                public void onResponse(String response) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Subject Added SuccessFully")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

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

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");

                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }




            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void subject_grade_spinner() {


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Global_url_twotr.Profile_subject_grade_spin, new Response.Listener<String>() {

            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);


                    JSONArray jsonArray1 = jObj.getJSONArray("gradeLevel");

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        if(jsonArray1.length()==0)
                        {
                            ETgrade.setEnabled(false);
                            ETgrade.setText(String.valueOf(jsonArray1.get(i)));
                        }
                        else {
                            ASgrade.add(new MultiSelectModel(i,String.valueOf(jsonArray1.get(i))));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    public void subject_spinner(String myapi) {


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET,myapi , new Response.Listener<String>() {

            public void onResponse(String response) {
                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("subjects");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);

                        String name = jsonobject.getString("title");

                        SearchResult option = new SearchResult(name, getResources().getDrawable(R.drawable.ic_local_library_black_24dp));
                        search.addSearchable(option);

//                        // String id = jsonobject.getString("_id");
//
//                        subject_name.add(new MultiSelectModel(i,name));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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




    public void user_subject(String usersubject)
    {

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            JSONObject jsonObjectall=new JSONObject();
            jsonObjectall.put("title",usersubject);

            final String requestBody = jsonObjectall.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Global_url_twotr.Profile_subject_create, new Response.Listener<String>() {

                public void onResponse(String response) {
                    JSONObject jObj = null;
                    try {
                        jObj = new JSONObject(response);
                        String title = jObj.getString("title");
                        String createdBy = jObj.getString("createdBy");
                        String id = jObj.getString("_id");
                        String createdAt = jObj.getString("createdAt");
                        String isApproved = jObj.getString("isApproved");
                        Ssubjectid=id;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Server Error")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }
            })
            {
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
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");

                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







}