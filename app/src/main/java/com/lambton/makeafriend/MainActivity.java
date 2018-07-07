package com.lambton.makeafriend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.danielcswain.makeafriend.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Launcher/Main activity for the NearbyChat application.
 */
public class MainActivity extends AppCompatActivity{

    @BindView(R.id.username_field) EditText mUsernameField;
    @BindView(R.id.button_enter_chat) Button mEnterChatButton;

    public static final String SHARED_PREFS_FILE = "NearbyChatPreferences";
    public static final String USERNAME_KEY = "username";
    public static final String AVATAR_COLOUR_KEY = "avatar_colour";
    public static Context mainContext;
    public static SharedPreferences sharedPreferences;

    private static Integer sCurrentAvatarColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the viw and toolbar
       setContentView(R.layout.activity_main);
         ButterKnife.bind(this);

        // Get the main context
        mainContext = getApplicationContext();

        // Get any saved username and avatar colour from the shared preference file
        sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME_KEY, "");
        String avatarColour = sharedPreferences.getString(AVATAR_COLOUR_KEY, "");

        // Set the username field if the user had saved one previously
        if (!username.isEmpty() ){
            mUsernameField.setText(username);
            mUsernameField.setSelection(mUsernameField.getText().length());
        }

        // Set sCurrentAvatarColour to the default colour (currently md_pink_500
        sCurrentAvatarColour = ContextCompat.getColor(getApplicationContext(), R.color.md_pink_500);

    }


       @OnClick(R.id.button_enter_chat)
        public void onClickButtinEnterChat(View view) {
            // Get the username and sCurrentAvatarColour
            String username = mUsernameField.getText().toString();
            String avatarColour = Integer.toHexString(sCurrentAvatarColour);

            // If the username is empty prompt the user and don't enter the chat
            if (username.isEmpty() || username.equals("")){
                Snackbar.make(mEnterChatButton, getString(R.string.error_empty_username), Snackbar.LENGTH_SHORT).show();
            } else {
                // Ensure the avatarColour string starts with # prior to sending and saving
                if (!avatarColour.startsWith("#")){
                    avatarColour = "#" + avatarColour;
                }

                // Save the username and sCurrentAvatarColour in the shared preferences
                storeUsernameAndAvatarColour(username, avatarColour);

                // Enter the chat with the username and avatarColour sent to the ChatActivity
                Intent enterChatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                enterChatIntent.putExtra(USERNAME_KEY, username);
                enterChatIntent.putExtra(AVATAR_COLOUR_KEY, avatarColour);
                startActivity(enterChatIntent);
            }
        }

    /**
     * Store the username and avatarColour selections in the SharedPreference file to persist the user's choices.
     */
    private void storeUsernameAndAvatarColour(String username, String avatarColour){
        // Store the values in the SharedPreferences file
        sharedPreferences.edit().putString(USERNAME_KEY, username).apply();
        sharedPreferences.edit().putString(AVATAR_COLOUR_KEY, avatarColour).apply();
    }
}
