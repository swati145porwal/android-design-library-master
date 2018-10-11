package com.example.android.materialdesigncodelab;

/**
 * Created by SWATI on 10-04-2017.
 */


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.speech.tts.TextToSpeech;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;



public class File_ReaderActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private DrawerLayout mDrawerLayout;
    private TextToSpeech tts;
    public static String url="";
    public String m_Text;
    private static String location = Environment.getExternalStorageDirectory()
            + "/epubtempdir/";

    String str1 = " ";

    StringBuilder sb = new StringBuilder();
    Reader reader;
    private String linez = null, line, finalstr="";
    String ttspeak ;
    int i = 0;
    private WebView v1;
    Book eBook;
    public String unzipDir=location+"unzip1/";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.file_readeractivity);
        Bundle p= getIntent().getExtras();
        String path = p.getString("path");
        String location = Environment.getExternalStorageDirectory()
                + "/epubtempdir/";
        int pos = location.lastIndexOf('/');
        tts = new TextToSpeech(this, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        // TODO: handle navigation

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });




        v1 = (MyWebView) findViewById(R.id.webView);
        v1.getSettings().setJavaScriptEnabled(true);
        v1.getSettings().setLoadsImagesAutomatically(true);
        v1.setWebViewClient(new CustomWebClient(getApplicationContext()));


        try {
            InputStream inputStream = new FileInputStream(path);
             eBook = new EpubReader().readEpub(inputStream);




            Spine spine = eBook.getSpine();
            Resource res;
            List<SpineReference> spineList = spine.getSpineReferences() ;

            int count = spineList.size();
            int start = 0;

            StringBuilder string = new StringBuilder();
            for (int i = start; count > i; i = i +1) {
                res = spine.getResource(i);


               try {
                    InputStream is = res.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    try {
                        while ((line = reader.readLine()) != null) {
                            linez =   string.append(line + "\n").toString();
                        }

                    } catch (IOException e) {e.printStackTrace();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /*String data=null;
            try{
                data=new String(eBook.getContents().get(2).getData());
            }catch (IOException e){
                e.printStackTrace();
            }*/

            url= unzipDir+getPathOPF(unzipDir);
            v1.clearCache(false);
            v1.getSettings().setUseWideViewPort(true);
            v1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            v1.setVerticalScrollBarEnabled(false);
            v1.setHorizontalScrollBarEnabled(false);
            v1.getSettings();
            v1.setBackgroundColor(0);
            v1.setBackgroundResource(R.drawable.pic7);
         /*   v1.addJavascriptInterface(new IJavascriptHandler(this),"Android");
            String javaScriptToExtractText = "function getAllTextInColumn(left,top,width,height){"
                    +   "if(document.caretRangeFromPoint){"
                    +   "var caretRangeStart = document.caretRangeFromPoint(left, top);"
                    +   "var caretRangeEnd = document.caretRangeFromPoint(left+width-1, top+height-1);"
                    +   "} else {"
                    +   "return null;"
                    +   "}"
                    +   "if(caretRangeStart == null || caretRangeEnd == null) return null;"
                    +   "var range = document.createRange();"
                    +   "range.setStart(caretRangeStart.startContainer, caretRangeStart.startOffset);"
                    +   "range.setEnd(caretRangeEnd.endContainer, caretRangeEnd.endOffset);"
                    +   "return range.toString();};";
            String javaScriptFunctionCall = "getAllTextInColumn(0,0,100,100)";

            v1.loadUrl("javascript:"+ javaScriptToExtractText);

            v1.loadUrl("javascript:window.Android.processContent("+javaScriptFunctionCall+");");*/


            v1.loadDataWithBaseURL("file://"+url, linez, "text/html", "utf-8",null);





        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }



   /* final class IJavascriptHandler {

        Context mContext;
        IJavascriptHandler(Context c) {
            mContext = c;
        }

        //API 17 and higher required you to add @JavascriptInterface as mandatory before your method.
        @JavascriptInterface
        public void processContent(String aContent)
        {
            //this method will be called from within the javascript method that you will write.
            final String content = aContent;
            ttspeak = content;
            Log.e("Content",content);
        }
    }*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_TOC) {
            return DisplayToc(item);
        }
       /* else if (id == R.id.fontbtn){
            return changeFont(item);
        }*/
        //else if(id== R.id.act_search) {
            //return search(item,);
        //}
        else if (id == R.id.ttsbtn){
            return textToSpeech(item);
        }
        else if (id == R.id.ariel){
            try {
                InputStream inputStream = getAssets().open("ariel.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == R.id.serif){
            try {
                InputStream inputStream = getAssets().open("serif.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == R.id.monospace){
            try {
                InputStream inputStream = getAssets().open("monospace.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (id == R.id.nty){
            try {
                InputStream inputStream = getAssets().open("ninety.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (id == R.id.hndrd){
            try {
                InputStream inputStream = getAssets().open("hndrd.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (id == R.id.ontwofv){
            try {
                InputStream inputStream = getAssets().open("onetwofive.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (id == R.id.onfv){
            try {
                InputStream inputStream = getAssets().open("onefive.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (id == R.id.two){
            try {
                InputStream inputStream = getAssets().open("two.css");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                v1.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (id == R.id.search){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Search Text");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    searchText(m_Text);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();


        }

        else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void searchText(String abc) {

        // WebView web=(WebView)findviewbyid(R.id.webview);
        v1.findAllAsync(abc);
        try {
            for (Method m : WebView.class.getDeclaredMethods()) {
                if (m.getName().equals("setFindIsUp")) {
                    m.setAccessible(true);
                    m.invoke((v1), true);
                    break;
                }
            }
        } catch (Exception ignored) {
        }

    }




    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean textToSpeech(MenuItem item) {


       /* v1.evaluateJavascript(
                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        Log.d("HTML", html);
                        ttspeak = html;
                        // code here
                    }
                });*/



       /* v1.evaluateJavascript("(function(){return window.document.body.outerHTML})();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        String textOnly = Jsoup.parse(html.toString()).text();
                        String text = textOnly.substring(40,3000);
                        if (tts != null) {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "end");
                            tts.speak(text, TextToSpeech.QUEUE_ADD, params);
                            Log.d("current", textOnly);

                        }
                    }
                });*/


        String textOnly = Jsoup.parse(linez.toString()).text();
        String text = textOnly.substring(40,3000);
        if (tts != null) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "end");
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
            Log.d("jsoup", textOnly);
            return true;
        }

        // String mText = linez;
      /*  String textOnly = Jsoup.parse(ttspeak.toString()).text();
        String text = textOnly.substring(40,3000);
        if (tts != null) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "end");
            tts.speak(text, TextToSpeech.QUEUE_ADD, params);
            Log.d("current",textOnly);

           /* if(linez==null)
                Log.d("linez","linez me gadbad h");
            else
                Log.d("linez",linez);*/
           /* return true;
        }*/



        return false;
    }


    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void onInit(int status) {
        // TODO Auto-generated method stub
        // TTS is successfully initialized
        if (status == TextToSpeech.SUCCESS) {
            // Setting speech language
            int result = tts.setLanguage(Locale.US);
            // If your device doesn't support language you set above
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Cook simple toast message with message
                Toast.makeText(getApplicationContext(), "Language not supported",
                        Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            }
            // Enable the button - It was disabled in main.xml (Go back and
            // Check it)

        }
    }

    public boolean DisplayToc(MenuItem item) {


        createTocFile();

        v1.loadUrl(tableOfContents());
        v1.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });
return true;
    }



    public void createTocFile() {

        Bundle p = getIntent().getExtras();
        String path = p.getString("path");

        try {
            InputStream inputStream = new FileInputStream(path);
            eBook = new EpubReader().readEpub(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        List<TOCReference> tmp;
        TableOfContents toc = eBook.getTableOfContents();
        String html = "<html><body><ul>";
        String pathOPF = null;
        try {
            pathOPF = getPathOPF(unzipDir);
            int pos = pathOPF.lastIndexOf('/');
            if (pos == -1) {
                pathOPF = "";
            } else {
                pathOPF = pathOPF.substring(0, pos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tmp = toc.getTocReferences();

        if (tmp.size() > 0) {
            String b = getString(R.string.tocReference);
            html += b;
            for (int i = 0; i < tmp.size(); i++) {
                //String path1 = "file://" + unzipDir + tmp.get(i).getCompleteHref();
                String path1 = "file://" + unzipDir + pathOPF + "/" + tmp.get(i).getCompleteHref();

                html += "<li>" + "<a href=\"" + path1 + "\">"
                        + tmp.get(i).getTitle() + "</a>" + "</li>";

                // pre-order traversal?
                List<TOCReference> children = tmp.get(i).getChildren();

                for (int j = 0; j < children.size(); j++)
                    html += r_createTocFile(children.get(j));

            }
        }

        String a = getString(R.string.tablebodyhtmlClose);
        html += a;

        // write down the html file
        String filePath = unzipDir + "Toc.html";
        try {
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file);
            fw.write(html);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String tableOfContents() {
        return "File://" + unzipDir + "Toc.html";
    }

    public String r_createTocFile(TOCReference e) {

        Bundle p = getIntent().getExtras();
        String path = p.getString("path");

        try {
            InputStream inputStream = new FileInputStream(path);
            eBook = new EpubReader().readEpub(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String pathOPF = null;
        try {
            pathOPF = getPathOPF(unzipDir);
            int pos = pathOPF.lastIndexOf('/');
            if (pos == -1) {
                pathOPF = "";
            } else {
                pathOPF = pathOPF.substring(0, pos);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String childrenPath = "file://" + unzipDir + pathOPF + "/" + e.getCompleteHref();

        String html = "<ul><li>" + "<a href=\"" + childrenPath + "\">"
                + e.getTitle() + "</a>" + "</li></ul>";

        List<TOCReference> children = e.getChildren();

        for (int j = 0; j < children.size(); j++)
            html += r_createTocFile(children.get(j));

        return html;
    }

    public boolean changeFont(MenuItem item) {


        injectCSS();
        // v2.loadDataWithBaseURL( "file:///android_asset/",linezs.toString(), "text/html", "utf-8",null);

    return true;
    }


    private void injectCSS() {
        try {
            InputStream inputStream = getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            v1.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String getPathOPF(String unzipDir) throws IOException {
        String pathOPF = "";


        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new FileReader( unzipDir
                    + "META-INF/container.xml"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = sb.toString();
        Document doc = Jsoup.parse(result, "", Parser.xmlParser());
        Element e = doc.getElementsByTag("rootfile").first();
        pathOPF=e.attr("full-path");



        return pathOPF;

    }




    }














