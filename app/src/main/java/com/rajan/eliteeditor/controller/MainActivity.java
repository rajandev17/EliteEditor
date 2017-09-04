package com.rajan.eliteeditor.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rajan.eliteeditor.Elite.EliteEditor;
import com.rajan.eliteeditor.Elite.Parser;
import com.rajan.eliteeditor.R;
import com.rajan.eliteeditor.storage.Cache;
import com.rajan.eliteeditor.storage.EliteDatabase;


public class MainActivity extends Activity {
    private EliteEditor editor;
    private EliteDatabase database;
    private Cache cache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editor = findViewById(R.id.editor);
        database = EliteDatabase.getInstance(this);
        cache = database.editorData().getCachedData();
        if(cache != null && !TextUtils.isEmpty(cache.getContent()))
        editor.setText(new SpannableStringBuilder(Parser.fromHtml(cache.getContent())));
    }

    public void onBoldClick(View v) {
        editor.modifyBold();
    }

    public void onItalicClick(View v) {
        editor.modifyItalic();
    }

    public void onUnderlineClick(View v) {
        editor.modifyUnderline();
    }

    public void onStrikeClicked(View v) {
        editor.modifyStrike();
    }

    public void onBulletClick(View v) {
        if(editor.hasQuote()) {
            Toast.makeText(this, "Quote and Bullet Can't be on Same line", Toast.LENGTH_SHORT).show();
            return;
        }
        editor.modifyBullet();
    }

    public void onQuoteClick(View v) {
        if(editor.hasBullet()) {
            Toast.makeText(this, "Quote and Bullet Can't be on Same line", Toast.LENGTH_SHORT).show();
            return;
        }
        editor.modifyQuote();
    }

    public void onLinkClick(View v) {
        updateLink();
    }

    public void onClearClick(View view) {
        editor.invalidateStyle();
    }

    public void onSaveClick(View view){
        String content = Parser.toHtml(editor.getEditableText());
        if(cache == null)
            cache = new Cache();
        cache.setContent(content);
        database.editorData().insertOrUpdateCache(cache);
        Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();
    }

    public void onSubmitClick(View view){
        Intent webViewIntent = new Intent(this,OutputActivity.class);
        webViewIntent.putExtra("html",Parser.toHtml(editor.getEditableText()));
        startActivity(webViewIntent);
    }

    private void updateLink() {
        View view = View.inflate(this,R.layout.link_box, null);
        final EditText urlEdt = view.findViewById(R.id.url);
        urlEdt.setText(editor.getSelectedLink());
        new AlertDialog.Builder(this)
                .setCancelable(false).setView(view)
                .setTitle("Modify link")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String link = urlEdt.getText().toString().trim();
                        if (TextUtils.isEmpty(link)) {
                            return;
                        }

                        editor.modifyLink(link);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("unlink", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.modifyLink(null);
            }
        }).create().show();
    }

}
