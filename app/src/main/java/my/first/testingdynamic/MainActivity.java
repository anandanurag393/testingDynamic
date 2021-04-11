package my.first.testingdynamic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private LinearLayout linearlayout ;
    private ScrollView scrollview;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public String name0 = "notgiven";

    public String url;








    final int N = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearlayout = findViewById(R.id.linearLayout);




        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);


        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        //name0 = loadNote();


        //Toast.makeText(this, name0, Toast.LENGTH_SHORT).show();

        db.collection("contests")
                .orderBy("startTime" , Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                //Toast.makeText(MainActivity.this, document.getString("name"), Toast.LENGTH_SHORT).show();

                                String platform = document.getString("platform");

                                final TextView rowTextView = new TextView(MainActivity.this);

                                final RelativeLayout relativeLayout = new RelativeLayout(MainActivity.this);
                                //textView background

                                GradientDrawable shape =  new GradientDrawable();
                                shape.setCornerRadius( 30 );
                                shape.setColor(Color.rgb(253, 170, 157));





                                // set some properties of rowTextView or something

                                rowTextView.setTextSize(30);

                                //set text of textview from the document
                                rowTextView.setText(document.getString("name"));


                                //textView parameters
                                rowTextView.setLayoutParams(params);

                                rowTextView.setHeight(500);

                                rowTextView.setPadding(50,30,30,50);

                                rowTextView.setBackground(shape);




                                //parameters for textview

                                RelativeLayout.LayoutParams params12 = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params12.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                params12.leftMargin = 50;
                                params12.rightMargin =50;
                                params12.topMargin = 50;
                                //addTextView to relative layout

                                relativeLayout.addView(rowTextView,params12);


                                //textView for date and time

                                //day date and time for text view
                                Timestamp startTime = document.getTimestamp("startTime");
                                Date date = startTime.toDate();

                                String tempDate = date.toString();
                                String dayDateTime = tempDate.substring(0,16);
                                //Toast.makeText(MainActivity.this,dayDateTime, Toast.LENGTH_SHORT).show();

                                final TextView dateTextView = new TextView(MainActivity.this);

                                //setting text
                                dateTextView.setText(dayDateTime);

                                //setting on long press
                                dateTextView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        Toast.makeText(MainActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });

                                dateTextView.setPadding(50,30,30,50);


                                RelativeLayout.LayoutParams paramsdateText = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                paramsdateText.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                paramsdateText.leftMargin = 50;
                                paramsdateText.rightMargin =50;
                                paramsdateText.topMargin = 160;

                                //add to relative layout
                                relativeLayout.addView(dateTextView,paramsdateText);


                                //alarm icon

                                ImageView alarm = new ImageView(MainActivity.this);

                                RelativeLayout.LayoutParams paramsAlarm = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                paramsAlarm.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                                paramsAlarm.leftMargin = 100;
                                paramsAlarm.rightMargin = 100;
                                paramsAlarm.topMargin =100;
                                paramsAlarm.height =75;
                                paramsAlarm.width =75;

                                alarm.setImageResource(R.drawable.calendaricon);

                                //set reminder OnClick

                                String title = document.getString("name");
                                Calendar c = Calendar.getInstance();
                                c.setTime(date);


                                alarm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_INSERT);
                                        intent.setData(CalendarContract.Events.CONTENT_URI);
                                        intent.putExtra(CalendarContract.Events.TITLE ,title);
                                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,c.getTimeInMillis());

                                        if(intent.resolveActivity(getPackageManager())!= null)
                                        {
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this, "Calendar App not found", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                                //setLongClick on clock

                                alarm.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        Toast.makeText(MainActivity.this, "Set Reminder "  , Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });


                                //add to relative layout

                                relativeLayout.addView(alarm,paramsAlarm);





                                //HYPERLINK_BUTTON

                                //code to add dynamic button


                                ImageButton btn = new ImageButton(MainActivity.this);
                                btn.setLayoutParams(params1);

                                //button alignment
                                RelativeLayout.LayoutParams params123 = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params123.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                                params123.leftMargin = 107;
                                params123.rightMargin = 100;
                                params123.topMargin =450;
                                params123.height =75;
                                params123.width =75;

                                //long pressing the button

                                btn.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        Toast.makeText(MainActivity.this, "link to " +  platform +" contest", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });

                                //scaling of button

                                btn.setScaleType(ImageView.ScaleType.FIT_CENTER);



                                //add image to button

                                btn.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.chrome));
                                btn.getLayoutParams().height =100;
                                btn.getLayoutParams().width = 50;

                                //open URL from button
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openUrl(document.getString("link"));

                                    }
                                });

                                //linearlayout.addView(btn,params123);

                                relativeLayout.addView(btn,params123);

                                //HYPERLINK_BUTTON_ENDS



                                //platform based customization

                                String p1 = "hackerrank";
                                String p2 = "codechef";
                                String p3 = "codeforces";
                                String p4 = "google";

                                if(p1.equals(platform) ){
                                    shape.setColor(Color.rgb(119, 221, 119));
                                    btn.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.hackerrank));
                                }

                                else if(p2.equals(platform)){
                                    shape.setColor(Color.rgb(131, 105, 83));
                                    btn.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.chef));
                                }

                                else if(p3.equals(platform)){
                                    shape.setColor(Color.rgb(119, 158, 203));
                                    btn.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.codeforces));
                                }

                                else if(p4.equals(platform)){
                                    shape.setColor(Color.rgb(	253, 170, 157));
                                    btn.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.googlei));
                                }

                                //textView for duration

                                String duration = document.getString("duration");

                                TextView timeRemaining = new TextView(MainActivity.this);
                                timeRemaining.setText(duration);
                                timeRemaining.setTextSize(16);

                                RelativeLayout.LayoutParams params1234 = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                params1234.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                params1234.leftMargin = 200;
                                params1234.rightMargin = 100;
                                params1234.topMargin =455;
                                params1234.height =75;

                                relativeLayout.addView(timeRemaining,params1234);

                                //imageview Clock icon

                                ImageView clock = new ImageView(MainActivity.this);

                                RelativeLayout.LayoutParams paramsClock = new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                paramsClock.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                paramsClock.leftMargin = 100;
                                paramsClock.rightMargin = 100;
                                paramsClock.topMargin =450;
                                paramsClock.height =75;
                                paramsClock.width =75;

                                clock.setImageResource(R.drawable.clock);

                                //setLongClick on clock

                                clock.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        Toast.makeText(MainActivity.this, "Duration " + duration , Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });

                                relativeLayout.addView(clock,paramsClock);




                                // add relative layout to linear layout

                                Timestamp currentTime = Timestamp.now();
                                long currentTimeS = currentTime.getSeconds();
                                Timestamp endTime = Timestamp.now();
                                endTime = document.getTimestamp("endTime");


                                long difference = Timestamp.now().getSeconds() - endTime.getSeconds();

                                long  endTimeS = endTime.getSeconds();

                                int toBeAdded = 1;



                                if(difference>0){
                                   toBeAdded =0;
                                   document.getReference().delete()
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {

                                               }
                                           })
                                           .addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {

                                               }
                                           });
                                }

                                if(toBeAdded ==1){
                                    linearlayout.addView(relativeLayout);
                                }

                            }

                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });





