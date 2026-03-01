package protect.card_locker;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static android.os.Looper.getMainLooper;
import static org.robolectric.Shadows.shadowOf;

import androidx.preference.DropDownPreference;
import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.util.Arrays;
import java.util.List;

import protect.card_locker.preferences.SettingsActivity;

@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {

    @Test
    public void verifyPointsExistsAsOption(){
        ActivityController<SettingsActivity> controller = Robolectric.buildActivity(SettingsActivity.class);
        SettingsActivity activity = controller.setup().get();

        shadowOf(getMainLooper()).idle();

        PreferenceFragmentCompat fragment = (PreferenceFragmentCompat) activity.getSupportFragmentManager()
                .findFragmentById(R.id.settings_container);
        assertNotNull(fragment); //settings fragment exists

        DropDownPreference currencyPref = fragment.findPreference(activity.getString(R.string.settings_key_default_currency));
        assertNotNull(currencyPref); //currency preference exists

        List<CharSequence> entries = Arrays.asList(currencyPref.getEntries());
        assertTrue(entries.contains(activity.getString(R.string.points))); //entries contains points
    }
}