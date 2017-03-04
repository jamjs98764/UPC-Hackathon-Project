package com.example.marccaps.harassmentcontrol;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.marccaps.harassmentcontrol.Constant.Constants;
import com.example.marccaps.harassmentcontrol.Interfaces.Endpoint;

import java.util.Date;

import es.dmoral.toasty.Toasty;
import it.slyce.messaging.SlyceMessagingFragment;
import it.slyce.messaging.listeners.UserSendsMessageListener;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.marccaps.harassmentcontrol.Constant.Constants.BASE_URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static int n = 0;
    private ImageView mSendView;
    private EditText mEditMessage;

    private Endpoint apiService;


    private static Message getRandomMessage() {
        TextMessage textMessage = new TextMessage();
        textMessage.setText("Hey buddy");
        textMessage.setDate(new Date().getTime());
        textMessage.setAvatarUrl(Constants.STALKED_URL_IMAGE);
        textMessage.setUserId("LP");
        textMessage.setSource(MessageSource.EXTERNAL_USER);
        return textMessage;
    }

    SlyceMessagingFragment slyceMessagingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,mEditMessage.getText().toString());
                Call<Response> call = apiService.analizeMessage(mEditMessage.getText().toString());
                call.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        String statusCode = response.message();
                        if(statusCode.equals("0")) {
                            TextMessage checkedMessage = new TextMessage();
                            checkedMessage.setText(mEditMessage.getText().toString());
                            slyceMessagingFragment.addNewMessage(checkedMessage);
                            checkedMessage.setDate(new Date().getTime());
                            checkedMessage.setAvatarUrl(Constants.STALKED_URL_IMAGE);
                            checkedMessage.setUserId("LP");
                            checkedMessage.setSource(MessageSource.EXTERNAL_USER);
                        }
                        else {
                            Toasty.warning(getApplicationContext(), "Are ypu sure you want to send this?",
                                    Toast.LENGTH_LONG, true).show();

                            Log.d(TAG, statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        Log.d(TAG,t.getMessage());
                    }
                });
            }
        });

        slyceMessagingFragment.setOnSendMessageListener(new UserSendsMessageListener() {
            @Override
            public void onUserSendsTextMessage(String text) {
                slyceMessagingFragment.addNewMessage(getRandomMessage());
            }

            @Override
            public void onUserSendsMediaMessage(Uri imageUri) {
                Log.d("inf", "******************************** " + imageUri);
            }
        });

    }

    private void init() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiService = retrofit.create(Endpoint.class);

        mSendView = (ImageView) findViewById(R.id.slyce_messaging_image_view_send);
        mEditMessage = (EditText) findViewById(R.id.slyce_messaging_edit_text_entry_field);

        slyceMessagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.messaging_fragment);
        slyceMessagingFragment.setDefaultAvatarUrl(Constants.STALKER_URL_IMAGE);
        slyceMessagingFragment.setDefaultDisplayName("Matthew Page");
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo");

    }
}