/*        for (int i = 1; i < N; i++) {
            // create a new textview
            final TextView rowTextView = new TextView(this);

            final RelativeLayout relativeLayout = new RelativeLayout(this);

            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 30 );

            // set some properties of rowTextView or something

            rowTextView.setTextSize(30);

            //set text of textview from the document

            String address = "contests/" +i;

            DocumentReference noteRef = db.document(address);

            noteRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String name1 = documentSnapshot.getString("name");
                            //Toast.makeText(MainActivity.this,documentSnapshot.getString("name"), Toast.LENGTH_SHORT).show();
                            rowTextView.setText(name1 +"\n");
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Eroor!", Toast.LENGTH_SHORT).show();
                        }
                    });


            //



            rowTextView.setLayoutParams(params);

            rowTextView.setHeight(500);

            shape.setAlpha(255);

            rowTextView.setPadding(50,30,30,220);

            if(i==0){
                shape.setColor(Color.rgb(253, 170, 157));
            }

            else if(i<=3){
                shape.setColor(Color.rgb(255, 212, 194));
            }

            else if(i<=5){
                shape.setColor(Color.rgb(175, 227, 206));
            }

            else
            {
                shape.setColor(Color.rgb(255, 229, 209));
            }




            //rowTextView.setBackgroundColor(Color.rgb(15*i,0,0));

            rowTextView.setBackground(shape);

            // add the textview to the linearlayout



            //code to add dynamic button

            int button_right = rowTextView.getRight();
            int button_bottom = rowTextView.getBottom();

            ImageButton btn = new ImageButton(this);
            btn.setLayoutParams(params1);


           // btn.setImageResource(R.drawable.hackerrank);
            //btn.setScaleType(ImageView.ScaleType.FIT_CENTER);



            RelativeLayout.LayoutParams params12 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params12.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params12.leftMargin = 50;
            params12.rightMargin =50;
            params12.bottomMargin=100;

           // rowTextView.setId(R.id.);

            relativeLayout.addView(rowTextView,params12);



            //linearlayout.addView(relativeLayout);


            //button alignment
            RelativeLayout.LayoutParams params123 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params123.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params123.leftMargin = 107;
            params123.rightMargin = 100;
            params123.topMargin =400;
            params123.height =75;
            params123.width =75;

            //scaling of button

            btn.setScaleType(ImageView.ScaleType.FIT_CENTER);



            //add image to button

            btn.setBackground(this.getResources().getDrawable(R.drawable.hackerrank));
            btn.getLayoutParams().height =100;
            btn.getLayoutParams().width = 50;

            //linearlayout.addView(btn,params123);

            relativeLayout.addView(btn,params123);


            //textView for time remaining

            TextView timeRemaining = new TextView(this);
            timeRemaining.setText("2 hours");
            timeRemaining.setTextSize(16);

            RelativeLayout.LayoutParams params1234 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params1234.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params1234.leftMargin = 200;
            params1234.rightMargin = 100;
            params1234.topMargin =410;
            params1234.height =75;

            relativeLayout.addView(timeRemaining,params1234);

            //imageview Clock icon

            ImageView clock = new ImageView(this);

            RelativeLayout.LayoutParams paramsClock = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsClock.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsClock.leftMargin = 100;
            paramsClock.rightMargin = 100;
            paramsClock.topMargin =400;
            paramsClock.height =80;
            paramsClock.width =80;

            clock.setImageResource(R.drawable.clock);

            relativeLayout.addView(clock,paramsClock);



            //Imageview for difficultylevel

            ImageView difficulty = new ImageView(this);
            RelativeLayout.LayoutParams paramsdiff = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsdiff.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsdiff.leftMargin = 100;
            paramsdiff.rightMargin = 100;
            paramsdiff.topMargin =50;
            paramsdiff.height =80;
            paramsdiff.width =80;

            difficulty.setImageResource(R.drawable.chef);
            relativeLayout.addView(difficulty,paramsdiff);

            //button for setting alarm

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openUrl("http://www.google.com");
                }
            });





            linearlayout.addView(relativeLayout);

            //linearlayout.addView(rowTextView);




        }




    }*/

   /* public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    */







    }
    public void openUrl(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    };

}