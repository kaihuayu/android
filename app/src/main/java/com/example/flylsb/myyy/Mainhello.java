package com.example.flylsb.myyy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.*;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Mainhello extends AppCompatActivity implements SpeechSynthesizerListener,AdapterView.OnItemClickListener{

    private ListView listView;
    private ArrayAdapter<String> arr_aAdapter;

    private static final String TAG = "MainHello";

    private SpeechSynthesizer mSpeechSynthesizer;//百度语音合成客户端
    private String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license_2016-04-05";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private static final String APP_ID = "1020736";//请更换为自己创建的应用
    private static final String API_KEY = "i4eAFTq8bbhaACCA2BWyfeSQCkAGlTvp";//请更换为自己创建的应用
    private static final String SECRET_KEY = "kwys8ShgYev9KT5CE8OSydhC35sCGwae";//请更换为自己创建的应用

    private Button btn_speak,page;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainhello);
        BdTextToSpeech.getInstance(this).speak("你好刘以沫小朋友我们开始学英语了，你是最棒的");
        initView();
        InputStream inputStream = getResources().openRawResource(R.raw.hello1);//R.raw.heelo 读取raw目录下的HELLO文本
        listView = (ListView)findViewById(R.id.ayyay_list_list);
        //  创建适配器对象时将数据加载到适配器里
        /**new ArrayAdapter<T>(context, textViewResourceId)
         * context--  上下文,一般为this
         * textViewResourceId-- 当前ListView加载的每一个列表项所对应的布局文件【这里采用系统默认的一个布局android.R.layout.simple_list_item_1】
         */
        //  2. 添加数据源到适配器
        //String[] arr_data = {"fanff", "fan", "tencent", "QQ"};// 创建的数据源
       // String[] arr_data = {"打招呼","Hello Nice To Meet You","Hello Nice To Meet You Too","How Are You","I'm Fine Thank You And You I'm Fine Too","询问敲门","Who Is That Boy He Is My Friend","Who Is That Girl She Is My Friend","敲门声","Come In Please Come In ","Nice To Meet You Again Nice To Meet You Too","What's Your Name","My name Is liu yi mo","How Old Are You ","Iam 7 Years Old","Who Is That Women? ","She Is Me English Teacher Miss Wang","Come In Please Come In ","Nice To Meet You Again Nice To Meet You Too","坐下聊天","What Do You Do In The Morning ","I go To The Children's Palace I Like Drawing","What Do You Do In The Morning?"," I stay At Home  I Watch Cartoons","What's This It's Little Dog ","It's Cute. What Color Is It?","It’S  White","Is This A Monkey? Yes"," It Is.  What Color Is It?","It’S  Yellow","Is This A Sheep? No"," It Is T. It's A Giraffe.","Do You Like Giraffe? Yes I Do","迟到","Who Is Not Here?","Sister Not Here","姐姐敲门","May I Come In Come In Please","I'm Sorry ","I'm Late It's Ok ","Don't Be Late Again Thank You","做游戏 老师：","Ok Please Be Quiet I Know You Take Some Toys. ","So  You Want To Play A Game?","How Many Toys Can You See Let Me Count","one two three four five six seven eight nine ten","I Have 9 Numbers. I Put Then In The Box. You Can Select One And That Number Toy Belongs To You.  Understand?  OK "," Let's Start","学生抽到数字后 拿玩具 说:The number is I have a I like it","打招呼","Hello Nice To Meet You Hello Nice To Meet You Too","你好，见到你很高兴，见到你我也很高兴。","How Are You	I'm Fine Thank You And You I'm Fine Too","你好吗？我很好，谢谢你，我是Fine Too。","询问敲门","询问敲门","Who Is That Boy He Is My Friend Who Is That Girl She Is My Friend","那个男孩是谁？他是我的朋友。那个女孩是谁？她是我的朋友。","敲门声","Come In Please Come In","请进，请进。","Nice To Meet You Again Nice To Meet You Too","很高兴再次见到你，见到你我也很高兴。","What's Your Name My name Is","你叫什么名字？我叫","How Old Are You Iam 7 Years Old","你多大了？我7岁了。","Who Is That Women? She Is Me English Teacher Miss Wang","那个女人是谁？她是我的英语老师王小姐。","Come In Please Come In Nice To Meet You Again Nice To Meet You Too","请进，很高兴再次见到你，很高兴认识你。","坐下聊天","What Do You Do In The Morning I go To The Children's Palace I Like Drawing","早上你做什么？我去少年宫我喜欢画画。","What Do You Do In The Morning? I stay At Home I Watch Cartoons","你早上干什么？我呆在家里看卡通片。","What's This It's Little Dog ","It's Cute. What Color Is It? It’S White","这是什么？它是小狗，它很可爱。它是什么颜色的？这里是White。","Is This A Monkey? Yes"," It Is. What Color Is It? It’S Yellow","这是一只猴子吗？是的，它是。它是什么颜色的？它是黄色的","Is This A Sheep? No"," It Is T. It's A Giraffe. Do You Like Giraffe? Yes I Do","这是绵羊吗？不，它是T。它是一只长颈鹿。你喜欢Giraffe吗？是的","","迟到","Who Is Not Here? Sister Not Here","谁不在这里？姐姐不在这里","","姐姐敲门","May I Come In Come In Please","我可以进来吗？","I'm Sorry I'm Late It's Ok Don't Be Late Again Thank You","对不起，我迟到了，没关系，请不要再迟到了，谢谢。","","做游戏 老师：","Ok Please Be Quiet I Know You Take Some Toys. So You Want To Play A Game?","好的，请安静，我知道你带了一些玩具。你想玩游戏吗？","How Many Toys Can You See Let Me Count 123456789 在反问老师","你能看到多少玩具让我数到123456789在反问老师","I Have 9 Numbers. I Put Then In The Box. You Can Select One And That Number Toy Belongs To You. Understand? OK "," Let's Start","我有9个数字。我把它放进盒子里。你可以选择一个，那个数字玩具就属于你。明白了吗？好吧，我们开始吧。","学生抽到数字后 拿玩具 说： The number is I have a I like it","学生抽到数字后拿玩具说：号码是  我有一个我喜欢它"};

        //arr_aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_data);
        // 3. 视图(ListView)加载适配器
       // listView.setAdapter( arr_aAdapter);
        // 监听器
        listView.setOnItemClickListener(this);// 单击单个条目
        //listView.setOnScrollListener(this);// 视图在滚动中加载数据
