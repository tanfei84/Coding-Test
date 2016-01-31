package com.example.tf.fiboapp;

import android.test.AndroidTestCase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by TF on 1/31/2016.
 */
public class AdapterTest extends AndroidTestCase {
    private ArrayAdapter<Long> mAdapter;

    private Long one;
    private Long two;
    private Long three;

    public AdapterTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        ArrayList<Long> data = new ArrayList<Long>();

        one = (long) 99;
        two = (long) 972;
        three = (long) 3;
        data.add(one);
        data.add(two);
        data.add(three);

        mAdapter = new ArrayAdapter<Long> (getContext(), android.R.layout.simple_list_item_1, data);

        assertEquals(one, mAdapter.getItem(0));
        assertEquals(two, mAdapter.getItem(1));
        assertEquals(three, mAdapter.getItem(2));
        assertEquals("Count incorrect.", 3, mAdapter.getCount());

    }
}
