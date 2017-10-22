package com.agnt45.nearme;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewEventFragment extends Fragment {
    private DocumentReference documentReference ;
    private View mview;
    private TextInputLayout eventname;
    private TextInputLayout eventdesc;
    private TextView location,datePicker,timePicker;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private String Date,Time,EventName,EventDescription;
    private List<String> Location;
    private Button UplaoadData,UploadImage;

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    int PLACE_PICKER_REQUEST=1;
    private Uri downloadurl;

    public AddNewEventFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mview = inflater.inflate(R.layout.fragment_add_new_event, container, false);
        return mview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = mview.findViewById(R.id.event_date);
        timePicker = mview.findViewById(R.id.event_time);
        eventname = mview.findViewById(R.id.event_name);
        eventdesc = mview.findViewById(R.id.event_desc);
        location = mview.findViewById(R.id.event_location);
        UplaoadData = mview.findViewById(R.id.eventadd);
        UploadImage = mview.findViewById(R.id.eventpic);
        Location = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        documentReference = FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid());
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent =builder.build(getActivity());
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int Date = calendar.get(Calendar.DAY_OF_MONTH);
                int Month = calendar.get(Calendar.MONTH);
                int Year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,onDateSetListener,Year,Month,Date);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int Hour = calendar.get(Calendar.HOUR_OF_DAY);
                int Minutes = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),onTimeSetListener,Hour,Minutes,true);
                timePickerDialog.show();
            }
        });
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timePicker.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
                Time =timePicker.getText().toString();
            }
        };

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePicker.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
                Date = datePicker.getText().toString();
            }
        };
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(getActivity());
            }
        });
        UplaoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              EventName = eventname.getEditText().getText().toString();
              EventDescription = eventdesc.getEditText().getText().toString();
              if(EventName.isEmpty()||EventDescription.isEmpty()||Date.isEmpty()||Time.isEmpty()||Location.isEmpty()){
                  Toast.makeText(getActivity(),"Please Fill All The Feilds",Toast.LENGTH_LONG).show();
                  return;
              }
              else{
                    Map<String,Object> EventMap =new HashMap<String, Object>();
                    EventMap.put("Event_Name:",EventName);
                    EventMap.put("Event_Description:",EventDescription);
                    EventMap.put("Event_Date:",Date);
                    EventMap.put("Event_Time:",Time);
                    EventMap.put("Event_Location:",Location);
                    EventMap.put("Event_Picture",downloadurl.toString());

                    documentReference.collection("Myevents").add(EventMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Faliure",Toast.LENGTH_LONG).show();
                        }
                    });
              }



            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(getActivity(),data);
                String place_add = String.format(" %s",place.getAddress());
                location.setText(place_add);

                Location.add(location.getText().toString());

                Location.add(place.getName().toString());
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    progressDialog.setTitle("Uploading Image..");
                    progressDialog.setMessage("Image selected is being Uploaded please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().
                            child("Users").child("MyEvents").child(eventdesc.getEditText().getText().toString());
                    storageReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            downloadurl = taskSnapshot.getDownloadUrl();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.hide();
                        }
                    });

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }

        }
    }
}
