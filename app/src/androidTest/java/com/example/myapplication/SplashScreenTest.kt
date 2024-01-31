package com.example.myapplication

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SplashScreen::class.java)

    @Test fun whenSplashScreenFinishesMainActivityStarts() {
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }
}