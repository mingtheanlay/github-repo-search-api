/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.datafrominternet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
    }

    // TODO (2) Create a method called makeGithubSearchQuery
    void makeGithubSearchQuery() {
        // TODO (3) Within this method, build the URL with the text from the EditText and set the built URL to the TextView
        String userQueryInput = mSearchBoxEditText.getText().toString();
        URL githubQuery = NetworkUtils.buildUrl(userQueryInput);
        mUrlDisplayTextView.setText(githubQuery.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        String userQuery = mSearchBoxEditText.getText().toString();
        if (itemThatWasClickedId == R.id.action_search) {
            if(userQuery.matches("")) {
                // Toast: feedback to user action
                Toast toast = Toast.makeText(getApplicationContext(),"Please input Github repo in search box",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
                return false;
            } else {
                // TODO (5) Call makeGithubSearchQuery when the search menu item is clicked
                makeGithubSearchQuery();
                // Close keyboard
                mSearchBoxEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                return true;
            }
        } else if(itemThatWasClickedId == R.id.copy_to_clipboard) {
            if(userQuery.matches("")) {
                // Toast: feedback to user action
                Toast toast = Toast.makeText(getApplicationContext(),"Please input Github repo in search box",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
                return false;
            } else {
                // Query and display to user
                makeGithubSearchQuery();

                // Copy to user clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("URL", mUrlDisplayTextView.getText().toString());
                clipboard.setPrimaryClip(clip);

                // Message
                Toast toast = Toast.makeText(getApplicationContext(),"Copied to clipboard",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();

                // Close keyboard
                mSearchBoxEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                return true;
            }
        }  else if(itemThatWasClickedId == R.id.open_in_browser) {
            if(userQuery.matches("")) {
                // Toast: feedback to user action
                Toast toast = Toast.makeText(getApplicationContext(),"Please input Github repo in search box",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
                return false;
            } else {
                // Query and display to user
                makeGithubSearchQuery();

                // Intent view
                String url = mUrlDisplayTextView.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                // Close keyboard
                mSearchBoxEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                return true;
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
