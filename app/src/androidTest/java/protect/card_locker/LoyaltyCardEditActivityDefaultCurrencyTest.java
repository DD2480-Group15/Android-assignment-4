package protect.card_locker;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;


import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

@RunWith(AndroidJUnit4.class)
public class LoyaltyCardEditActivityDefaultCurrencyTest {

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    @Test
    public void defaultCurrencyPreselected() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.settings_key_default_currency);
        prefs.edit().putString(key, "USD").commit();

        try (ActivityScenario<LoyaltyCardEditActivity> scenario = ActivityScenario.launch(LoyaltyCardEditActivity.class)) {
            onView(withText(R.string.options)).perform(click());
            onView(withId(R.id.balanceCurrencyField))
                .check(matches(withText("$")));
        }
    }

    @Test
    public void manualCurrencyPersistsAfterSave() {
        // Setup preferred currency is USD ($) (before app starts)
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.settings_key_default_currency);
        prefs.edit().putString(key, "USD").commit();

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
        
            // 1 click + to add new card (fabAdd)
            onView(withId(R.id.fabAdd)).perform(click());

            // 2 handle camera stuff
            // try-catch if it does not come up
            try {
                onView(withText("Got It")).perform(click());
            } catch (Exception e) { /* already clicked or never showed */ }

            // 3 click more options
            onView(withText("More options")).perform(click());

            // 4 click on text
            onView(withText("Add a card with no barcode")).perform(click());

            // 5 click on edit and type
            onView(withClassName(endsWith("EditText"))).perform(typeText("123"), closeSoftKeyboard());
            onView(withText("OK")).perform(click());

            // 6 fill in name
            onView(withId(R.id.storeNameEdit)).perform(typeText("mr no hands"), closeSoftKeyboard());
            
            // 7 click options
            onView(withText(R.string.options)).perform(click());

            // 8 check preferred curr is correct
            onView(withId(R.id.balanceCurrencyField))
                .check(matches(withText("$")));

            // 9 change curr
            onView(withId(R.id.balanceCurrencyField)).perform(replaceText("£"), closeSoftKeyboard());

            // 10 save (fabSave)
            onView(withId(R.id.fabSave)).perform(click());

            // 11 find card in list and open
            onView(withText("mr no hands")).perform(click());

            // 12 click edit
            onView(withId(R.id.fabEdit)).perform(click());

            // 13 click options
            onView(withText(R.string.options)).perform(click());

            // 14 verify curr is £ and not default $
            onView(withId(R.id.balanceCurrencyField))
                .check(matches(withText("£")));
        }
    }

}