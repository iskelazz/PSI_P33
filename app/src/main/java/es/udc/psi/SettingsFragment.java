package es.udc.psi;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(MainActivity.SHARED_PREFS_NAME);
        setPreferencesFromResource(R.xml.preferences, rootKey);

        EditTextPreference userPref = findPreference("user");
        if (userPref != null) {
            userPref.setOnPreferenceChangeListener((preference, newValue) -> {
                String newUser = (String) newValue;
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();
                editor.putString("user", newUser);
                editor.apply();
                return true;
            });
        }
    }
}