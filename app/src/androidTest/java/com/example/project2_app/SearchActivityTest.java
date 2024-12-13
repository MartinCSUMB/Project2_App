package com.example.project2_app;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import junit.framework.TestCase;

public class SearchActivityTest extends TestCase {

    Context context = null;
    Intent intent = null;

    public void setUp() throws Exception {
        super.setUp();
        context = ApplicationProvider.getApplicationContext();
        intent = SearchActivity.searchIntentFactory(context);
    }

    public void tearDown() throws Exception {
        context = null;
        intent = null;
    }



    public void testSearchIntentFactory() {

        assertNotNull(intent);
        assertEquals(SearchActivity.class.getName(), intent.getComponent().getClassName());
    }
}