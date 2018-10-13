package developerkx.random_pswd_generator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static String defaultPasswordLengthKey = "default_password_length";
    int maxPasswordLength = 50;
    int defaultPasswordLength;
    TextView passwordTextView;
    EditText lengthEditText;
    SharedPreferences defaultSharedPreferences;
    PasswordGenerator passwordGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        lengthEditText = (EditText) findViewById(R.id.lengthEditText);

        defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        defaultPasswordLength = defaultSharedPreferences.getInt(defaultPasswordLengthKey, 6);
        lengthEditText.setText(String.valueOf(defaultPasswordLength));

        refreshPasswordGenerator();
        onRefreshPassword(null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshPasswordGenerator();
        onRefreshPassword(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            showSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRefreshPassword(View view) {
        int length;

        try {
            length = Integer.parseInt(lengthEditText.getText().toString());
        }
        catch (Exception e) {
            length = defaultPasswordLength;
        }

        int minPasswordLength = passwordGenerator.getMinLength();

        if (minPasswordLength <= length && length <= maxPasswordLength) {
            String newPassword = passwordGenerator.getRandomPassword(length);
            passwordTextView.setText(newPassword);

            if (length != defaultPasswordLength) {
                SharedPreferences.Editor editor = defaultSharedPreferences.edit();
                editor.putInt(defaultPasswordLengthKey, length);
                editor.apply();
                defaultPasswordLength = length;
            }
        }
        else {
            lengthEditText.setError(String.format("%d <= length <= %d", minPasswordLength, maxPasswordLength));
        }

    }

    private void refreshPasswordGenerator() {
        boolean useLowercaseLetters = defaultSharedPreferences.getBoolean("lowercase_letters_switch", true);
        boolean useUppercaseLetters = defaultSharedPreferences.getBoolean("uppercase_letters_switch", true);
        boolean useDigits = defaultSharedPreferences.getBoolean("digits_switch", true);
        boolean useSpecialSymbols = defaultSharedPreferences.getBoolean("special_symbols_switch", true);

        passwordGenerator = new PasswordGenerator(useLowercaseLetters, useUppercaseLetters, useDigits, useSpecialSymbols);
    }

    private void showSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
