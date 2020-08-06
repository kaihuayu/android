package com.example.flylsb.myyy;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

//import com.baidu.tts.answer.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JunkChen on 2016/4/5 0005.
 */
public class BdTextToSpeech implements SpeechSynthesizerListener {
    private static final String TAG = "BdTextToSpeech";

    private static Context context;
    private SpeechSynthesizer mSpeechSynthesizer;
    private String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license_2016-04-05";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    //    private static final String APP_ID = "7965724";
//    private static final String API_KEY = "e9TQZ4CPEbQkbHVHrEWFdEhf";
//    private static final String SECRET_KEY = "c059606c70e2f76e08a03c348556cd90";
    private static final String APP_ID = "7957876";//请更换为自己创建的应用
    private static final String API_KEY = "cVN31pILxBhRNdGdlNHyeuyq";//请更换为自己创建的应用
    private static final String SECRET_KEY = "84e6987b56f11e6ee97e02ef25a2b4f0";//请更换为自己创建的应用

    private static BdTextToSpeech ourInstance;

    public static synchronized BdTextToSpeech getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new BdTextToSpeech(context);
        }
        return ourInstance;
    }

    private BdTextToSpeech(Context context) {
        Log.i(TAG, ">>>BdTextToSpeech executed.<<<");
        this.context = context;
        initialEnv();
        initialTts();
    }

    /**
     * 初始化语音合成客户端并启动
     */
    private void initialTts() {
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(context);
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
                + LICENSE_FILE_NAME);
        mSpeechSynthesizer.setAppId(APP_ID);
        mSpeechSynthesizer.setApiKey(API_KEY, SECRET_KEY);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
       // AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
       // if (authInfo.isSuccess()) {
          //  Log.i(TAG, ">>>auth success.");
       // } else {
         //   String errorMsg = authInfo.getTtsError().getDetailMessage();
         //   Log.i(TAG, ">>>auth failed errorMsg: " + errorMsg);
       // }
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        int result = mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" +
                ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        Log.i(TAG, ">>>loadEnglishModel result: " + result);
    }

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/RiiECG/" + SAMPLE_DIR_NAME;
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
                is = context.getResources().getAssets().open(source);
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

    public int speak(String text) {
        return this.mSpeechSynthesizer.speak(text);
    }

    public void release() {
        if (mSpeechSynthesizer != null) this.mSpeechSynthesizer.release();
    }

    @Override
    public void onSynthesizeStart(String s) {

    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onSpeechFinish(String s) {

    }

    @Override
    public void onError(String s, SpeechError speechError) {

    }
}
