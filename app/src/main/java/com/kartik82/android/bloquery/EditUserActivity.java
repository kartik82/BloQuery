package com.kartik82.android.bloquery;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Random;

public class EditUserActivity extends AppCompatActivity {

    private String profile_user;
    private Firebase ref;
    private Firebase usersRef;
    private Cloudinary cloudinary;
    //private File photofile;
    Bitmap photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);

        ref = new Firebase(DatabaseConfig.FIREBASE_URL);
        profile_user = getIntent().getExtras().getString("user_id");
        usersRef = ref.child("users/" + profile_user);

        if (profile_user.equals(ref.getAuth().getUid())) {
            getSupportActionBar().setTitle("BloQuery - Edit Profile");
        }

        cloudinary = new Cloudinary(DatabaseConfig.CLOUDINARY_URL);

        final ImageView iv_edituser_photo = (ImageView) findViewById(R.id.iv_edituser_photo);
        final EditText et_edituser_fullname = (EditText) findViewById(R.id.et_edituser_fullname);
        final EditText et_edituser_description = (EditText) findViewById(R.id.et_edituser_description);
        final EditText et_edituser_displayname = (EditText) findViewById(R.id.et_edituser_displayname);
        final TextView tv_edituser_email_text = (TextView) findViewById(R.id.tv_edituser_email_text);
        final Button btn_edituser_updatephoto = (Button) findViewById(R.id.btn_edituser_updatephoto);
        final Button btn_edituser_deletephoto = (Button) findViewById(R.id.btn_edituser_deletephoto);
        final Button btn_edituser_saveprofile = (Button) findViewById(R.id.btn_edituser_saveprofile);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.with(getApplicationContext())
                        .load(cloudinary.url().transformation(new Transformation().width(300).height(300).radius("max").gravity("face").crop("thumb")).generate(user.getPhoto_url()))
                        .into(iv_edituser_photo);

                et_edituser_fullname.setText(user.getFull_name());
                et_edituser_description.setText(user.getDescription());
                et_edituser_displayname.setText(user.getDisplay_name());
                tv_edituser_email_text.setText(usersRef.getAuth().getProviderData().get("email").toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        if (btn_edituser_updatephoto != null) {
            btn_edituser_updatephoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditUserActivity.this);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {

                                //File photostorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                //photofile = new File(photostorage, (System.currentTimeMillis()) + ".jpg");

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                                startActivityForResult(intent, 0);
                            } else if (items[item].equals("Choose from Library")) {
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(
                                        Intent.createChooser(intent, "Select File"),
                                        1);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                }
            });
        }

        if (btn_edituser_deletephoto != null) {
            btn_edituser_deletephoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usersRef.child("photo_url").setValue("BloQuery/default-avatar.png");
                    Toast.makeText(getApplicationContext(), "Your photo has been removed.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btn_edituser_saveprofile != null) {
            btn_edituser_saveprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usersRef.child("full_name").setValue(et_edituser_fullname.getText().toString());
                    usersRef.child("description").setValue(et_edituser_description.getText().toString());
                    usersRef.child("display_name").setValue(et_edituser_displayname.getText().toString());

                    Toast.makeText(getApplicationContext(), "Your profile has been saved.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditUserActivity.this, UserActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("user_id", profile_user);
                    intent.putExtras(extras);
                    startActivity(intent);

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {

                try{
                    photo = (Bitmap) data.getExtras().get("data");
                }
                catch(NullPointerException ex){
                    //photo = BitmapFactory.decodeFile(photofile.getAbsolutePath());
                }

                String root = Environment.getExternalStorageDirectory().toString();
                File newDir = new File(root + "/saved_images");
                newDir.mkdirs();
                Random gen = new Random();
                int n = 10000;
                n = gen.nextInt(n);
                String fotoname = "photo-"+ n +".jpg";
                final File file = new File (newDir, fotoname);

                try {
                    //FileOutputStream out = new FileOutputStream(photofile);
                    FileOutputStream out = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.flush();
                    out.close();

                    cloudinary = new Cloudinary("cloudinary://364269363967144:T3wH6m8tALnK5_SMhyYRIvgs-UE@dbmohzfdq");

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Map cloudmap = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "BloQuery/"));
                                String cloudmapfilename = cloudmap.get("public_id").toString();

                                usersRef.child("photo_url").setValue(cloudmapfilename + ".png");


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    thread.start();


                    Toast.makeText(getApplicationContext(), "Your photo has been updated.", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {

                }



            } else if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                final String selectedImagePath = cursor.getString(column_index);

                cursor.close();

                cloudinary = new Cloudinary("cloudinary://364269363967144:T3wH6m8tALnK5_SMhyYRIvgs-UE@dbmohzfdq");

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Map cloudmap = cloudinary.uploader().upload(selectedImagePath, ObjectUtils.asMap("folder", "BloQuery/"));

                            String cloudmapfilename = cloudmap.get("public_id").toString();

                            usersRef.child("photo_url").setValue(cloudmapfilename + ".png");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();

                Toast.makeText(getApplicationContext(), "Your photo has been updated.", Toast.LENGTH_SHORT).show();

            }

        }

    }

}

