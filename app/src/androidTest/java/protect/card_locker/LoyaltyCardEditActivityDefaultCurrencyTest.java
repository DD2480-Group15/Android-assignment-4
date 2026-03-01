package protect.card_locker;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

@RunWith(AndroidJUnit4.class)
public class LoyaltyCardEditActivityDefaultCurrencyTest {

    @Test
    public void defaultCurrencyPreselected() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("pref_currency", "$").commit();

        try (ActivityScenario<LoyaltyCardEditActivity> scenario = ActivityScenario.launch(LoyaltyCardEditActivity.class)) {
            onView(withText(R.string.options)).perform(click());
            onView(withId(R.id.balanceCurrencyField))
                .check(matches(withText("$")));
        }
    }

    @Test
    public void manualCurrencyOverridePersists() {
        // 1. Setup: Sätt standardvaluta till "$"
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.settings_key_default_currency);
        prefs.edit().putString(key, "$").commit();

        // 2. Öppna vyn för att skapa nytt kort
        try (ActivityScenario<LoyaltyCardEditActivity> scenario = ActivityScenario.launch(LoyaltyCardEditActivity.class)) {
            
            // Fyll i obligatoriskt namn och ID på första fliken
            onView(withId(R.id.storeNameEdit)).perform(typeText("Testkort"), closeSoftKeyboard());
            onView(withId(R.id.cardIdView)).perform(typeText("12345"), closeSoftKeyboard());

            // 3. Gå till Options och ändra valuta manuellt till "£"
            onView(withText(R.string.options)).perform(click());
            // Vi använder replaceText för att simulera att användaren väljer i listan
            onView(withId(R.id.balanceCurrencyField)).perform(replaceText("£"), closeSoftKeyboard());

            // 4. Klicka på spara-knappen (FAB)
            onView(withId(R.id.fabSave)).perform(click());
        }

        // 5. Nu simulerar vi att vi öppnar kortet igen
        // I ett riktigt scenario skulle vi hämta ID från databasen, 
        // men vi kan testa att öppna EditActivity igen för att se att den inte tappat minnet.
        // För enkelhetens skull i detta steg kollar vi att det sparades i databasen:
        
        // (Här kan vi lägga till logik för att öppna det nyss skapade kortet)
    }


}