getString(inputStream);
    }

    @Override
    protected void onDestroy() {
        if (mSpeechSynthesizer != null) this.mSpeechSynthesizer.release();//释放资源
        BdTextToSpeech.getInstance(this).release();
        super.onDestroy();
    }

    private void initView() {
        //edt_content = (EditText) findViewById(R.id.edt_content);
        textView = (TextView)this.findViewById(R.id.textview);
        textView.setClickable(true);
        textView.setFocusable(true);
        btn_speak = (Button) findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = textView.getText().toString();
                // mSpeechSynthesizer.speak(content);
                BdTextToSpeech.getInstance(Mainhello.this).speak(content);
                // Log.i(TAG, ">>>say: " + editText.getText().toString()); //调试用于打印一些比较重要的数据，这些数据是你非常想看到的数据、可帮你分析数据。对应info
            }
        });
    }

    //跳转页面按钮
    public void  Click_page(View view ){

        String str="1";
        textView.setText(str);
       // startActivity(new Intent(this, HellotowActivity.class));//红色部分为要打开的心窗口的类名

        try
        {
            startActivity(new Intent(this, HellotowActivity.class));//红色部分为要打开的心窗口的类名
        }
        catch (Exception ex)
        {
 // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }
    public void  getString(InputStream inputStream) {

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        int i = 0;
        String line;
        String[] myArray = new String[800];//定义字符数组
        ArrayList<String> arrayList = new ArrayList<String>();//动态数组
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
                myArray[i] = line;
                arrayList.add(i,line );
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        //通过生成字符数组 加载到list
        //Log.i(myArray.toString());
        //String[] arr_data = {"打招呼","Hello Nice To Meet You","Hello Nice To Meet You Too","How Are You","I'm Fine Thank You And You I'm Fine Too","询问敲门","Who Is That Boy He Is My Friend","Who Is That Girl She Is My Friend","敲门声","Come In Please Come In ","Nice To Meet You Again Nice To Meet You Too","What's Your Name","My name Is liu yi mo","How Old Are You ","Iam 7 Years Old","Who Is That Women? ","She Is Me English Teacher Miss Wang","Come In Please Come In ","Nice To Meet You Again Nice To Meet You Too","坐下聊天","What Do You Do In The Morning ","I go To The Children's Palace I Like Drawing","What Do You Do In The Morning?"," I stay At Home  I Watch Cartoons","What's This It's Little Dog ","It's Cute. What Color Is It?","It’S  White","Is This A Monkey? Yes"," It Is.  What Color Is It?","It’S  Yellow","Is This A Sheep? No"," It Is T. It's A Giraffe.","Do You Like Giraffe? Yes I Do","迟到","Who Is Not Here?","Sister Not Here","姐姐敲门","May I Come In Come In Please","I'm Sorry ","I'm Late It's Ok ","Don't Be Late Again Thank You","做游戏 老师：","Ok Please Be Quiet I Know You Take Some Toys. ","So  You Want To Play A Game?","How Many Toys Can You See Let Me Count","one two three four five six seven eight nine ten","I Have 9 Numbers. I Put Then In The Box. You Can Select One And That Number Toy Belongs To You.  Understand?  OK "," Let's Start","学生抽到数字后 拿玩具 说:The number is I have a I like it","打招呼","Hello Nice To Meet You Hello Nice To Meet You Too","你好，见到你很高兴，见到你我也很高兴。","How Are You	I'm Fine Thank You And You I'm Fine Too","你好吗？我很好，谢谢你，我是Fine Too。","询问敲门","询问敲门","Who Is That Boy He Is My Friend Who Is That Girl She Is My Friend","那个男孩是谁？他是我的朋友。那个女孩是谁？她是我的朋友。","敲门声","Come In Please Come In","请进，请进。","Nice To Meet You Again Nice To Meet You Too","很高兴再次见到你，见到你我也很高兴。","What's Your Name My name Is","你叫什么名字？我叫","How Old Are You Iam 7 Years Old","你多大了？我7岁了。","Who Is That Women? She Is Me English Teacher Miss Wang","那个女人是谁？她是我的英语老师王小姐。","Come In Please Come In Nice To Meet You Again Nice To Meet You Too","请进，很高兴再次见到你，很高兴认识你。","坐下聊天","What Do You Do In The Morning I go To The Children's Palace I Like Drawing","早上你做什么？我去少年宫我喜欢画画。","What Do You Do In The Morning? I stay At Home I Watch Cartoons","你早上干什么？我呆在家里看卡通片。","What's This It's Little Dog ","It's Cute. What Color Is It? It’S White","这是什么？它是小狗，它很可爱。它是什么颜色的？这里是White。","Is This A Monkey? Yes"," It Is. What Color Is It? It’S Yellow","这是一只猴子吗？是的，它是。它是什么颜色的？它是黄色的","Is This A Sheep? No"," It Is T. It's A Giraffe. Do You Like Giraffe? Yes I Do","这是绵羊吗？不，它是T。它是一只长颈鹿。你喜欢Giraffe吗？是的","","迟到","Who Is Not Here? Sister Not Here","谁不在这里？姐姐不在这里","","姐姐敲门","May I Come In Come In Please","我可以进来吗？","I'm Sorry I'm Late It's Ok Don't Be Late Again Thank You","对不起，我迟到了，没关系，请不要再迟到了，谢谢。","","做游戏 老师：","Ok Please Be Quiet I Know You Take Some Toys. So You Want To Play A Game?","好的，请安静，我知道你带了一些玩具。你想玩游戏吗？","How Many Toys Can You See Let Me Count 123456789 在反问老师","你能看到多少玩具让我数到123456789在反问老师","I Have 9 Numbers. I Put Then In The Box. You Can Select One And That Number Toy Belongs To You. Understand? OK "," Let's Start","我有9个数字。我把它放进盒子里。你可以选择一个，那个数字玩具就属于你。明白了吗？好吧，我们开始吧。","学生抽到数字后 拿玩具 说： The number is I have a I like it","学生抽到数字后拿玩具说：号码是  我有一个我喜欢它"};
        System.out.println(arrayList.toString());
        arr_aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        // 3. 视图(ListView)加载适配器
        listView.setAdapter( arr_aAdapter);

        //System.out.println(myArray[10]);
        // return sb.toString();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        String text = listView.getItemAtPosition(position) + "";// 指定位置的内容
        Toast.makeText(this, "positon=" + position + "text" + text,
                Toast.LENGTH_LONG).show();
        BdTextToSpeech.getInstance(Mainhello.this).speak(text);
    }

    /**
     * 初始化语音合成客户端并启动
     */
    private void initialTts() {
        //获取语音合成对象实例
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        //设置Context
        this.mSpeechSynthesizer.setContext(this);
        //设置语音合成状态监听
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
                + LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId(APP_ID);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey(API_KEY, SECRET_KEY);
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 授权检测接口(可以不使用，只是验证授权是否成功)
        // AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
        /// if (authInfo.isSuccess()) {
        //    Log.i(TAG, ">>>auth success.");
        // } else {
        //     String errorMsg = authInfo.getTtsError().getDetailMessage();
        //     Log.i(TAG, ">>>auth failed errorMsg: " + errorMsg);
        //   }
        // 引擎初始化tts接口
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        int result =
                mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
                        + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        Log.i(TAG, ">>>loadEnglishModel result: " + result);
    }

    @Override
    public void onSynthesizeStart(String s) {
        //监听到合成开始
        Log.i(TAG, ">>>onSynthesizeStart()<<< s: " + s);
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
        //监听到有合成数据到达
        Log.i(TAG, ">>>onSynthesizeDataArrived()<<< s: " + s);
    }

    @Override
    public void onSynthesizeFinish(String s) {
        //监听到合成结束
        Log.i(TAG, ">>>onSynthesizeFinish()<<< s: " + s);
    }

    @Override
    public void onSpeechStart(String s) {
        //监听到合成并开始播放
        Log.i(TAG, ">>>onSpeechStart()<<< s: " + s);
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        //监听到播放进度有变化
        Log.i(TAG, ">>>onSpeechProgressChanged()<<< s: " + s);
    }

    @Override
    public void onSpeechFinish(String s) {
        //监听到播放结束
        Log.i(TAG, ">>>onSpeechFinish()<<< s: " + s);
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        //监听到出错
        Log.i(TAG, ">>>onError()<<< description: " + speechError.description + ", code: " + speechError.code);
    }

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        File file = new File(mSampleDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);
    }

    /**
     * 将工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    public void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
